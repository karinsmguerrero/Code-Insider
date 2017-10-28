package pruebita.logic;

import org.eclipse.core.runtime.IPath;

public class ClassSqueme {
	SimpleList grap_lList;
	SimpleList method_list;
	Boolean is_main;
	IPath path;

	public ClassSqueme() {
		this.grap_lList = new SimpleList();
		this.method_list = new SimpleList();
		this.is_main = false;
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

}
