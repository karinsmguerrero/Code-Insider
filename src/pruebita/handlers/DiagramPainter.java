package pruebita.handlers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import pruebita.logic.CicleStatement;
import pruebita.logic.CodeScanner;
import pruebita.logic.Node;
import pruebita.logic.SimpleList;
import pruebita.logic.SimpleStatement;

public class DiagramPainter {
	private CodeScanner codesc;

	public void paint(Canvas cnv) {
		paintAux(cnv);
	}
	
	public Label[] getLabels(Composite cnv) {
		return getLabelsAux(cnv);
	}

	private Image[] getImages(SimpleList list) {
		Node actual = list.getHead();
		Image[] images = new Image[list.getSize()];
		for (int i = 0; i < list.getSize(); i++) {

			if (actual.getDato().getClass().equals(SimpleStatement.class)) {
				SimpleStatement statement = (SimpleStatement) actual.getDato();
				images[i] = statement.getImage();
			} else {
				CicleStatement statement = (CicleStatement) actual.getDato();
				images[i] = statement.getImage();
			}
			actual = actual.getNext();
		}
		return images;
	}

	private Label[] getLabelsAux(Composite cnv) {
		SimpleList list = codesc.scanWorkspace();
		Image[] images = getImages(list);
		Label[] lbls = new Label[list.getSize()];
		for(int i = 0; i < list.getSize();i++){
			Label lbl = new Label(cnv, SWT.NONE);
			lbl.setImage(images[i]);
			lbls[i] = lbl;
		}
		return lbls;
//		codesc = new CodeScanner(cnv.getDisplay());
//		SimpleList sLst = codesc.scanWorkspace();
//		Image[] images = getImages(sLst);
//		for (int i = 0; i < 2; i++) {
//		//	final Image image = images[i];
//			Label lbl = new Label(cnv, SWT.NONE);
//			final Image image = new Image(lbl.getDisplay(), "C:\\Users\\karin\\desktop\\20161105_192330.jpg");
//			lbl.setImage(image);
//			lbl.pack();
//		}
	}
	
	private void paintAux(Canvas cnv) {
		//Point origin = new Point(0, 0);
		codesc = new CodeScanner(cnv.getDisplay());
		SimpleList sLst = codesc.scanWorkspace();
		Image[] images = getImages(sLst);
		for (int i = 0; i < images.length; i++) {
			final Image image = images[i];
			Label lbl = new Label(cnv, SWT.NONE);
			lbl.setImage(image);
			lbl.pack();
			// cnv.addListener(SWT.Paint, e -> {
			// GC gc = e.gc;
			// gc.drawImage(image, origin.x, origin.y);
			// Rectangle rect = image.getBounds();
			// Rectangle client = cnv.getClientArea();
			// int marginWidth = client.width - rect.width;
			// if (marginWidth > 0) {
			// gc.fillRectangle(rect.width, 0, marginWidth, client.height);
			// }
			// int marginHeight = client.height - rect.height;
			// if (marginHeight > 0) {
			// gc.fillRectangle(0, rect.height, client.width, marginHeight);
			// }
			// });
			//cnv.setContent(lbl);
		}
		//cnv.pack();

	}
	
	public String getCode() {
		return getCodeAux();
	}

	private String getCodeAux() {
		return codesc.getCode();
	}
}
