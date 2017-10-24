package pruebita.parts;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;

import debugger.SimpleTrace;
import pruebita.handlers.DiagramPainter;
import pruebita.logic.CodeScanner;

public class MainView {

	private SashForm sashForm;
	private Text txtCode, txtVar;
	private Group codeGroup, diagramGroup, dataGroup;
	private TextSelection txt;
	private Button btncode;
	private Canvas cnv;
	private ScrolledComposite scroller;
	private Image img;
	private Composite comp;
	private Point point = new Point(10, 10);

	/**
	 * Método que crea los controles Se deben declarar aquí los controles que
	 * aparecerán al inicio
	 * 
	 * @param parent
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {
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
		txtCode.setText("Seleccione el código que desea ejecutar");
		txtCode.setLocation(10, 25);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtCode.pack();

		// DiagramContainer diagram = new DiagramContainer();
		// scroller = diagram.createContainer(diagramGroup);
		// scroller.pack();
		// comp = (Composite)scroller.getChildren()[0];
		// diagramGroup.pack();

		// FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		scroller = new ScrolledComposite(diagramGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		scroller.setLayout(fillLayout);

		comp = new Composite(scroller, SWT.BORDER);
		// comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// comp.setLayout(new GridLayout(1, true));
		comp.setLayout(fillLayout);
		comp.pack();

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
		btncode.setText("Debug");
		btncode.addListener(SWT.Selection, event -> click());
		codeGroup.pack();

	}

	private void click() {

		Runtime runtime = Runtime.getRuntime();
		try {
			Process p1 = runtime.exec("cmd /c start C:\\Users\\karin\\Downloads\\SimpleTrace\\SimpleTrace\\runTrace.bat Example");
			InputStream is = p1.getInputStream();
			int i = 0;
			while ((i = is.read()) != -1) {
				System.out.print((char) i);
				System.out.println(is);
			}
		} catch (IOException ioException) {
			System.out.println(ioException.getMessage());
		}

		DiagramPainter painter = new DiagramPainter();

		comp.setRedraw(false);
		painter.paintDiagram(comp);
		// // painter.paint(cnv);
		// Label lbl = new Label(comp, SWT.NONE);
		// //lbl.setText("hola" + point.y);
		// lbl.setImage(new Image(lbl.getDisplay(),
		// "C:\\Users\\karin\\workspace3\\pruebita\\src\\pruebita\\gui\\image\\for_statement.png"));
		// //point.y = point.y += 10;
		// //lbl.setLocation(point);
		// lbl.pack();
		//
		comp.setRedraw(true);
		comp.layout(true);
		//
		scroller.setMinSize(comp.computeSize(SWT.DEFAULT, scroller.getSize().y));
		// // comp.setLayout(new FillLayout());
		//// comp.pack();
		//// scroller.setExpandVertical(true);
		//// scroller.setExpandHorizontal(true);
		//// scroller.setContent(comp);
		//
		// // txtVar.setText(painter.getCode());
		// // String mess = txtVar.getText() ;
		// // txtVar.setText(mess += "clicked");

	}

	@Focus
	public void setFocus() {

	}

	/**
	 * This method manages the selection of your current object. In this example
	 * we listen to a single Object (even the ISelection already captured in E3
	 * mode). <br/>
	 * You should change the parameter type of your received Object to manage
	 * your specific selection
	 * 
	 * @param o:
	 *            the current object received
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o) {
		// Test if label exists (inject methods are called before PostConstruct)
		if (txtCode != null) {
			if (o instanceof TextSelection) {
				txt = (TextSelection) o;
				txtCode.setText(txt.getText());
			}
		}

	}

}
