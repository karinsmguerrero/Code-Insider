package logic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class DiagramContainer {

	public ScrolledComposite createContainer(Group container) {
		return createContainerAux(container);
	}

	private  ScrolledComposite createContainerAux(Group container) {
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		ScrolledComposite scroller = new ScrolledComposite(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);

		Composite parent = new Composite(scroller, SWT.NONE);
		parent.setLayout(fillLayout);
		parent.pack();
		scroller.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scroller.setContent(parent);

		return scroller;

	}

	public static void main(String[] args) {

		// setup the SWT window
		Display display = new Display();
		Shell shell = new Shell(display);
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		shell.setLayout(fillLayout);
		shell.setText("Photo Application");

		Group group = new Group(shell, SWT.NONE);
		group.setText("Variables");
		group.setLayout(fillLayout);

		ScrolledComposite scroller = new ScrolledComposite(group, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);

		Composite parent = new Composite(scroller, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(fillLayout);
		// parent.setLayout(fillLayout);
		Label lbl = new Label(parent, SWT.NONE);
		lbl.setImage(new Image(lbl.getDisplay(), "C:\\Users\\karin\\desktop\\20161105_192330.jpg"));
		lbl.pack();
		Label lbl2 = new Label(parent, SWT.NONE);
		lbl2.setImage(new Image(lbl2.getDisplay(), "C:\\Users\\karin\\desktop\\20161105_192336.jpg"));
		lbl2.pack();
		parent.pack();
		scroller.setContent(parent);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		// tear down the SWT window
		display.dispose();

	}

}
