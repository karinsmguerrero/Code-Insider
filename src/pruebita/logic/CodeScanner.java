/**
 * 
 */
package pruebita.logic;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
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
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author fmuri
 *
 */
public class CodeScanner {
	Display display;
	SimpleList graph_list;
	String code = "";
	String complexity;

	
	public CodeScanner(Display display) {
		this.display = display;
		this.graph_list = new SimpleList();
		this.complexity = "";
	}
	
	public String getCode(){
		return code;
	}
	
	public SimpleList scanWorkspace() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		
		IProject[] projects = root.getProjects();
		for (IProject project : projects) {
			if (project.getName().equals("saxs")) {
				try {
					printProjectInfo(project);
				} catch (CoreException | BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
		return this.graph_list;
	}
	
	private void printProjectInfo(IProject project) throws CoreException, BadLocationException {
		System.out.println("Working on project " + project.getName());;
		
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			printPackageInfos(javaProject);
		}
	}
	
	private void printPackageInfos(IJavaProject javaProject) throws JavaModelException, BadLocationException {
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				System.out.println("Package " + mypackage.getElementName());
				printICompilationUnitInfo(mypackage);
			}
		}
	}
	
	private void printICompilationUnitInfo(IPackageFragment mypackage) throws JavaModelException, BadLocationException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			printCompilationUnitDetails(unit);
		}
	}
	
	private void printIMethods(ICompilationUnit unit) throws JavaModelException {
		IType[] allTypes = unit.getAllTypes();
		for (IType type : allTypes) {
			printIMethodDetails(type);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void printCompilationUnitDetails(ICompilationUnit unit) throws JavaModelException, BadLocationException {
		System.out.println("Source file " + unit.getElementName());
		Document doc = new Document(unit.getSource());
		String line = doc.get();
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(line.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		
		cu.accept(new ASTVisitor() {
			Set names = new HashSet();
			SimpleList listaStatements = new SimpleList();
			
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				//System.out.println("Declaration of '" + name + "' at line" + cu.getLineNumber(name.getStartPosition()));
				code += "Declaration of '" + name + "' at line" + cu.getLineNumber(name.getStartPosition()) + "\n";
				
				Image simple_statement = new Image(display, "C:\\Users\\karin\\workspace3\\pruebita\\src\\pruebita\\gui\\image\\simple_statement.png");
				SimpleStatement declaration = new SimpleStatement("declaration", name.toString(), simple_statement);
				Node statement = new Node(declaration);
				graph_list.add_element(statement);
				return false;
			}
			
			public boolean visit(SimpleName node) {
				if(this.names.contains(node.getIdentifier())) {
					//System.out.println("Usage of '" + node + "' at line" + cu.getLineNumber(node.getStartPosition()));
					code += "Usage of '" + node + "' at line" + cu.getLineNumber(node.getStartPosition()) + "\n";
				}
				return true;
			}
			
			public boolean visit(ForStatement node) {
//				System.out.println("For found at line " + cu.getLineNumber(node.getStartPosition()));
				code += "For found at line " + cu.getLineNumber(node.getStartPosition()) + "\n";
//				System.out.println("For condition: " + node.getExpression().toString());
				code += "For condition: " + node.getExpression().toString() + "\n";
				
//				System.out.println("For Body: " + node.getBody().toString());
				code += "For Body: " + node.getBody().toString() + "\n";
				
				Node insertar = new Node(node.getBody().toString());
				this.listaStatements.add_element(insertar);

				Image for_statement = new Image(display, "C:\\Users\\karin\\workspace3\\pruebita\\src\\pruebita\\gui\\image\\for_statement.png");
				CicleStatement statement = new CicleStatement("for", node.getBody().toString(), for_statement, node.getExpression().toString());
				Node forStatement = new Node(statement);
				graph_list.add_element(forStatement);
				return true;
			}
			
			public boolean visit(WhileStatement node) {
//				System.out.println("While found at line: " + cu.getLineNumber(node.getStartPosition()));
				code += "While found at line: " + cu.getLineNumber(node.getStartPosition()) + "\n";
//				System.out.println("While condition: " + node.getExpression().toString());
				code += "While condition: " + node.getExpression().toString() + "\n";
//				System.out.println(("While body: " + node.getBody().toString()));
				code += "While body: " + node.getBody().toString() + "\n";
				Node insertar = new Node(node.getBody().toString());
				this.listaStatements.add_element(insertar);
				
				Image while_statement = new Image(display, "C:\\Users\\karin\\workspace3\\pruebita\\src\\pruebita\\gui\\image\\while_statement.png");
				CicleStatement statement = new CicleStatement("while", node.getBody().toString(), while_statement, node.getExpression().toString());
				Node whileStatement = new Node(statement);
				graph_list.add_element(whileStatement);
				return true;
			}
			
			public boolean visit(IfStatement node) {
//				System.out.println("If found in line: " + cu.getLineNumber(node.getStartPosition()));
				code += "If found in line: " + cu.getLineNumber(node.getStartPosition()) + "\n";
//				System.out.println("If condition: " + node.getExpression().toString());
				code += "If condition: " + node.getExpression().toString() + "\n";
//				System.out.println("If body: " + node.getThenStatement().toString());
				code += "If body: " + node.getThenStatement().toString() + "\n";
				
				Image if_statement = new Image(display, "C:\\Users\\karin\\workspace3\\pruebita\\src\\pruebita\\gui\\image\\if_statement.png");
				SimpleStatement ifStatement = new SimpleStatement("if", node.getExpression().toString(), if_statement);
				Node if_node = new Node(ifStatement);
				graph_list.add_element(if_node);
				return true;
			}
			
			public boolean visit(ExpressionStatement node) {
				if(!this.listaStatements.listContains(node.getExpression().toString())) {
					//System.out.println(node.getExpression().toString());
					code += node.getExpression().toString() + "\n";
					
					Image simple_statement = new Image(display, "C:\\Users\\karin\\workspace3\\pruebita\\src\\pruebita\\gui\\image\\simple_statement.png");
					SimpleStatement statement = new SimpleStatement("statement", node.getExpression().toString(), simple_statement);
					Node node_statement = new Node(statement);
					graph_list.add_element(node_statement);
				}
				return true;
			}
			
			public boolean visit(MethodInvocation node) {
				//System.out.println("Method Invoked: " + node.getName());
				code += "Method Invoked: " + node.getName() + "\n";
				
				Image external_statement = new Image(display, "C:\\Users\\karin\\workspace3\\pruebita\\src\\pruebita\\gui\\image\\external_method.png");
				SimpleStatement external = new SimpleStatement("external", node.getName().toString(), external_statement);
				Node node_external = new Node(external);
				graph_list.add_element(node_external);
				return true;
			}
		});
		//System.out.println("Has number of lines:" + doc.getNumberOfLines());
		code += "Has number of lines:" + doc.getNumberOfLines() + "\n";
		CodeVisitor visitor = new CodeVisitor(cu);
		cu.accept(visitor.getVisitor());
		this.graph_list = visitor.getGraph_list();
		this.complexity = visitor.getComplexity();
		System.out.println("Has number of lines:" + doc.getNumberOfLines());
	}
	
	private void printIMethodDetails(IType type) throws JavaModelException {
		IMethod[] methods = type.getMethods();
		for(IMethod method : methods) {
			System.out.println("Method name " + method.getElementName());
			System.out.println("Signature " + method.getSignature());
			System.out.println("Return Type " + method.getReturnType());
		}
	}
}
