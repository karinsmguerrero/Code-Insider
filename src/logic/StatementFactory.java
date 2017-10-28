/**
 * 
 */
package logic;

import org.eclipse.swt.graphics.Image;

/**
 * @author fmuri
 *
 */
public class StatementFactory {

	/**
	 * Objeto utilizado para almacenar la instancia de statement creada para luego retornarla.
	 */
	private Object statement;
	
	/**
	 * Constructor utilizado para crear instancias de SimpleStatement
	 * @param type tipo de statement a crear
	 * @param context el codigo del statement creado
	 * @param image  la imagen asociada al statement
	 */
	public StatementFactory(String type, String context, Image image) {
		this.statement = new SimpleStatement(type, context, image);
	}
	
	/**
	 * @param type tipo de statement que se desea crear
	 * @param context la condicion que hara que se ejecute las acciones.
	 * @param image La imagen que se le asignara al statement.
	 * @param body El codigo que conforma las acciones a realizar por el if, for, o while.
	 */
	public StatementFactory(String type, String context, Image image, String body) {
		this.statement = new ConditionStatement(type, context, image, body);
		
	}

	public Object getStatement() {
		return statement;
	}

	
	
	
}
