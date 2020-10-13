package group.spart.kg.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 4, 2020 4:24:30 PM 
*/
public class ExtractionRequestor extends FileASTRequestor {

	private List<ASTVisitor> fVisitors = new ArrayList<>();
	
	@Override
	public void acceptAST(String sourceFilePath, CompilationUnit ast) {
		for(ASTVisitor visitor: fVisitors) {
			ast.accept(visitor);
		}
	}
	
	public void registerVisitor(ASTVisitor visitor) {
		fVisitors.add(visitor);
	}

}
