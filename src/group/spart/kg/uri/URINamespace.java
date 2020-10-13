package group.spart.kg.uri;

import java.util.HashMap;
import java.util.Map;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Jan 8, 2020 12:12:00 PM 
*/
public abstract class URINamespace {
	
	protected static Map<String, String> fPrefixMap = new HashMap<>();
	private static String fPrefixes = null;
	private static Map<String, String> fExpendedUriMap = new HashMap<>();
	
	public static Map<String, String> getPrefixMap() {
		return fPrefixMap;
	}
	
	public static void addPrefixMap(Map<String, String> prefixMap) {
		for(Map.Entry<String, String> entry: prefixMap.entrySet()) {
			addPrefix(entry.getKey(), entry.getValue());
		}
		
	}
	
	/**
	 * Expends the URI in prefixed format, 
	 * 		e.g. java:hasMethod -> {uri of java}#hasMethod
	 * @param shortUri
	 * @return - expended URI, or <br>
	 * 		- the original input when the input contains "/" or doesn't contain ":"
	 */
	public static String expendedUri(String shortUri) {
		if(fExpendedUriMap.containsKey(shortUri)) return fExpendedUriMap.get(shortUri);
		if(shortUri.contains("/")) return shortUri;
		
		final int idx = shortUri.indexOf(':');
		if(idx == -1) return shortUri;
		
		final String uriPrefix = shortUri.substring(0, idx).toLowerCase();
		final String expendedUri =  uriOfPrefix(uriPrefix) + shortUri.substring(idx + 1);
		fExpendedUriMap.put(shortUri, expendedUri);
		return expendedUri;
	}
	
	/**
	 * Generates the prefix header of a SPARQL query.
	 * @return prefix header in multiple lines.
	 */
	public static String prefixHeader() {
		if(fPrefixes != null) {
			return fPrefixes;
		}
		
		StringBuffer sBuffer = new StringBuffer();
		for(Map.Entry<String, String> entry: fPrefixMap.entrySet()) {
			sBuffer.append("prefix " + entry.getKey() + ": <" + entry.getValue() + ">\r\n");
		}
		
		return fPrefixes = sBuffer.toString();
	}
	
	public static String uriOfPrefix(String prefixName) {
		return fPrefixMap.get(prefixName);
	}
	
	/**
	 * Adds a prefix uri to the prefix map.
	 * @param name prefix of the URI.
	 * @param uri URI corresponding to the name. If it doesn't end with '#', a '#' will be postfixed.
	 */
	protected static void addPrefix(String name, String uri) {
		fPrefixMap.put(name, uri.endsWith("#")?uri:uri+"#");
	}
	
}
