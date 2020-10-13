package group.spart.kg.uri;

/**
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Oct 11, 2020 9:50:54 PM
 */
public class DefaultNamespace extends URINamespace {
	public static String MERGED = "http://www.spart.group/megre/ontologies/2017/12/Merged";
	public static String BASE = MERGED;
	
	static {
		addPrefix("mgd", MERGED);
		addPrefix("base", BASE);
		
		addPrefixMap(StandardXmlNamespace.getPrefixMap());
	}
	
}

