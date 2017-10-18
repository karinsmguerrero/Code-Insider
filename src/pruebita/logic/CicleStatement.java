package pruebita.logic;

import org.eclipse.swt.graphics.Image;

public class CicleStatement extends SimpleStatement {
	String condition;
	
	public CicleStatement(String type, String statement, Image image, String condition) {
		super(type, statement, image);
		this.condition = condition;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
