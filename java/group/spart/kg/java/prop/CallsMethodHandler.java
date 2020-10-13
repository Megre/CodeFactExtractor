package group.spart.kg.java.prop;

import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import group.spart.kg.NameFactory;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 8, 2020 6:08:38 PM 
*   
*/
public class CallsMethodHandler extends AbstractPropertyHandler {
	private static int fCount = 1;
	
	private class MethodIvocationVisitor extends ASTVisitor {
		@SuppressWarnings("unchecked")
		@Override
		public boolean visit(MethodInvocation node) {
			final Resource method = fNodeVisitor.retriveIndividual(fNode);
			fNodeVisitor.addTriple(method, fPropertyUri, node);
			
			final Resource methodInvocation = fNodeVisitor.createIndividual(String.format("jsbh:mi_%s_%d", NameFactory.getName(fNode), fCount++),
					"jsbh:MethodInvocation");
			fNodeVisitor.setType(methodInvocation, "sbh:Event");
			fNodeVisitor.addTriple(methodInvocation, "jsbh:methodCaller", fNode);
			fNodeVisitor.addTriple(methodInvocation, "jsbh:methodCalled", node);
			for(Expression expr: (List<Expression>) node.arguments()) {
				fNodeVisitor.addTriple(methodInvocation, "jsbh:hasArg", expr.toString());
			}
			
			return true;
		}
	};
	
	private MethodIvocationVisitor methodIvocationVisitor = new MethodIvocationVisitor();
	
	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IPropertyHandler#handle()
	 */
	@Override
	public void handle() {
		fNode.accept(methodIvocationVisitor);
	}

}
