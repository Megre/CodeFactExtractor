package group.spart.kg.java.prop;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 9, 2020 10:54:57 PM 
*   
*/
public class InstantiatesClassHandler extends AbstractPropertyHandler {

	private ASTVisitor classInstanceCreationVisitor = new ASTVisitor() {
		@Override
		public boolean visit(ClassInstanceCreation node) {
			fNodeVisitor.addTriple(fNode, fPropertyUri, node.getType());
			return true;
		}
	};
	
	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IPropertyHandler#handle()
	 */
	@Override
	public void handle() {
		fNode.accept(classInstanceCreationVisitor);
	}

}
