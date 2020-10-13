package group.spart.kg.java.visitor;

import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 6, 2020 3:34:03 PM 
*   
*/
public class FieldDeclarationVisitor extends AbstractASTNodeVisitor {

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(FieldDeclaration node) {
		
		for(VariableDeclarationFragment frag: (List<VariableDeclarationFragment>) node.fragments()) {
			final Resource field = createIndividual(frag, "Field");
			addTriple(field, "fieldTypeIs", node.getType());
			
			final int parentType = node.getParent().getNodeType();
			if(parentType == ASTNode.TYPE_DECLARATION || parentType == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
				addTriple(node.getParent(), "hasField", frag);
				addTriple(node.getParent(), "hasFieldType", node.getType());
			}
		}
		
		return true;
	}
}
