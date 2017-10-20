/**
 * 
 */
package logic;

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
	String complexity;
	
	public CodeScanner(Display display) {
		this.display = display;
		this.graph_list = new SimpleList();
		this.complexity = "";
	}
	
	public SimpleList scanWorkspace() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		
		IProject[] projects = root.getProjects();
		for (IProject project : projects) {
			if (project.getName().equals("Test")) {
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
