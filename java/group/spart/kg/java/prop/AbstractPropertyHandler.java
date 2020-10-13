package group.spart.kg.java.prop;

import org.eclipse.jdt.core.dom.ASTNode;

import group.spart.kg.java.visitor.AbstractASTNodeVisitor;

/** 
* The base of all property handlers.
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 6, 2020 8:48:08 AM 
*/
public abstract class AbstractPropertyHandler implements IPropertyHandler {
	
	/**
	 * The handled AST node.
	 */
	protected ASTNode fNode;
	
	/**
	 * The visitor of the AST node.
	 */
	protected AbstractASTNodeVisitor fNodeVisitor;
	
	/**
	 * The URI of the property.
	 */
	protected String fPropertyUri;

	public AbstractPropertyHandler initialize(ASTNode node, AbstractASTNodeVisitor nodeVisitor) {
		fNode = node;
		fNodeVisitor = nodeVisitor;
		return this;
	}
	
	public void setPropertyUri(String propertyUri) {
		fPropertyUri = propertyUri;
	}
	
	protected ASTNode ancestorNode(ASTNode node, Class<? extends ASTNode> ancestorNodeType) {
		ASTNode parent = node.getParent();
		while(parent != null) {
			if(parent.getClass() == ancestorNodeType) return parent;
			parent = parent.getParent();
		}
		return null;
	}
	
}
