/**
 * 
 */
package pruebita.logic;

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

	public String getStatement() {
		return statement;
	}
	public Image getImage() {
		return image;
	}
	
	
	
	

}
