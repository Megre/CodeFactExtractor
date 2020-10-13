package group.spart.kg.java.visitor;


import org.apache.jena.rdf.model.Resource;
import org.eclipse.jdt.core.dom.ArrayType;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 5, 2020 11:57:01 PM 
*   
*/
public class ArrayTypeVisitor extends AbstractASTNodeVisitor {

	@Override
	public boolean visit(ArrayType node) {
		
		final Resource array = createIndividual(node, "Array");
		addTriple(array, "arrayOf", node.getElementType());
		addTriple(array, "dimensionIs", String.valueOf(node.getDimensions()));
		
		return true;
	}
}
