package group.spart.kg.java.prop;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 6, 2020 10:34:47 PM 
*   
*/
public class IsInLocalPackageHandler extends AbstractPropertyHandler {

	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IPropertyHandler#handle()
	 */
	@Override
	public void handle() {
		fNodeVisitor.addTriple(fNode, fPropertyUri, true);
	}

}
