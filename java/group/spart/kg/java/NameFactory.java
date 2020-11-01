package group.spart.kg.java;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/** 
* Creates unique names for AST nodes and bindings.
* Visitors and property handlers need a unique name to create or retrieve an individual. 
* An individual corresponding to an AST node should have a unique name, because the individual 
* may be created in different visitors or property handlers. Creating an individual with 
* the same name of an existing individual will reuse the existing individual. Thus, a visitor 
* or property handler doesn't need to check whether an individual already exists. Jena ensures 
* existing resource will be reused.
* 
* @see org.apache.jena.rdf.model.Model#createResource
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 5, 2020 6:07:06 PM 
*/
public class NameFactory {
	public static String getName(ASTNode node) {
		switch(node.getNodeType()) {
			case ASTNode.METHOD_DECLARATION: 
				return getName(((MethodDeclaration) node).resolveBinding());
			case ASTNode.ENUM_DECLARATION: 
			case ASTNode.ANNOTATION_TYPE_DECLARATION:
			case ASTNode.TYPE_DECLARATION: 
				return getName(((AbstractTypeDeclaration) node).resolveBinding());
			case ASTNode.ANONYMOUS_CLASS_DECLARATION: 
				return getName(((AnonymousClassDeclaration) node).resolveBinding());
			case ASTNode.ARRAY_TYPE: 
			case ASTNode.PRIMITIVE_TYPE:
			case ASTNode.SIMPLE_TYPE:
			case ASTNode.QUALIFIED_TYPE:
			case ASTNode.INTERSECTION_TYPE:
			case ASTNode.NAME_QUALIFIED_TYPE:
			case ASTNode.PARAMETERIZED_TYPE:
			case ASTNode.UNION_TYPE:
			case ASTNode.WILDCARD_TYPE:
				return getName(((Type) node).resolveBinding());
			case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
				return getName((VariableDeclarationFragment) node);
			case ASTNode.SINGLE_VARIABLE_DECLARATION:
				return getName(((SingleVariableDeclaration) node).resolveBinding());
			case ASTNode.PACKAGE_DECLARATION:
				return getName(((PackageDeclaration) node).resolveBinding());
			case ASTNode.METHOD_INVOCATION:
				return getName(((MethodInvocation) node).resolveMethodBinding());
			default: 
				return unsupportedType(node);
		}
	}
	
	public static String getName(IBinding binding) {
		switch (binding.getKind()) {
			case IBinding.PACKAGE:
				return getName((IPackageBinding) binding);
			case IBinding.TYPE:
				return getName((ITypeBinding) binding);
			case IBinding.VARIABLE:
				return getName((IVariableBinding) binding);
			case IBinding.METHOD:
				return getName((IMethodBinding) binding);
			case IBinding.ANNOTATION:
			case IBinding.MEMBER_VALUE_PAIR:
			default:
				return unsupportedType(binding);
		}
	}
	
	private static String getName(IPackageBinding binding) {
		return binding.getName();
	}
	
	private static String getName(ITypeBinding binding) {
		String name = binding.getQualifiedName();
		if(name.isEmpty()) name = binding.getBinaryName();
		if(name == null) name = unknownType(binding);
		if(binding.isArray()) name = name.replace("[]", "-A" + binding.getDimensions());
		return name;
	}
	
	private static String getName(IMethodBinding binding) {
		final StringBuffer stringBuffer = new StringBuffer(binding.getName());
		stringBuffer.append("_`");
		final ITypeBinding[] typeBindings = binding.getParameterTypes();
		for(int i=0; i<typeBindings.length; ++i) {
			stringBuffer.append(getName(typeBindings[i]));
			if(i < typeBindings.length-1) stringBuffer.append("+");
		}
		stringBuffer.append("`_").append(getName(binding.getDeclaringClass()));
		return stringBuffer.toString();
	}
	
	private static String unsupportedType(Object object) {
		return String.format("unsupported_type_%s", object.toString());
	}
	
	private static String unknownType(Object object) {
		return String.format("unknown_type_%s", object.toString());
	}
	
	private static String getName(IVariableBinding binding) {
		if(binding.isField()) {
			return binding.getName() + "_" + getName(binding.getDeclaringClass());
		}
		else if(binding.isEnumConstant()) {
			return binding.getName() + "_" + getName(binding.getType());
		}
		else { // parameter
			return binding.getName() + "_" + getName(binding.getDeclaringMethod());
		}
	}
	
	private static String getName(VariableDeclarationFragment node) {
		final ASTNode parentNode = node.getParent();
		switch(parentNode.getNodeType()) {
			case ASTNode.FIELD_DECLARATION:
				return node.getName().getIdentifier() + "_" + getName(parentNode.getParent());
			case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
			case ASTNode.FOR_STATEMENT:
			case ASTNode.LAMBDA_EXPRESSION:
			default:
				return unsupportedType(node);
		}
	}
	
}
