package group.spart.kg.layout;

/** 
* Represents the configuration item starts with line "[property]".
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Aug 15, 2020 6:06:28 PM 
*   
*/
public class PropertyItem extends ConfigItem {
	
	public PropertyItem() {
		
	}

	@Override
	public String toString() {
		return "[property] " + super.toString();
	}
}
