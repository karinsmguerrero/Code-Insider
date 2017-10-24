package debugger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;

public class SimpleTrace {

	public SimpleTrace(String[] args) {
		VirtualMachine vm = launchConnect(args);
		monitorJVM(vm);
	}
	
	  public static void main(String[] args)
	  { 
	    if (args.length == 0)
	      System.err.println("Usage: runTrace <program>");
	    else
	      new SimpleTrace(args);  
	  } 

	private VirtualMachine launchConnect(String[] args) {
		VirtualMachine vm = null;
		LaunchingConnector conn = getCommandLineConnector();
		Map<String, Connector.Argument> connArgs = setMainArgs(conn, args);
		try {
			vm = conn.launch(connArgs); // launch the JVM and connect to it
		} catch (IOException e) {
			throw new Error("Unable to launch JVM: " + e);
		} catch (IllegalConnectorArgumentsException e) {
			throw new Error("Internal error: " + e);
		} catch (VMStartException e) {
			throw new Error("JVM failed to start: " + e);
		}
		return vm;
	} // end of launchConnect()

	private LaunchingConnector getCommandLineConnector() {
		List<Connector> conns = Bootstrap.virtualMachineManager().allConnectors();
		for (Connector conn : conns) {
			if (conn.name().equals("com.sun.jdi.CommandLineLaunch"))
				return (LaunchingConnector) conn;
		}
		throw new Error("No launching connector found");
	}

	private Map<String, Connector.Argument> setMainArgs(LaunchingConnector conn, String[] args) {
		// get connector field for program's main() method
		Map<String, Connector.Argument> connArgs = conn.defaultArguments();
		Connector.Argument mArgs = (Connector.Argument) connArgs.get("main");
		if (mArgs == null)
			throw new Error("Bad launching connector");
		// concatenate all tracer's input args into a single string
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++)
			sb.append(args[i] + " ");
		mArgs.setValue(sb.toString());
		// assign args to main field
		return connArgs;
	} // end of setMainArgs()

	private void monitorJVM(VirtualMachine vm) {
		// start JDI event handler which displays trace info
		JDIEventMonitor watcher = new JDIEventMonitor(vm);
		watcher.start();
		/*
		 * redirect VM's output and error streams to the system output and error
		 * streams
		 */
		Process process = vm.process();
		Thread errRedirect = new StreamRedirecter("error reader", process.getErrorStream(), System.err);
		Thread outRedirect = new StreamRedirecter("outputreader", process.getInputStream(), System.out);
		errRedirect.start();
		outRedirect.start();
		vm.resume(); // start the application
		try {
			watcher.join(); // Wait until JDI watcher terminates
			errRedirect.join(); // make sure all outputs have been forwarded
			outRedirect.join();
		} catch (InterruptedException e) {
		}
	} // end of monitorJVM()

}
