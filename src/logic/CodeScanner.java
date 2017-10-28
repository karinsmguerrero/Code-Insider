/**
 * 
 */
package logic;

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
	/**
	 * Atributo que guardara el display en donde generara la imagen para swt
	 */
	Display display;
	/**
	 * lista de todas las clases que encontro en el proyecto escaneado.
	 */
	SimpleList class_list;
	/**
	 * String que contiene la complejidad algoritmica
	 */
	String complexity;
	/**
	 * Ruta de acceso en donde se encuentra el proyeto en disco
	 */
	String proyect_path;
	/**
	 * Nombre del proyecto
	 */
	String proyect_name;
	
	public CodeScanner(Display display) {
		this.display = display;
		this.class_list = new SimpleList();
		this.complexity = "";
	}
	
	/**
	 * Metodo encargado de realizar los llamados a los metodos privados para realizar el escaneo del proyecto.
	 * @return retorna la lista de todas las clases escaneadas.
	 */
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
	
	/**
	 * Encargadaa de mostrar en pantalla todos los detalles del proyecto escaneado.
	 * @param project Variable que contiene el proyecto a escanear.
	 */
	private void printProjectInfo(IProject project) throws CoreException, BadLocationException {
		System.out.println("Working on project " + project.getName());;
		this.proyect_name = project.getName();
		this.proyect_path = project.getFullPath().toString() + "/src";
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			printPackageInfos(javaProject);
		}
	}
	
	/**
	 * Encargada de mostrar toda la informacion del paquete que se esta escaneando, y de llamar al metodo para el escaneo de todos los archivos .java
	 * @param javaProject Proyecto de tipo "java proyect que se desea escanear."
	 */
	private void printPackageInfos(IJavaProject javaProject) throws JavaModelException, BadLocationException {
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				System.out.println("Package " + mypackage.getElementName());
				printICompilationUnitInfo(mypackage);
			}
		}
	}
	
	/**
	 * Encargada de realizar el escaneo uno por uno de los archivos .java encontrados.
	 * @param mypackage packete encontrado dentro del proyecto, que se va a escanear para encontrar todos sus archivos .java
	 */
	private void printICompilationUnitInfo(IPackageFragment mypackage) throws JavaModelException, BadLocationException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			printCompilationUnitDetails(unit);
		}
	}
	
	
	/**
	 * Metodo encargado de parsear el codigo de un archivo .java y realizar el esuqema del codigo de dicha clase seleccionada.
	 * @param unit unidad de compilacion (archivo .java) a escanear.
	 */
	@SuppressWarnings("deprecation")
	private void printCompilationUnitDetails(ICompilationUnit unit) throws JavaModelException, BadLocationException {
		System.out.println("Source file " + unit.getElementName());
		Document doc = new Document(unit.getSource());
		String line = doc.get();
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(line.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		CodeVisitor visitor = new CodeVisitor(cu, this.display);
		cu.accept(visitor.getVisitor());
		ClassSqueme class_info = new ClassSqueme();
		class_info.setGrap_lList(visitor.getGraph_list());
		class_info.setMethod_list(visitor.getMethodList());
		class_info.setIs_main(visitor.getIs_main());
		class_info.path = unit.getPath();
		class_info.setCode(line);
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
