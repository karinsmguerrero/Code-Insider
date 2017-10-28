package logic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class DiagramPainter {
	private CodeScanner codesc;

	public void paintDiagram(Composite comp, SimpleList graph, int index, Label lbl) {
		Node selected = graph.get(index);
		if(selected.getDato().getClass() == SimpleStatement.class) {
			SimpleStatement draw = (SimpleStatement) selected.getDato();
			if(draw.getType().equals("statement") || draw.getType().equals("declaration")) {
				GC gc = new GC(draw.getImage());
				gc.drawText(draw.getStatement(),130,60,true);
				gc.dispose();
				lbl.setVisible(true);
				lbl.setImage(draw.getImage());
				lbl.setLocation(100, 100);
				lbl.pack();
			}
			if(draw.getType().equals("external")) {
				GC gc = new GC(draw.getImage());
				gc.drawText(draw.getStatement(),150,80,true);
				gc.dispose();
				lbl.setVisible(true);
				lbl.setImage(draw.getImage());
				lbl.setLocation(100, 100);
				lbl.pack();
			}
		}
		if(selected.getDato().getClass() == ConditionStatement.class) {
			ConditionStatement draw = (ConditionStatement) selected.getDato();
			if(draw.getType().equals("for")) {
				lbl.setVisible(true);
				GC gc = new GC(draw.getImage());
				gc.drawText(draw.getCondition(),215,90,true);
				gc.drawText(draw.getStatement(), 145, 230, true);
				gc.dispose();
				lbl.setImage(draw.getImage());
				lbl.pack();
			}
			if(draw.getType().equals("while")) {
				lbl.setVisible(true);
				GC gc = new GC(draw.getImage());
				gc.drawText(draw.getCondition(),190,110,true);
				gc.drawText(draw.getStatement(), 140, 235, true);
				gc.dispose();
				lbl.setImage(draw.getImage());
				lbl.pack();
			}

		}
	}

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
				ConditionStatement statement = (ConditionStatement) actual.getDato();
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
		for (int i = 0; i < list.getSize(); i++) {
			Label lbl = new Label(cnv, SWT.NONE);
			lbl.setImage(images[i]);
			lbls[i] = lbl;
		}
		return lbls;
		// codesc = new CodeScanner(cnv.getDisplay());
		// SimpleList sLst = codesc.scanWorkspace();
		// Image[] images = getImages(sLst);
		// for (int i = 0; i < 2; i++) {
		// // final Image image = images[i];
		// Label lbl = new Label(cnv, SWT.NONE);
		// final Image image = new Image(lbl.getDisplay(),
		// "C:\\Users\\karin\\desktop\\20161105_192330.jpg");
		// lbl.setImage(image);
		// lbl.pack();
		// }
	}

	private void paintAux(Canvas cnv) {
		// Point origin = new Point(0, 0);
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
			// cnv.setContent(lbl);
		}
		// cnv.pack();

	}

	public SimpleList getCode() {
		return getCodeAux();
	}

	private SimpleList getCodeAux() {
		return codesc.scanWorkspace();
	}
}
