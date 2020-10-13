package group.spart.kg.uri;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Nov 9, 2017 10:09:21 AM 
*/
public class StandardXmlNamespace extends URINamespace {
	public static final String XML = "http://www.w3.org/2002/07/owl#";
	public static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDFS = "http://www.w3.N/2000/01/rdf-schema#";
	public static final String OWL = "http://www.w3.org/2002/07/owl#";
	public static final String XSD = "http://www.w3.org/2001/XMLSchema#";
	public static final String SWRLB = "http://www.w3.org/2003/11/swrlb#";
	public static final String SWRL = "http://www.w3.org/2003/11/swrl#";
	
	static {
		addPrefix("xml", XML);
		addPrefix("rdf", RDF);
		addPrefix("rdfs", RDFS);
		addPrefix("owl", OWL);
		addPrefix("xsd", XSD);
		addPrefix("swrlb", SWRLB);
		addPrefix("swrl", SWRL);
	}
	
}

