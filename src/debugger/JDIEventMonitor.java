package debugger;

import java.util.List;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

public class JDIEventMonitor extends Thread{

	// globals
	// exclude events generated for these classes
	private final String[] excludes = { "java.*", "javax.*", "sun.*", "com.sun.*" };
	private final VirtualMachine vm; // the JVM
	private boolean connected = true; // connected to VM?
	private boolean vmDied; // has VM death occurred?
	private ShowCode showCode;

	public JDIEventMonitor(VirtualMachine vm) {
		this.vm = vm;
		vmDied = false;
		setEventRequests();
		showCode = new ShowCode();
	}

	private void setEventRequests() {
		EventRequestManager mgr = vm.eventRequestManager();
		MethodEntryRequest menr = mgr.createMethodEntryRequest();
		for (int i = 0; i < excludes.length; ++i)
			// report method entries
			menr.addClassExclusionFilter(excludes[i]);
		menr.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
		menr.enable();
		MethodExitRequest mexr = mgr.createMethodExitRequest();
		for (int i = 0; i < excludes.length; ++i)
			// report method exits
			mexr.addClassExclusionFilter(excludes[i]);
		mexr.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
		mexr.enable();
		ClassPrepareRequest cpr = mgr.createClassPrepareRequest();
		for (int i = 0; i < excludes.length; ++i)
			// report class loads
			cpr.addClassExclusionFilter(excludes[i]);
		cpr.enable();
		ClassUnloadRequest cur = mgr.createClassUnloadRequest();
		for (int i = 0; i < excludes.length; ++i)
			// report class unloads
			cur.addClassExclusionFilter(excludes[i]);
		cur.enable();
		ThreadStartRequest tsr = mgr.createThreadStartRequest();
		tsr.enable();
		// report thread starts
		ThreadDeathRequest tdr = mgr.createThreadDeathRequest();
		tdr.enable();
		// report thread deaths
	} // end of setEventRequests()

	public void run() {
		EventQueue queue = vm.eventQueue();
		while (connected) {
			try {
				EventSet eventSet = queue.remove();
				for (Event event : eventSet)
					handleEvent(event);
				eventSet.resume();
			} catch (InterruptedException e) {
			} // Ignore
			catch (VMDisconnectedException discExc) {
				handleDisconnectedException();
				break;
			}
		}
	} // end of run()

	private synchronized void handleDisconnectedException() {
		EventQueue queue = vm.eventQueue();
		while (connected) {
			try {
				EventSet eventSet = queue.remove();
				for (Event event : eventSet) {
					if (event instanceof VMDeathEvent)
						vmDeathEvent((VMDeathEvent) event);
					else if (event instanceof VMDisconnectEvent)
						vmDisconnectEvent((VMDisconnectEvent) event);
				}
				eventSet.resume();
			} catch (InterruptedException e) {
			} // ignore
		}
	} // end of handleDisconnectedException()


	private void vmDeathEvent(VMDeathEvent event)
	// Notification of VM termination
	{
		vmDied = true;
		System.out.println("-- The application has exited --");
	}

	private void vmDisconnectEvent(VMDisconnectEvent event)
	/*
	 * Notification of VM disconnection, either through normal termination or
	 * because of an exception/error.
	 */
	{
		connected = false;
		if (!vmDied)
			System.out.println("- The application has been disconnected -");
	}

	private void handleEvent(Event event) {
		// method events
		if (event instanceof MethodEntryEvent)
			methodEntryEvent((MethodEntryEvent) event);
		else if (event instanceof MethodExitEvent)
			methodExitEvent((MethodExitEvent) event);
		// class events
		else if (event instanceof ClassPrepareEvent)
			classPrepareEvent((ClassPrepareEvent) event);
		else if (event instanceof ClassUnloadEvent)
			classUnloadEvent((ClassUnloadEvent) event);
		// thread events
		else if (event instanceof ThreadStartEvent)
			threadStartEvent((ThreadStartEvent) event);
		else if (event instanceof ThreadDeathEvent)
			threadDeathEvent((ThreadDeathEvent) event);
		// step event -- a line of code is about to be executed
		else if (event instanceof StepEvent)
			stepEvent((StepEvent) event);
		// modified field event -- a field is about to be changed
		else if (event instanceof ModificationWatchpointEvent)
			fieldWatchEvent((ModificationWatchpointEvent) event);
		// VM events
		else if (event instanceof VMStartEvent)
			vmStartEvent((VMStartEvent) event);
		else if (event instanceof VMDeathEvent)
			vmDeathEvent((VMDeathEvent) event);
		else if (event instanceof VMDisconnectEvent)
			vmDisconnectEvent((VMDisconnectEvent) event);
		else
			throw new Error("Unexpected event type");
	} // end of handleEvent()

	private void methodEntryEvent(MethodEntryEvent event)
	// entered a method but no code executed yet
	{
		Method meth = event.method();
		String className = meth.declaringType().name();
		System.out.println();
		if (meth.isConstructor())
			System.out.println("entered " + className + " constructor");
		else
			System.out.println("entered " + className + "." + meth.name() + "()");
	} // end of methodEntryEvent()

