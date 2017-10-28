/**
 * 
 */
package logic;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @author fmuri
 *
 */
public class CodeVisitor{
	
	/**
	 * Clase visitor (iterator) que se encarga de recorrer el AST del codigo generado para identificar la accion de cada linea de codigo
	 */
	private ASTVisitor visitor;
	/**
	 * Lista que contiene los statements que se encontraron en el codigo
	 */
	private SimpleList graph_list;
	/**
	 * String que guarda la complejidad algoritmica.
	 */
	private String complexity;
	/**
	 * Indica si la clase escaneada es la main class
	 */
	private Boolean is_main;
	/**
	 * Lista de todos los metodos declarados en esta clase.
	 */
	private SimpleList method_list;

	/**
	 * Display utilizado para dibujar las imagenes en swt.
	 */
	private Display display;
	
	public CodeVisitor(CompilationUnit cu, Display display) {
		this.graph_list = new SimpleList();
		this.visitor = this.createVisitor(graph_list, cu);
		this.complexity = "";
		this.is_main = false;
		this.method_list = new SimpleList();
		this.display = display;
	}
	
	private ASTVisitor createVisitor(SimpleList graph_list, CompilationUnit cu) {
		ASTVisitor visitor = new ASTVisitor() {
			Set names = new HashSet();
			SimpleList listaStatements = new SimpleList();
			
			/* (non-Javadoc)
			 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.VariableDeclarationFragment)
			 * QUe hacer en caso de que el nodo encontrado sea una declaracion de una variable.
			 */
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '" + name + "' at line" + cu.getLineNumber(name.getStartPosition()));
				Image simple_statement = new Image(display, CodeVisitor.class.getResourceAsStream("simple_statement.png"));
				
				StatementFactory declaration = new StatementFactory("declaration", name.toString(), simple_statement);
				SimpleStatement line = (SimpleStatement) declaration.getStatement();
				Node statement = new Node(line);
				graph_list.add_element(statement);
				return false;
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SimpleName)
			 * Caso en el que el nodo sea el uso de una variable.
			 */
			public boolean visit(SimpleName node) {
				if(this.names.contains(node.getIdentifier())) {
					System.out.println("Usage of '" + node + "' at line" + cu.getLineNumber(node.getStartPosition()));					
				}
				return true;
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ForStatement)
			 * En caso de que el nodo visitado sea un for
			 */
			public boolean visit(ForStatement node) {
				System.out.println("For found at line " + cu.getLineNumber(node.getStartPosition()));
				System.out.println("For condition: " + node.getExpression().toString());
				System.out.println("For Body: " + node.getBody().toString());
				Node insertar = new Node(node.getBody().toString());
				this.listaStatements.add_element(insertar);
				
				Image for_statement = new Image(display, CodeVisitor.class.getResourceAsStream("for_statement.png"));
				
				StatementFactory statement = new StatementFactory("for", node.getBody().toString(), for_statement, node.getExpression().toString());
				ConditionStatement line = (ConditionStatement) statement.getStatement();
				Node forStatement = new Node(line);
			
				String str = node.getBody().toString();
				String findStr = "for";
				int ultimoIndice = 0;
				int contador = 0;
				
				while(ultimoIndice != -1) {
					ultimoIndice = str.indexOf(findStr, ultimoIndice);
					
					if(ultimoIndice != -1) {
						contador++;
						ultimoIndice += findStr.length();
					}
				}
				if(complexity.equals("")) {
					complexity = "O(N)";
				}
				
				String complexity_aux[] = new String[contador];
				
				if (contador != 0) {
					complexity += "(";
				}
				for(int i = 0; i < contador; i++) {
					complexity_aux[i] = "O(N)";
				}
				for(int i = 0; i < contador; i++) {
					if(i + 1 == contador) {
						complexity += complexity_aux[i];
					} else {
						complexity += complexity_aux[i] + "+";
					}
				}
				
				findStr = "while";
				ultimoIndice = 0;
				contador = 0;
				
				while(ultimoIndice != -1) {
					ultimoIndice = str.indexOf(findStr, ultimoIndice);
					
					if(ultimoIndice != -1) {
						contador++;
						ultimoIndice += findStr.length();
					}
				}
				
				if (contador != 0) {
					complexity += "(";
				}
				
				String complexity_aux_two[] = new String[contador];
				
				for(int i = 0; i < contador; i++) {
					complexity_aux_two[i] = "O(N)";
				}
				for(int i = 0; i < contador; i++) {
					if(i + 1 == contador) {
						complexity += complexity_aux_two[i];
					} else {
						complexity += complexity_aux_two[i] + "+";
					}
				}
				graph_list.add_element(forStatement);
				return true;
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.WhileStatement)
			 * Caso en el que el nodo visitado sea un While
			 */
			public boolean visit(WhileStatement node) {
				System.out.println("While found at line: " + cu.getLineNumber(node.getStartPosition()));
				System.out.println("While condition: " + node.getExpression().toString());
				System.out.println(("While body: " + node.getBody().toString()));
				Node insertar = new Node(node.getBody().toString());
				this.listaStatements.add_element(insertar);
				
				Image while_statement = new Image(display, CodeVisitor.class.getResourceAsStream("while_statement.png"));
				
				
				StatementFactory statement = new StatementFactory("while", node.getBody().toString(), while_statement, node.getExpression().toString());
				ConditionStatement line = (ConditionStatement) statement.getStatement();
				Node whileStatement = new Node(line);
				graph_list.add_element(whileStatement);
				
				String str = node.getBody().toString();
				String findStr = "for";
				int ultimoIndice = 0;
				int contador = 0;
				
				while(ultimoIndice != -1) {
					ultimoIndice = str.indexOf(findStr, ultimoIndice);
					
					if(ultimoIndice != -1) {
						contador++;
						ultimoIndice += findStr.length();
					}
				}
				if(complexity.equals("")) {
					complexity = "O(N)";
				}else {
					complexity += "O(N)";
				}
				
				String complexity_aux[] = new String[contador];
				
				if (contador != 0) {
					complexity += "(";
				}
				for(int i = 0; i < contador; i++) {
					complexity_aux[i] = "O(N)";
				}
				for(int i = 0; i < contador; i++) {
					if(i + 1 == contador) {
						complexity += complexity_aux[i];
					} else {
						complexity += complexity_aux[i] + "+";
					}
				}
				
				findStr = "while";
				ultimoIndice = 0;
				contador = 0;
				
				while(ultimoIndice != -1) {
					ultimoIndice = str.indexOf(findStr, ultimoIndice);
					
					if(ultimoIndice != -1) {
						contador++;
						ultimoIndice += findStr.length();
					}
				}
				
				if (contador != 0) {
					complexity += "(";
				}
				
				String complexity_aux_two[] = new String[contador];
				
				for(int i = 0; i < contador; i++) {
					complexity_aux_two[i] = "O(N)";
				}
				for(int i = 0; i < contador; i++) {
					if(i + 1 == contador) {
						complexity += complexity_aux_two[i];
					} else {
						complexity += complexity_aux_two[i] + "+";
					}
				}
				return true;
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.IfStatement)
			 * Caso en el que el nodo visitado sea un if
			 */
			public boolean visit(IfStatement node) {
				System.out.println("If found in line: " + cu.getLineNumber(node.getStartPosition()));
				System.out.println("If condition: " + node.getExpression().toString());
				System.out.println("If body: " + node.getThenStatement().toString());
				
				Image if_statement = new Image(display, CodeVisitor.class.getResourceAsStream("if_statement.png"));

				StatementFactory ifStatement = new StatementFactory("if", node.getExpression().toString(), if_statement, node.getThenStatement().toString());
				ConditionStatement line = (ConditionStatement) ifStatement.getStatement();
				Node if_node = new Node(line);
				graph_list.add_element(if_node);
				return true;
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ExpressionStatement)
			 * caso en el que el nodo visitado sea una expresion
			 */
			public boolean visit(ExpressionStatement node) {
				if(!this.listaStatements.listContains(node.getExpression().toString())) {
					System.out.println(node.getExpression().toString());
					
					Image simple_statement = new Image(display, CodeVisitor.class.getResourceAsStream("simple_statement.png"));
					
					StatementFactory statement = new StatementFactory("statement", node.getExpression().toString(), simple_statement);
					SimpleStatement line = (SimpleStatement) statement.getStatement();
					Node node_statement = new Node(line);
					graph_list.add_element(node_statement);
				}
				return true;
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodInvocation)
			 */
			public boolean visit(MethodInvocation node) {
				System.out.println("Method Invoked: " + node.getName());
				
				Image external_statement = new Image(display, CodeVisitor.class.getResourceAsStream("external_method.png"));
				
				StatementFactory external = new StatementFactory("external", node.getName().toString(), external_statement);
				SimpleStatement line = (SimpleStatement) external.getStatement();
				Node node_external = new Node(line);
				graph_list.add_element(node_external);
				return true;
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodDeclaration)
			 * caso en el que el nodo visitado sea una declaracion de un metodo.
			 */
			public boolean visit(MethodDeclaration node) {
				System.out.println("Method declared: " + node.getName().toString());
				if(node.getName().toString().equals("main")) {
					is_main = true;
				}else {
					Image external_statement = new Image(display, CodeVisitor.class.getResourceAsStream("external_method.png"));
					StatementFactory factory = new StatementFactory("method", node.getName().toString(), external_statement, node.getBody().toString());
					ConditionStatement line = (ConditionStatement) factory.getStatement();
					Node node_method = new Node(line);
					method_list.add_element(node_method);
				}
				return true;
			}
		};
		
		return visitor;
	}

	public ASTVisitor getVisitor() {
		return this.visitor;
	}

	public SimpleList getGraph_list() {
		return graph_list;
	}

	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}
	
	public SimpleList getMethodList() {
		return this.method_list;
	}

	public Boolean getIs_main() {
		return this.is_main;
	}
	
	
	
	
}
