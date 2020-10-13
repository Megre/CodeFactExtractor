package group.spart.kg.java.visitor;

import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 4, 2020 10:04:10 PM 
*   
*/
public class EntryVisitor extends AbstractASTNodeVisitor {
	
	@Override
	public boolean visit(TypeDeclaration node) {

		if(node.isInterface()) {
			createIndividual(node, "Interface");
		}
		else {
			createIndividual(node, "Class");
		}
		
		handleProperties(node);
		processChildVisitors(node);
		
		return true;
	}
	
	@Override
	public boolean visit(EnumDeclaration node) {
		
		createIndividual(node, "Enum");
		
		handleProperties(node);
		
		return true;
	}
	
	@Override
	public boolean visit(PackageDeclaration node) {
		createIndividual(node, "Package");
		createIndividual(node, "LocalPackage");
		return true;
	}
}
