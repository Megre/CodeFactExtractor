package group.spart.kg.java.prop;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 5, 2020 3:24:16 PM 
*   
*/
public class HasModifierHandler extends AbstractPropertyHandler {

	/* (non-Javadoc)
	 * @see group.spart.kg.prop.IPropertyHandler#handle()
	 */
	@Override
	public void handle() {
		List<?> modifiers = null;
		
		if(fNode instanceof MethodDeclaration) {
			modifiers = ((MethodDeclaration) fNode).modifiers();
		}
		else if(fNode instanceof TypeDeclaration) {
			modifiers = ((TypeDeclaration) fNode).modifiers();
		}
		
		addModifiers(modifiers);
	}

	private void addModifiers(List<?> modifiers) {
		for(Object obj: modifiers) {
			Modifier modifier = (Modifier) obj;
			fNodeVisitor.addTriple(fNode, fPropertyUri, modifier.getKeyword().toString());
		}
	}
}
