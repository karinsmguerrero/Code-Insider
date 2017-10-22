/**
 * 
 */
package pruebita.logic;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author fmuri
 *
 */
public class CodeVisitor{
	
	private ASTVisitor visitor;
	private SimpleList graph_list;
	private String complexity;
	
	public CodeVisitor(CompilationUnit cu) {
		this.graph_list = new SimpleList();
		this.visitor = this.createVisitor(graph_list, cu);
		this.complexity = "";
	}
	
	private ASTVisitor createVisitor(SimpleList graph_list, CompilationUnit cu) {
		ASTVisitor visitor = new ASTVisitor() {
			Set names = new HashSet();
			SimpleList listaStatements = new SimpleList();
			
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '" + name + "' at line" + cu.getLineNumber(name.getStartPosition()));
				
				Bundle bundle = FrameworkUtil.getBundle(getClass());
				String path = "/gui/image/simple_statement.png";
				URL url = FileLocator.find(bundle, new Path(path), null);
				ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
				Image simple_statement = imageDesc.createImage();
				
				StatementFactory declaration = new StatementFactory("declaration", name.toString(), simple_statement);
				Node statement = new Node(declaration);
				graph_list.add_element(statement);
				return false;
			}
			
			public boolean visit(SimpleName node) {
				if(this.names.contains(node.getIdentifier())) {
					System.out.println("Usage of '" + node + "' at line" + cu.getLineNumber(node.getStartPosition()));					
				}
				return true;
			}
			
			public boolean visit(ForStatement node) {
				System.out.println("For found at line " + cu.getLineNumber(node.getStartPosition()));
				System.out.println("For condition: " + node.getExpression().toString());
				System.out.println("For Body: " + node.getBody().toString());
				Node insertar = new Node(node.getBody().toString());
				this.listaStatements.add_element(insertar);
				
				Bundle bundle = FrameworkUtil.getBundle(getClass());
				String path = "/gui/image/for_statement.png";
				URL url = FileLocator.find(bundle, new Path(path), null);
				ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
				Image for_statement = imageDesc.createImage();
				
				StatementFactory statement = new StatementFactory("for", node.getBody().toString(), for_statement, node.getExpression().toString());
				Node forStatement = new Node(statement);
			
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
			
			public boolean visit(WhileStatement node) {
				System.out.println("While found at line: " + cu.getLineNumber(node.getStartPosition()));
				System.out.println("While condition: " + node.getExpression().toString());
				System.out.println(("While body: " + node.getBody().toString()));
				Node insertar = new Node(node.getBody().toString());
				this.listaStatements.add_element(insertar);
				
				Bundle bundle = FrameworkUtil.getBundle(getClass());
				String path = "/gui/image/while_statement.png";
				URL url = FileLocator.find(bundle, new Path(path), null);
				ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
				Image while_statement = imageDesc.createImage();
				
				
				StatementFactory statement = new StatementFactory("while", node.getBody().toString(), while_statement, node.getExpression().toString());
				Node whileStatement = new Node(statement);
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
			
			public boolean visit(IfStatement node) {
				System.out.println("If found in line: " + cu.getLineNumber(node.getStartPosition()));
				System.out.println("If condition: " + node.getExpression().toString());
				System.out.println("If body: " + node.getThenStatement().toString());
				
				Bundle bundle = FrameworkUtil.getBundle(getClass());
				String path = "/gui/image/if_statement.png";
				URL url = FileLocator.find(bundle, new Path(path), null);
				ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
				Image if_statement = imageDesc.createImage();

				StatementFactory ifStatement = new StatementFactory("if", node.getExpression().toString(), if_statement);
				Node if_node = new Node(ifStatement);
				graph_list.add_element(if_node);
				return true;
			}
			
			public boolean visit(ExpressionStatement node) {
				if(!this.listaStatements.listContains(node.getExpression().toString())) {
					System.out.println(node.getExpression().toString());
					
					Bundle bundle = FrameworkUtil.getBundle(getClass());
					String path = "/gui/image/simple_statement.png";
					URL url = FileLocator.find(bundle, new Path(path), null);
					ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
					Image simple_statement = imageDesc.createImage();
					
					StatementFactory statement = new StatementFactory("statement", node.getExpression().toString(), simple_statement);
					Node node_statement = new Node(statement);
					graph_list.add_element(node_statement);
				}
				return true;
			}
			
			public boolean visit(MethodInvocation node) {
				System.out.println("Method Invoked: " + node.getName());
				
				Bundle bundle = FrameworkUtil.getBundle(getClass());
				String path = "/gui/image/external_method.png";
				URL url = FileLocator.find(bundle, new Path(path), null);
				ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
				Image external_statement = imageDesc.createImage();
				SimpleStatement external = new SimpleStatement("external", node.getName().toString(), external_statement);
				Node node_external = new Node(external);
				graph_list.add_element(node_external);
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
	
	
}
