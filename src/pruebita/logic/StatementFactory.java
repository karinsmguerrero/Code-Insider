/**
 * 
 */
package pruebita.logic;

import org.eclipse.swt.graphics.Image;

/**
 * @author fmuri
 *
 */
public class StatementFactory {

	private Object statement;
	
	public StatementFactory(String type, String context, Image image) {
		this.statement = new SimpleStatement(type, context, image);
	}
	
	public StatementFactory(String type, String context, Image image, String body) {
		this.statement = new ConditionStatement(type, context, image, body);
		
	}

	public Object getStatement() {
		return statement;
	}

	
	
	
}
