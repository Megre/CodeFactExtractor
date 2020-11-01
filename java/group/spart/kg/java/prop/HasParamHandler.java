package group.spart.kg.java.prop;

import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import group.spart.kg.java.NameFactory;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 9, 2020 10:58:16 PM 
*   
*/
public class HasParamHandler extends AbstractPropertyHandler {

	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IPropertyHandler#handle()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handle() {
		if(fNode.getNodeType() != ASTNode.METHOD_DECLARATION) return;
		
		final Resource method = fNodeVisitor.retriveIndividual(fNode);
		final StringBuffer sBuffer = new StringBuffer();
		final List<SingleVariableDeclaration> params = (List<SingleVariableDeclaration>) ((MethodDeclaration) fNode).parameters();
		for(int i=0; i<params.size(); ++i) {
			SingleVariableDeclaration varDeclaration = params.get(i);
			fNodeVisitor.createIndividual(varDeclaration, "Param");
			fNodeVisitor.addTriple(method, "hasParam", varDeclaration);
			fNodeVisitor.addTriple(varDeclaration, "paramTypeIs", varDeclaration.getType());
			fNodeVisitor.addTriple(varDeclaration, "localNameIs", varDeclaration.getName().getIdentifier());
			fNodeVisitor.addTriple(varDeclaration, "paramPos", String.valueOf(i));
			
			sBuffer.append(NameFactory.getName(varDeclaration.getType()));
			if(i < params.size() - 1) sBuffer.append(", ");
		}
		fNodeVisitor.addTriple(method, "methodSig", sBuffer.toString());
		
	}

}
