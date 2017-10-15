package pruebita.parts;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ExampleView {

	@Inject
	public ExampleView(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Hello World! Here I am");
	}


}
