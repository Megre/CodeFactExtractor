package group.spart.kg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import group.spart.error.Assert;
import group.spart.kg.util.ModelUtil;

/** 
* Represents an ontology model. 
* 
* @see group.spart.kg.OntologyModelNet
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Nov 8, 2017 10:52:17 PM 
*/
public class OWLFileModel {
	public static final String ONT_MODEL_EXT = ".owl";
	public static final String ATTACHED_RULE_EXT = ".qrules";
	
	private File fOwlFile;
	private List<File> fAttachedRules;
	private String fBaseUri;
	private OntModel fOntModel;
	
	public OWLFileModel(String owlFile) {
		this(OntModelSpec.OWL_MEM, new File(owlFile));
	}
	
	public OWLFileModel(OntModelSpec spec, String owlFile) {
		this(spec, new File(owlFile));
	}
	
	public OWLFileModel(OntModelSpec spec, File owlFile) {
		Assert.ensureArgument(owlFile.exists() && owlFile.isFile(), 
				"No such file: " + owlFile.getAbsolutePath());
		
		this.fOwlFile = owlFile;
		fOntModel = ModelUtil.createOntModel(spec, owlFile);
		
		fBaseUri =  ModelUtil.getBaseUri(fOntModel);
	}
	
	public File getFile() {
		return fOwlFile;
	}
	
	public void saveAs(String filePath) {
		ModelUtil.save(fOntModel, new File(filePath), true);
	}
	
	public String getBaseUri() {
		return fBaseUri;
	}
	
	/**
	 * Retrieves all imports including the indirect ones. 
	 * The method's behavior depends on {@link org.apache.jena.ontology.OntDocumentManager}.
	 * @return
	 */
	public Set<String> getHierarchicalImports() {
		return fOntModel.listImportedOntologyURIs();
	}
	
	public String getName() {
		return fOwlFile.getName();
	}
	
	/**
	 * Retrieves rule files (.qrules) with the same name of current file model.
	 * @return a list of founded files. 
	 * (Currently only one rule file is attached to each owl file. Multiple rule files may be supported in the future.)
	 */
	public List<File> getAttachedRuleFileList() {
		if(fAttachedRules != null) return fAttachedRules;
		
		fAttachedRules = new ArrayList<File>();
		String owlPath = fOwlFile.getAbsolutePath();
		String owlPathWithoutExt = owlPath.substring(0, owlPath.lastIndexOf('.'));
		File file = new File(owlPathWithoutExt + OWLFileModel.ATTACHED_RULE_EXT); 
		if(file.exists() && file.isFile()) {
			fAttachedRules.add(file);
			Assert.info("Attached rule: " + fOwlFile.getName() + " <- " + file.getName());
		}
		return fAttachedRules;
	}
	
	public OntModel getOntModel() {
		return fOntModel;
	}
	
	public Set<String> listDirectImports() {
		return ModelUtil.listDirectImports(fOntModel);
	}
		
}
