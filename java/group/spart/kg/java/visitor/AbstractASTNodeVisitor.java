package group.spart.kg.java.visitor;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

import group.spart.kg.java.NameFactory;
import group.spart.kg.java.layout.JavaVisitorLayout;
import group.spart.kg.java.prop.AbstractPropertyHandler;
import group.spart.kg.uri.StandardXmlNamespace;
import group.spart.kg.uri.DefaultNamespace;

/** 
 * The base of all AST node visitors.
 * 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 5, 2020 4:09:43 PM 
*/
public abstract class AbstractASTNodeVisitor extends ASTVisitor {
	private Model fModel;
	private String fPrefixUri;
	protected JavaVisitorLayout<AbstractASTNodeVisitor> fVisitorLayout;
	
	public AbstractASTNodeVisitor initialize(Model model, JavaVisitorLayout<AbstractASTNodeVisitor> visitorLayout) {
		fModel = model;
		fVisitorLayout = visitorLayout;
		fPrefixUri = DefaultNamespace.uriOfPrefix(visitorLayout.getVisitorItem(this.getClass()).getNamespace());
		return this;
	}
	
	protected Model getModel() {
		return fModel;
	}
	
	public Resource createIndividual(String individualUri, String ontClassUri) {
		final Resource individual = retriveIndividual(individualUri);
		setType(individual, ontClassUri);
		return individual;
	}
	
	public Resource createIndividual(ASTNode node, String ontClassUri) {
		final Resource individual = retriveIndividual(node);
		setType(individual, ontClassUri);
		return individual;
	}
	
	public Resource retriveIndividual(String individualUri) {
		return fModel.createResource(expendedUri(individualUri));
	}
	
	public Resource retriveIndividual(ASTNode node) {
		return retriveIndividual(NameFactory.getName(node));
	}
	
	public Property getProperty(String propertyUri) {
		return fModel.getProperty(expendedUri(propertyUri));
	}
	
	public void addTriple(Resource subject, String propertyUri, ASTNode objectNode) {
		if(subject == null || objectNode == null) return;
		
		fModel.add(subject, 
				getProperty(propertyUri), 
				retriveIndividual(objectNode));
	}
	
	public void addTriple(ASTNode subjectNode, String propertyUri, ASTNode objectNode) {
		if(subjectNode == null || objectNode == null) return;
		
		addTriple(retriveIndividual(subjectNode), 
				propertyUri, 
				objectNode);
	}
	
	
	public void addTriple(Resource subject, String propertyUri, String literal) {
		if(subject == null || literal == null) return;
		
		fModel.add(subject, 
				getProperty(propertyUri), 
				fModel.createLiteral(literal));
	}
	
	public void addTriple(ASTNode subjectNode, String propertyUri, String literal) {
		addTriple(retriveIndividual(subjectNode), 
				propertyUri, 
				literal);
	}
	
	public void addTriple(Resource subject, String propertyUri, boolean literal) {
		if(subject == null) return;
		
		fModel.add(subject, 
				getProperty(propertyUri), 
				fModel.createTypedLiteral(literal));
	}
	
	public void addTriple(ASTNode subjectNode, String propertyUri, boolean literal) {
		if(subjectNode == null) return;
		
		addTriple(retriveIndividual(subjectNode), 
				propertyUri, 
				literal);
	}
	
	public void setType(Resource individual, String ontClassUri) {
		fModel.add(individual, 
				fModel.getProperty(StandardXmlNamespace.RDF + "type"), 
				fModel.getResource(expendedUri(ontClassUri)));
	}
	
	protected void processChildVisitors(ASTNode node) {
		List<AbstractASTNodeVisitor> childList = fVisitorLayout.childVisitors(getClass());
		for(AbstractASTNodeVisitor visitor: childList) {
			node.accept(visitor.initialize(fModel, fVisitorLayout));
		}
	}
	
	protected void handleProperties(ASTNode node) {
		for(AbstractPropertyHandler visitor: (List<AbstractPropertyHandler>) fVisitorLayout.propertyHandlers(node)) {
			visitor.initialize(node, this).handle();
		}
	}
	
	private String expendedUri(String uri) {
		return uri.contains(":")?DefaultNamespace.expendedUri(uri):(fPrefixUri+uri);
	}
	
}
