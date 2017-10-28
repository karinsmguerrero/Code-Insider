package codeinsider.parts;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import logic.ClassSqueme;
import logic.CodeScanner;
import logic.ConditionStatement;
import logic.DiagramPainter;
import logic.FieldMonitor;
import logic.Node;
import logic.SimpleList;
import logic.SimpleStatement;

public class SampleView {
	private SashForm sashForm;
	private Text txtCode, txtVar, txtExternal;
	private Group codeGroup, diagramGroup, dataGroup;
	private Button btncode, btnExternal;
	private ScrolledComposite scroller;
	private Composite comp;
	private int index;
	private SimpleList graph, class_list;
	private Label lbl;

	@PostConstruct
	public void createPartControl(Composite parent) {
		this.index = 0;
		CodeScanner scan = new CodeScanner(parent.getDisplay());
		class_list = scan.scanWorkspace();
		ClassSqueme class_unit = this.getMainClass(class_list);
		this.graph = class_unit.getGrap_lList();
		
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		parent.setLayout(new GridLayout(1, true));

		// Create the SashForm with HORIZONTAL
		// Un sash form permite arrastrar su borde y redimencionarlo
		sashForm = new SashForm(parent, SWT.HORIZONTAL);
		// Se agrega el atributo de layout para permitir que el tamaño se maneje
		// dinámicamente
		sashForm.setLayout(new GridLayout(1, true));
		// Un layout que ocupa todo el espacio disponible
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		codeGroup = new Group(sashForm, SWT.CENTER);
		codeGroup.setText("Código");
		codeGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		codeGroup.setLayout(new GridLayout(1, true));

		diagramGroup = new Group(sashForm, SWT.CENTER);
		diagramGroup.setText("Diagrama de flujo");
		diagramGroup.setLayout(fillLayout);

		dataGroup = new Group(sashForm, SWT.CENTER);
		dataGroup.setText("Variables");
		dataGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dataGroup.setLayout(new GridLayout(1, true));

		txtCode = new Text(codeGroup, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		txtCode.setText(class_unit.getCode());
		txtCode.setLocation(10, 25);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtCode.pack();
		
		txtExternal = new Text(codeGroup, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		txtExternal.setLocation(10, 25);
		txtExternal.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtExternal.setText(class_unit.getCode());
		txtExternal.pack();

		scroller = new ScrolledComposite(diagramGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		scroller.setLayout(fillLayout);

		comp = new Composite(scroller, SWT.BORDER);
		comp.setLayout(fillLayout);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);
		scroller.setContent(comp);
		diagramGroup.pack();

		// scroller.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);
		scroller.setContent(comp);
		diagramGroup.pack();

		txtVar = new Text(dataGroup, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		txtVar.setLocation(10, 25);
		txtVar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtVar.pack();

		btncode = new Button(codeGroup, SWT.PUSH);
		btncode.setText("StepOver");
		btncode.addListener(SWT.Selection, event -> {
			try {
				click();
			} catch (IOException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		btnExternal = new Button(codeGroup, SWT.PUSH);
		btnExternal.addListener(SWT.Selection, event -> external());
		btnExternal.setText("StepInto");
		codeGroup.pack();
		
		DiagramPainter painter = new DiagramPainter();
		comp.setRedraw(false);
		this.lbl = new Label(comp, SWT.NONE);
		painter.paintDiagram(comp, graph, this.index, lbl);
		comp.setRedraw(true);
		comp.layout(true);
		scroller.setMinSize(comp.computeSize(SWT.DEFAULT, scroller.getSize().y));
		String path = "D:\\Users\\fmuri\\eclipse-workspace\\Test\\src\\Test.java";
		try {
			Runtime.getRuntime().exec("java -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -jar " + path);
			txtVar.setText("Running");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			txtVar.setText(e.toString());
		}

	}

	private void click() throws IOException, InterruptedException {

		if(this.index < this.graph.getSize()) {
			this.index += 1;
		}
		
		DiagramPainter painter = new DiagramPainter();

		comp.setRedraw(false);
		painter.paintDiagram(comp, this.graph, this.index, this.lbl);

		comp.setRedraw(true);
		comp.layout(true);
		scroller.setMinSize(comp.computeSize(SWT.DEFAULT, scroller.getSize().y));
		FieldMonitor monitor = new FieldMonitor();
		monitor.watch_vm();
	}
	
	private void external() {
		boolean found = false;
		Node selected = graph.get(index);
		SimpleStatement external = (SimpleStatement) selected.getDato();
		Node aux = class_list.getHead();
		for(int i = 0; i < class_list.getSize(); i++) {
			if(!found) {
				ClassSqueme class_unit = (ClassSqueme) aux.getDato();
				SimpleList methodlist = class_unit.getMethod_list();
				Node aux2 = methodlist.getHead();
				for(int j = 0; j < methodlist.getSize(); j++) {
					ConditionStatement external_condition = (ConditionStatement) aux2.getDato();
					if(external_condition.getStatement().equals(external.getStatement()) && !class_unit.getIs_main()) {
						txtExternal.setText(external_condition.getCondition());
						found = true;
						break;
					}else {
						aux2 = aux2.getNext();
					}
				
				}
				aux = aux.getNext();
			}else {
				break;
			}
				
		}
	}
	
	private ClassSqueme getMainClass(SimpleList class_list) {
		Node aux = class_list.getHead();
		for(int i = 0; i < class_list.getSize(); i++) {
			ClassSqueme class_unit = (ClassSqueme) aux.getDato();
			if(class_unit.getIs_main()) {
				return class_unit;
			}else {
				aux = aux.getNext();
			}
		}
		return null;
	}
}
