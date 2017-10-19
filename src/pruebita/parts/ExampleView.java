package pruebita.parts;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ExampleView {

	public static void main(String[] args) {

        // setup the SWT window
        Display display = new Display();
        final Shell shell = new Shell(display);
        FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
        shell.setLayout(fillLayout);
        shell.setText("Photo Application");

        // initialize a parent composite with a grid layout manager
        // with 5x columns
        Composite parent = new Composite(shell, SWT.NONE);
        parent.setLayout(fillLayout);

        // Get the Display default icons
        ArrayList<Image> imageList = new ArrayList<Image>();

        imageList.add(Display.getDefault().getSystemImage(SWT.ICON_WARNING));
        imageList.add(Display.getDefault().getSystemImage(SWT.ICON_WORKING));
        imageList.add(Display.getDefault().getSystemImage(SWT.ICON_QUESTION));
        imageList.add(Display.getDefault().getSystemImage(SWT.ICON_INFORMATION));
        imageList.add(Display.getDefault().getSystemImage(SWT.ICON_ERROR));

        for (Image image : imageList) {
            Label label = new Label(parent, SWT.NONE);
            label.setImage(image);
        }
        // show the SWT window

        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        // tear down the SWT window
        display.dispose();
        // if you do not use system images you would have to release them
        // not necessary in this example
        // for (Image image : imageList) {
        //    if (image != null) {
        //     image.dispose();
        //    }
        //    }
    }
}