package logic;

import org.eclipse.swt.graphics.Image;

/**
 * @author fmuri
 *
 *Clase que representara una accion con condicional (for, while o if). Esta hereda todos los metodos y atributos de su clase padre SimpleStatement
 */

public class ConditionStatement extends SimpleStatement {
	/**
	 * String que contiene la condicion para que se ejecuten acciones
	 */
	String condition;
	
	/**
	 * @param type tipo de statement
	 * @param statement El cuerpo de la declaracion.
	 * @param image La imagen que se desea asociar a esta declaracion.
	 * @param condition la condicion con la que se ejecuta la declaracion
	 */
	public ConditionStatement(String type, String statement, Image image, String condition) {
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
