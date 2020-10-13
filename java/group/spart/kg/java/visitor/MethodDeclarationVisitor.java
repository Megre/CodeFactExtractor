package group.spart.kg.java.visitor;

import org.apache.jena.rdf.model.Resource;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 5, 2020 5:27:04 PM 
*   
*/
public class MethodDeclarationVisitor extends AbstractASTNodeVisitor {

	@Override
	public boolean visit(MethodDeclaration node) {
		final Resource method = createIndividual(node, "Method");
		
		if(node.isConstructor()) addTriple(method, "isConstructor", true);
		
		addTriple(method, "returnTypeIs", node.getReturnType2());
		
		handleProperties(node);
		processChildVisitors(node);
		
		return true;
	}
}
