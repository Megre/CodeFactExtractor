package group.spart.kg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;

import group.spart.error.Assert;
import group.spart.kg.OntologyEnvironment;
import group.spart.kg.uri.StandardXmlNamespace;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Aug 26, 2020 7:35:17 AM 
*   
*/
public class ModelUtil {
	
	/**
	 * Retrieves base uri (xml:base).
	 * @param RDF model.
	 * @return the base uri of the model.
	 */
	public static String getBaseUri(Model model) {
		final String uri = model.getNsPrefixURI("");
		Assert.ensure(uri != null, "The \"base\" prefix wasn't set!");
		
		return fixUri(uri);
	}
	
	public static void save(Model model, String owlFilePath) {
		save(model, new File(owlFilePath));
	}
	
	public static void save(Model model, File owlFile) {
		save(model, owlFile, false);
	}
	
	public static void save(Model model, File owlFile, boolean processBadURI) {
		final Dataset dataset = DatasetFactory.create();
		dataset.supportsTransactionAbort();
		dataset.setDefaultModel(model);
		
		if(processBadURI) {
			File qrule = new File(OntologyEnvironment.ONTOLOGY_PATH + "/component/output_preprocessing.qrules");
			Assert.info("Preprocess URIs: " + qrule.getAbsolutePath());
			UpdateRequest ur;
			try {
				ur = UpdateFactory.read(new FileInputStream(qrule));
				UpdateProcessor proc = UpdateExecutionFactory.create(ur, dataset);
			    proc.execute();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	    
	    // save to OWL file
	    Assert.info("Save to owl file: " + owlFile.getAbsolutePath());
	    Model outModel = dataset.getDefaultModel();
	    RDFWriter w = outModel.getWriter("RDF/XML");
		w.setProperty("relativeURIs", "");
		w.setProperty("showDoctypeDeclaration", "true");
		w.setProperty("showXMLDeclaration", "true");
		w.setProperty("tab", "2");
		w.setProperty("allowBadURIs", !processBadURI);
		OutputStream os = null;
		try {
			os = new FileOutputStream(owlFile);
			w.write(outModel, os, getBaseUri(outModel));
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		dataset.abort();
		dataset.close();
	}
	
	
	/**
	 * list direct imports (owl:imports)
	 * @param model RDF model.
	 * @return a set of imported owl files' uris. (An owl file is imported by specifying its uri.)
	 */
	public static Set<String> listDirectImports(Model model) {
		Set<String> imports = new HashSet<String>();
		String base = getBaseUri(model);
		Property predicate = model.getProperty(StandardXmlNamespace.OWL, "imports");
		StmtIterator i = model.listStatements(model.getResource(base), predicate, (RDFNode)null);
		while (i.hasNext()) {
			Statement s = i.nextStatement();
			imports.add(s.getObject().toString());
		}
		return imports;
	}
	
	public static OntModel createOntModel(OntModelSpec spec, File owlFile) {
		final OntModel ontModel = ModelFactory.createOntologyModel(spec); 
		final InputStream in = FileManager.get().open(owlFile.getAbsolutePath());
		ontModel.getDocumentManager().setProcessImports(false); 
		if(in != null) {
			try {
				ontModel.read(in, null);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ontModel;
	}
	
	private static String fixUri(String uri) {
		return uri.replaceFirst("#$", "");
	}
}
