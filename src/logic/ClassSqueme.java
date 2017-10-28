package logic;

import org.eclipse.core.runtime.IPath;

/*
 * Clase encargada de almacenar la informacion obtenida de los arvhivos .java escaneados.
 * */

public class ClassSqueme {

	SimpleList grap_lList;
	/**
	 * Atributo encargado de almacenar la lista de statements declarados.
	 */
	SimpleList method_list;
	/**
	 * Encargado de almacenar la lista de metodos declarados en la clase
	 */
	Boolean is_main;
	/**
	 * Marca si la clase es la clase main del proyecto.
	 */
	IPath path;
	/**
	 * Guarda la ruta de almacenamiento en disco en donde se enuentra el archivo.
	 */
	String code;

	/**
	 * Guarda todo el codigo de la clase en un string.
	 */
	public ClassSqueme() {
		
		this.grap_lList = new SimpleList();
		this.method_list = new SimpleList();
		this.is_main = false;
		this.code = "";
	}
	
	public SimpleList getGrap_lList() {
		return grap_lList;
	}
	public void setGrap_lList(SimpleList grap_lList) {
		this.grap_lList = grap_lList;
	}
	
	public SimpleList getMethod_list() {
		return method_list;
	}
	public void setMethod_list(SimpleList method_list) {
		this.method_list = method_list;
	}
	public Boolean getIs_main() {
		return is_main;
	}
	public void setIs_main(Boolean is_main) {
		this.is_main = is_main;
	}
	
	public IPath getPath() {
		return path;
	}

	public void setPath(IPath path) {
		this.path = path;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
