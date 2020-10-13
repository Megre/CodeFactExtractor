package group.spart.kg.java.prop;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 7, 2020 7:19:37 AM 
*   
*/
public class EmptyHandler extends AbstractPropertyHandler {

	private static AbstractPropertyHandler fInstance = new EmptyHandler();
	
	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IPropertyHandler#handle()
	 */
	@Override
	public void handle() {
		
	}
	
	public static AbstractPropertyHandler instance() {
		return fInstance;
	}

}
