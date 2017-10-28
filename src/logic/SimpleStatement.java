/**
 * 
 */
package logic;

import org.eclipse.swt.graphics.Image;

/**
 * @author fmuri
 *
 *Clase para almacenar la informacion de un statement  dentor del codigo
 */
public class SimpleStatement {
	
	/**
	 * Atributo utilizado para identificar el tipo de statement
	 */
	String type;
	/**
	 * Utilizado para almacenar la linea de codigo a la que hace referncia
	 */
	String statement;
	/**
	 * Atributo utilizado para almacenar la imagen del statement analizado
	 */
	Image image;
	
	/**
	 * @param type tipo de statement a crear
	 * @param statement linea de codigo que hace referencia al statement
	 * @param image imagen a almacenar.
	 */
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
