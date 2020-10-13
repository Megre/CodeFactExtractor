package group.spart.kg.layout;

import java.util.HashMap;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 6, 2020 11:51:44 PM 
*   
*/
public class SearchCache {
	private HashMap<String, Object> fCacheMap = new HashMap<>();
	
	public boolean contains(String id, String key) {
		return fCacheMap.containsKey(concat(id, key));
	}
	
	public Object get(String id, String key) {
		return fCacheMap.get(concat(id, key));
	}
	
	public void set(String id, String key, Object value) {
		fCacheMap.put(concat(id, key), value);
	}
	
	private String concat(String id, String key) {
		return id.concat(key);
	}
}
