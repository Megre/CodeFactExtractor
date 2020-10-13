package group.spart.kg.java.prop;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 6, 2020 10:54:57 PM 
*   
*/
public class ImplementsHandler extends AbstractPropertyHandler {

	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IPropertyHandler#handle()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handle() {
		if(fNode.getNodeType() != ASTNode.TYPE_DECLARATION) return;
		
		final TypeDeclaration td = (TypeDeclaration) fNode;
		for(Type type: (List<Type>) td.superInterfaceTypes()) {
			fNodeVisitor.addTriple(fNode, fPropertyUri, type);
		}
		
	}

}
