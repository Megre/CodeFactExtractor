package group.spart.kg.layout;

/** 
* Represents the configuration item starts with line "[visitor]".
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Aug 15, 2020 6:01:31 PM 
*/
public class VisitorItem extends ConfigItem {

	public VisitorItem() {
		
	}
	
	@Override
	public String toString() {
		return "[visitor] " + super.toString();
	}
}
