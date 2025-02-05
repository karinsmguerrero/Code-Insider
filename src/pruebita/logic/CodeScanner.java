/**
 * 
 */
package pruebita.logic;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.widgets.Display;

/**
 * @author fmuri
 *
 */
public class CodeScanner {
	Display display;
	SimpleList class_list;
	String complexity;
	String proyect_path;
	String proyect_name;
	
	public CodeScanner(Display display) {
		this.display = display;
		this.class_list = new SimpleList();
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
		return this.class_list;
	}
	
	private void printProjectInfo(IProject project) throws CoreException, BadLocationException {
		System.out.println("Working on project " + project.getName());;
		this.proyect_name = project.getName();
		this.proyect_path = project.getFullPath().toString() + "/src";
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
		ClassSqueme class_info = new ClassSqueme();
		class_info.setGrap_lList(visitor.getGraph_list());
		class_info.setMethod_list(visitor.getMethodList());
		class_info.setIs_main(visitor.getIs_main());
		class_info.path = unit.getPath();
		Node add_class = new Node(class_info);
		this.class_list.add_element(add_class);
		this.complexity = visitor.getComplexity();
		System.out.println("Has number of lines:" + doc.getNumberOfLines());
	}

	public String getProyect_path() {
		return this.proyect_path;
	}

	public String getProyect_name() {
		return this.proyect_name;
	}
	
	
}
