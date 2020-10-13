package group.spart.kg.java.visitor;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 7, 2020 7:37:23 AM 
*   
*/
public class AnonymousClassDeclarationVisitor extends AbstractASTNodeVisitor {
	
	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		createIndividual(node, "Class");
		createIndividual(node, "AnonymousClass");
		
		return true;
	}
	
	@Override
	public boolean visit(EnumDeclaration node) {
		createIndividual(node, "Enum");
		
		return true;
	}
}
