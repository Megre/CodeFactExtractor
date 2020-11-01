package group.spart.kg.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import group.spart.error.Assert;
import group.spart.kg.java.layout.JavaVisitorLayout;
import group.spart.kg.java.visitor.AbstractASTNodeVisitor;
import group.spart.kg.layout.InstancePool;
import group.spart.kg.uri.DefaultNamespace;

/** 
* Creates ASTs and accepts ASTNode visitors.
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 5, 2020 4:37:58 PM 
*/
public class FactExtractor {

	private ExtractionRequestor fRequestor;
	private JavaProject fProject;
	private Model fModel;
	private JavaVisitorLayout<AbstractASTNodeVisitor> fVisitorLayout;
	
	public FactExtractor(JavaProject project, Model model, JavaVisitorLayout<AbstractASTNodeVisitor> visitorLayout) {
		fProject = project;
		fModel = model;
		fVisitorLayout = visitorLayout;
		fRequestor = new ExtractionRequestor();
		
		DefaultNamespace.addPrefixMap(model.getNsPrefixMap());
	}
	
	public void registerVisitor(Class<? extends AbstractASTNodeVisitor> visitorClass) {
		fRequestor.registerVisitor(InstancePool.instance(visitorClass).initialize(fModel, fVisitorLayout));
	}
	
	public void extract() {
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setEnvironment(fProject.getClassPaths(), fProject.getSourcePaths(), null, true);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.createASTs(fProject.listSourceFiles(), null, new String[] {}, fRequestor, null);
	}
	
	public Model infer(List<File> qrules) {
		Dataset ds = DatasetFactory.create() ;
		ds.setDefaultModel(fModel);
		ds.supportsTransactionAbort();
		ds.begin(ReadWrite.WRITE);
		try {
			for(File qfile: qrules) {
				Assert.info("Inferring: " + qfile.getName());
				UpdateRequest ur = UpdateFactory.read(new FileInputStream(qfile));
				UpdateProcessor proc = UpdateExecutionFactory.create(ur, ds);
			    proc.execute();
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Model resultModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, ds.getDefaultModel());
		ds.abort();
	    ds.close(); 
	    return resultModel;
	}
	
	public Model getModel() {
		return fModel;
	}
}