	private void methodExitEvent(MethodExitEvent event)
	// all code in method has been executed, and about to return
	{
		Method meth = event.method();
		String className = meth.declaringType().name();
		if (meth.isConstructor())
			System.out.println("exiting " + className + " constructor");
		else
			System.out.println("exiting " + className + "." + meth.name() + "()");
		System.out.println();
	} // end of methodExitEvent()

	private void classUnloadEvent(ClassUnloadEvent event) {
		if (!vmDied)
			System.out.println("unloaded class: " + event.className());
	}

	private void classPrepareEvent(ClassPrepareEvent event)
	// a new class has been loaded
	{
		ReferenceType ref = event.referenceType();
		List<Field> fields = ref.fields();
		List<Method> methods = ref.methods();
		String fnm;
		try {
			fnm = ref.sourceName(); // get filename of the class
			showCode.add(fnm);
		} catch (AbsentInformationException e) {
			fnm = "??";
		}
		System.out.println("loaded class: " + ref.name() + " from " + fnm + " - fields=" + fields.size() + ", methods="
				+ methods.size());
		System.out.println("  method names: ");
		for (Method m : methods)
			System.out.println("    | " + m.name() + "()");
		setFieldsWatch(fields);
	} // end of classPrepareEvent()

	private void setFieldsWatch(List<Field> fields) {
		EventRequestManager mgr = vm.eventRequestManager();
		for (Field field : fields) {
			ModificationWatchpointRequest req = mgr.createModificationWatchpointRequest(field);
			for (int i = 0; i < excludes.length; i++)
				req.addClassExclusionFilter(excludes[i]);
			req.setSuspendPolicy(EventRequest.SUSPEND_NONE);
			req.enable();
		}
	} // end of setFieldsWatch()

	private void fieldWatchEvent(ModificationWatchpointEvent event) {
		Field f = event.field();
		Value value = event.valueToBe();
		// value that _will_ be assigned
		System.out.println("Field modified" + "    > " + f.name() + " = " + value);
	}
	// end of fieldWatchEvent()

	private void threadDeathEvent(ThreadDeathEvent event)
	// the thread is about to terminate
	{
		ThreadReference thr = event.thread();
		if (thr.name().equals("DestroyJavaVM") || thr.name().startsWith("AWT-"))
			return;
		if (thr.threadGroup().name().equals("system"))// ignore sys threads
			return;
		System.out.println(thr.name() + " thread about to die");
	} // end of threadDeathEvent()

	private void threadStartEvent(ThreadStartEvent event)
	// a new thread has started running -- switch on single stepping
	{
		ThreadReference thr = event.thread();
		if (thr.name().equals("Signal Dispatcher") || thr.name().equals("DestroyJavaVM")
				|| thr.name().startsWith("AWT-")) // AWT threads
			return;
		if (thr.threadGroup().name().equals("system"))// ignore systhreads
			return;
		System.out.println(thr.name() + " thread started");
		setStepping(thr);
	} // end of threadStartEvent()

	private void setStepping(ThreadReference thr)
	// start single stepping through the new thread
	{
		EventRequestManager mgr = vm.eventRequestManager();
        
		StepRequest sr = mgr.createStepRequest(thr, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
		sr.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
		for (int i = 0; i < excludes.length; ++i)
			sr.addClassExclusionFilter(excludes[i]);
		sr.enable();
	} // end of setStepping()

	private void stepEvent(StepEvent event) {
		Location loc = event.location();
		try { // print the line
			String fnm = loc.sourceName();
			// get code's filename
			System.out.println(fnm + ": " + showCode.show(fnm, loc.lineNumber()));
		} catch (AbsentInformationException e) {
		}
		if (loc.codeIndex() == 0)
			// at the start of a method
			printInitialState(event.thread());
	} // end of stepEvent()

	private void printInitialState(ThreadReference thr) {
		// get top- most stack frame
		StackFrame currFrame = null;
		try {
			currFrame = thr.frame(0);
		} catch (Exception e) {
			return;
		}
		printLocals(currFrame);
		// print fields for the 'this' object
		ObjectReference objRef = currFrame.thisObject();
		if (objRef != null) {
			System.out.println("  object: " + objRef.toString());
			printFields(objRef);
		}
	} // end of printInitialState()

	private void printLocals(StackFrame currFrame) {
		List<LocalVariable> locals = null;
		try {
			locals = currFrame.visibleVariables();
		} catch (Exception e) {
			return;
		}
		if (locals.size() == 0) // no local vars in the list
			return;
		System.out.println("  locals: ");
		for (LocalVariable l : locals)
			System.out.println("    | " + l.name() + " = " + currFrame.getValue(l));
	} // end of printLocals()

	private void printFields(ObjectReference objRef) {
		ReferenceType ref = objRef.referenceType(); // get class of object
		List<Field> fields = null;
		try {
			fields = ref.fields();
			// get fields from the class
		} catch (ClassNotPreparedException e) {
			return;
		}
		System.out.println("  fields: ");
		for (Field f : fields)
			// print field name and value
			System.out.println("    | " + f.name() + " = " + objRef.getValue(f));
	} // end of printFields()

	private void vmStartEvent(VMStartEvent event) {

		System.out.println("-- VM Started --");
	}

}
