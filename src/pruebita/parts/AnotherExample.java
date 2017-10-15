package pruebita.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class AnotherExample {

	//private Label myLabelInView;
	private SashForm sashForm;
	private Text txtCode;
	private Group codeGroup, diagramGroup, dataGroup;

	/**
	 * Método que crea los controles
	 * Se deben declarar aquí los controles que aparecerán al inicio
	 * @param parent
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1,true));

		// Create the SashForm with HORIZONTAL
		//Un sash form permite arrastrar su borde y redimencionarlo 
	    sashForm = new SashForm(parent, SWT.HORIZONTAL);
	    //Se agrega el atributo de layout para permitir que el tamaño se maneje dinámicamente
	    sashForm.setLayout(new GridLayout(1, true));
	    //Un layout que ocupa todo el espacio disponible
	    sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    
	    codeGroup = new Group(sashForm, SWT.CENTER);
	    codeGroup.setText("Código");
	    codeGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    codeGroup.setLayout(new GridLayout(1, true));
	    
	    diagramGroup = new Group(sashForm, SWT.CENTER);
	    diagramGroup.setText("Diagrama de flujo");
	    diagramGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    diagramGroup.setLayout(new GridLayout(1, true));
	    
	    dataGroup = new Group(sashForm, SWT.CENTER);
	    dataGroup.setText("Variables");
	    dataGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    dataGroup.setLayout(new GridLayout(1, true));
	    
	    txtCode = new Text(codeGroup, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		txtCode.setText("This is a sample E4 view");
		txtCode.setLocation(10, 25);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtCode.pack();
		
		codeGroup.pack();
		
	}

	@Focus
	public void setFocus() {
		//sashForm.setFocus();
	}
	
	/**
	 * This method manages the selection of your current object. In this example
	 * we listen to a single Object (even the ISelection already captured in E3
	 * mode). <br/>
	 * You should change the parameter type of your received Object to manage
	 * your specific selection
	 * 
	 * @param o
	 *            : the current object received
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o) {
		// Test if label exists (inject methods are called before PostConstruct)
		if (txtCode != null)
			txtCode.setText("Current single selection class is : " + o.getClass());
	}

	/**
	 * This method manages the multiple selection of your current objects. <br/>
	 * You should change the parameter type of your array of Objects to manage
	 * your specific selection
	 * 
	 * @param o
	 *            : the current array of objects received in case of multiple selection
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object[] selectedObjects) {

		// Test if label exists (inject methods are called before PostConstruct)
		if (txtCode != null)
			txtCode.setText("This is a multiple selection of " + selectedObjects.length + " objects");
	}


}
