package group.spart.kg.layout;

/** 
* Represents the configuration item starts with line "[package]".
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Aug 15, 2020 5:59:41 PM 
*   
*/
public class PackageItem extends ConfigItem {

	public PackageItem() {
		super();
	}
	
	public String getVisitorPackage() {
		return getValue("visitor");
	}
	
	public String getHandlerPackage() {
		return getValue("handler");
	}
	
	@Override
	public String toString() {
		return "[package] " + super.toString();
	}
	
}
