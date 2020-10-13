package group.spart.kg.java.prop;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 7, 2020 11:11:29 PM 
*   
*/
public class InPackageHandler extends AbstractPropertyHandler {
	
	private ASTVisitor packageDeclarationVisitor = new ASTVisitor() {
		@Override
		public boolean visit(PackageDeclaration node) {
			fNodeVisitor.addTriple(fNode, fPropertyUri, node);
			return false;
		}
	};
	
	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IPropertyHandler#handle()
	 */
	@Override
	public void handle() {
		if(fNode.getNodeType() != ASTNode.TYPE_DECLARATION) return;
		
		final TypeDeclaration td = (TypeDeclaration) fNode;
		if(!td.isPackageMemberTypeDeclaration()) return;
		
		fNode.getParent().accept(packageDeclarationVisitor);
	}

}
