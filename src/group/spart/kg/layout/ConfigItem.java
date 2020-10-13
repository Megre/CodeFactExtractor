package group.spart.kg.layout;

import java.util.HashMap;

/** 
* The base of {@link PackageItem}, {@link VistiorItem}, and {@link PropertyItem}.
* See the visitor layout configuration file (.cfg) for detailed information.
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Aug 15, 2020 5:57:25 PM 
*/
public abstract class ConfigItem {
	private HashMap<String, ConfigItemValue> fKeyValueMap = new HashMap<>(); 
	
	public ConfigItemValue get(String key) {
		return fKeyValueMap.get(key);
	}
	
	public String getValue(String key) {
		if(!fKeyValueMap.containsKey(key)) return null;
		
		return fKeyValueMap.get(key).asSingleValue().getValue();
	}
	
	public String[] getList(String key) {
		if(!fKeyValueMap.containsKey(key)) return new String[] {};
		
		return fKeyValueMap.get(key).asListValue().getList();
	}
	
	public String[][] getMultipleList(String key) {
		if(!fKeyValueMap.containsKey(key)) return new String[][] {};
		
		return fKeyValueMap.get(key).asMultipleListValue().getMultipleList();
	}
	
	public void addItem(String key, ConfigItemValue value) {
		if(fKeyValueMap.containsKey(key)) {
			final ConfigItemValue itemValue = fKeyValueMap.get(key);
			fKeyValueMap.put(key, itemValue.append(value.getRawValue()));
		}
		else
			fKeyValueMap.put(key, value);
	}
	
	public HashMap<String, ConfigItemValue> getKeyValueMap() {
		return fKeyValueMap;
	}
	
	/**
	 * name = value
	 * @return value of name. For package item, returns null.
	 */
	public String getName() {
		return getValue("name");
	}
	
	/**
	 * namespace = value
	 * @return value of namespace. For package item, returns null.
	 */
	public String getNamespace() {
		return getValue("namespace");
	}
	
	@Override
	public String toString() {
		return hashMapToString(fKeyValueMap);
	}
	
	public static String hashMapToString(HashMap<?, ?> map) {
		StringBuffer stringBuffer = new StringBuffer("[");

		for(Object key: map.keySet()) {
			stringBuffer.append(key).append("->").append(map.get(key)).append("; ");
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}
}
