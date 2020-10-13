package group.spart.kg.test;

import org.apache.jena.ontology.OntModel;

import group.spart.error.Assert;
import group.spart.kg.OntologyModelNet;
import group.spart.kg.uri.DefaultNamespace;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Oct 8, 2020 11:06:03 AM
*/
public class TestOntologyModelNet {

	public static void main(String[] args) {
		baseTest();
	}
	
	static void baseTest() {
		final OntologyModelNet kn = new OntologyModelNet(Main.BASE_DIR + "\\ontology models");
		final OntModel ontModel = kn.loadOntModel();
		Assert.info(ontModel.getNsPrefixMap());
		
		DefaultNamespace.addPrefixMap(ontModel.getNsPrefixMap());
		Assert.info(DefaultNamespace.expendedUri("java:hasMethod"));
	}

}
