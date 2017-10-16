package pruebita.parts;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.editors.text.TextEditor;

public class ExampleView {

	//@Inject
	//ESelectionService selectionService;
	
	@Inject
	public ExampleView(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Hello World! Here I am");
	}
	
	@CanExecute
	public boolean canExecute ( @Named ( IServiceConstants. ACTIVE_SELECTION )
	@Optional Object selection ) {
	if ( selection != null && selection instanceof TextEditor)
	return true;
	return false;
	}


}
