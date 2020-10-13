package group.spart.kg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import group.spart.error.Assert;
import group.spart.kg.uri.StandardXmlNamespace;

import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;

/**
* A ".owl" file attached with several ".qrules" files are called an ontology model. 
* An ontology model may depends on one or more other ontology models, as declared in 
* "owl:imports" in ".owl" files. All related ontology models compose a net of ontology models.
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Nov 8, 2017 10:37:47 PM 
 */
public class OntologyModelNet {
	private String fOntModelPath;
	
	private OntModelSpec ONT_MODEL_SPEC = OntModelSpec.OWL_MEM;
	private Map<String, OWLFileModel> fUriOntModelMap;
	private Map<String, String> fFileNameUriMap;
	private List<File> fAttachedRules;
	
	public OntologyModelNet(String ontModelPath) {
		fOntModelPath = ontModelPath;
		fUriOntModelMap = new HashMap<String, OWLFileModel>();
		fFileNameUriMap = new HashMap<String, String>();
		fAttachedRules = new ArrayList<File>();
	}
	
	public void setOntPath(String ontPath) {
		this.fOntModelPath = ontPath;
	}
	
	/**
	 * Retrieves the owl file model with a given uri.
	 * @param uri uri of the owl file model.
	 * @return owl file model.
	 */
	public OWLFileModel getFileModel(String uri) {
		return fUriOntModelMap.get(uri);
	}
	
	/**
	 * Retrieves the owl file model with a given file name.
	 * @param fileName file name of the owl file model.
	 * @return owl file model.
	 */
	public OWLFileModel getFileModel(File file) {
		return fUriOntModelMap.get(fFileNameUriMap.get(file.getName()));
	}
	
	/**
	 * Retrieves the uri of a owl file.
	 * @param file a owl file.
	 * @return uri of the owl file.
	 */
	public String getUri(File file) {
		return fFileNameUriMap.get(file.getName());
	}
	
	/**
	 * Retrieves rule files (.qrules file) attached to owl file models in dependence order of owl file models.
	 * The list of attaches rules isn't available until {@link #loadOntModel()} is invoked.
	 * @return a list of attaches rules (.qrules file).
	 */
	public List<File> getAllAttachedRuleFiles() {
		return fAttachedRules;
	}
	
	/**
	 * load all owl model in ontModelPath
	 * @return
	 */
	public OntModel loadOntModel() {
		return loadOntModel(new File(fOntModelPath).listFiles());
	}
	
	private OntModel loadOntModel(File[] files) {
		buildFileModels();
		fAttachedRules.clear();
		
		// the result model
		OntModel model = ModelFactory.createOntologyModel(ONT_MODEL_SPEC);
		
		// build dependence stack
		Stack<String> stack = buildDependenceStack(files);
		
		// load the models in stack
		Assert.info("Load:");
		Set<String> loadedUriSet = new HashSet<String>(); 
		OntDocumentManager manager = ONT_MODEL_SPEC.getDocumentManager();
		
		while(stack.size() > 0) {
			String uri = stack.pop(); 
			if(loadedUriSet.contains(uri)) { // the uri may repeat in the dependence stack
				continue;
			}
			Assert.info(uri);
			OWLFileModel fileModel = getFileModel(uri);
			model.add(fileModel.getOntModel());
			
			// set namespace prefixes
			model.setNsPrefixes(fileModel.getOntModel().getNsPrefixMap());

			// ignore uri and add to document manager
			manager.addIgnoreImport(uri);
			manager.addModel(uri, fileModel.getOntModel());
			
			loadedUriSet.add(uri);
			
			// add attached rule files
			fAttachedRules.addAll(fileModel.getAttachedRuleFileList());
		}
		
		// delete imports
		Property predicate = model.getProperty(StandardXmlNamespace.OWL, "imports");
		StmtIterator stmtIterator = model.listStatements(null, predicate, (RDFNode)null);
		if(stmtIterator != null) {
			List<Statement> ls = new ArrayList<Statement>();
			while (stmtIterator.hasNext()) {
				ls.add(stmtIterator.nextStatement());
			}
			model.remove(ls);
		}
			
		return model;
	}
	
	/**
	 * Builds the dependence stack.
	 * @param files owl files.
	 * @return a stack containing uris of all related owl files.
	 * The uris near the bottom of the stack depends on the uris near the top of the stack.
	 * The uris in the result stack may repeatedly occur.
	 */
	private Stack<String> buildDependenceStack(File[] files) {
		Stack<String> stack = new Stack<String>();
		class CheckSet extends HashSet<String> { // check circular dependency
			private static final long serialVersionUID = 1L;

			public boolean contains(String leftUri, String rightUri) {
				return contains(join(leftUri, rightUri));
			}
			
			public void put(String leftUri, String rightUri) {
				add(join(leftUri, rightUri));
			}
			
			private String join(String leftUri, String rightUri) {
				return leftUri + ", " + rightUri;
			}
		}; 
		CheckSet check = new CheckSet();
		
		Assert.info("Dependence:");		
		for(File file : files) {
			if(!file.getName().toLowerCase().endsWith(OWLFileModel.ONT_MODEL_EXT)) {
				continue;
			}
			stack.push(getUri(file));
		}
		
		int idx = 0;
		while(idx < stack.size()) {
			final String leftUri = stack.get(idx);
			OWLFileModel fileModel = getFileModel(leftUri);
			Set<String> imports = fileModel.listDirectImports();
			Iterator<String> iterator = imports.iterator();
			while(iterator.hasNext()) {
				String rightUri = iterator.next();
				if(check.contains(rightUri, leftUri) || leftUri.equals(rightUri)) {
					Assert.warn(!check.contains(leftUri, rightUri), "circular dependency: " + leftUri + " <-> " + rightUri);
					check.put(leftUri, rightUri);
					continue;
				}
				
				stack.push(rightUri);
				
				Assert.info(!check.contains(leftUri, rightUri), leftUri + " -> " + rightUri); // print if not recorded in the check map yet
				check.put(leftUri, rightUri);
			}
			++idx;
		}
		
		return stack;
	}
	
	/**
	 * Builds the file model of all owl files under {@link #fOntModelPath}. 
	 * Imports (owl:import) are not processed.
	 * @return a set of OWLFileModel in the net.
	 */
	private Set<OWLFileModel> buildFileModels() {
		Set<OWLFileModel> fileModelSet = new HashSet<OWLFileModel>();
		File ontModelPath = new File(fOntModelPath);
		File[] files = ontModelPath.listFiles();
		for(File file: files) { 
			if(!file.isFile() || !file.getName().toLowerCase().endsWith(OWLFileModel.ONT_MODEL_EXT)) {
				continue;
			}
			
			fileModelSet.add(buildFileModel(file));
		}
		return fileModelSet;
	}
	
	/**
	 * Builds the owl file model of a given file.
	 * @param file an owl file.
	 * @return built file model.
	 */
	private OWLFileModel buildFileModel(File file) {
		final OWLFileModel fileModel = new OWLFileModel(ONT_MODEL_SPEC, file);
		if(fUriOntModelMap.containsKey(fileModel.getBaseUri())) {
			Assert.warn("Different files have the same URI: " + fUriOntModelMap.get(fileModel.getBaseUri()).getName() + ", " + file.getName());
		}
		fUriOntModelMap.put(fileModel.getBaseUri(), fileModel);
		fFileNameUriMap.put(file.getName(), fileModel.getBaseUri());
		return fileModel;
	}
}
