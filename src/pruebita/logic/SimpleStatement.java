/**
 * 
 */
package logic;

import org.eclipse.swt.graphics.Image;

/**
 * @author fmuri
 *
 */
public class SimpleStatement {
	
	String type;
	String statement;
	Image image;
	
	public SimpleStatement(String type, String statement, Image image){
		this.type = type;
		this.statement = statement;
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
	
	

}
