package group.spart.kg.java.prop;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 5, 2020 6:12:19 PM 
*   
*/
public class HasMethodHandler extends AbstractPropertyHandler {
	
	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IHandler#handle()
	 */
	@Override
	public void handle() {
		fNodeVisitor.addTriple(fNode.getParent(), fPropertyUri, fNode);	
	}
	
}
