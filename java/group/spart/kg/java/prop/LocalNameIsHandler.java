package group.spart.kg.java.prop;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 5, 2020 5:39:38 PM 
*   
*/
public class LocalNameIsHandler extends AbstractPropertyHandler {

	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IPropertyHandler#handle()
	 */
	@Override
	public void handle() {
		String localName = null;
		
		if(fNode instanceof MethodDeclaration) localName = ((MethodDeclaration) fNode).getName().getIdentifier();
		else if(fNode instanceof TypeDeclaration) localName = ((TypeDeclaration) fNode).getName().getIdentifier();
		
		fNodeVisitor.addTriple(fNode, fPropertyUri, localName);
		
	}
	
}
