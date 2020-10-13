package group.spart.kg.layout;

import java.util.HashMap;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Aug 9, 2020 10:28:43 PM 
*  
*/
public class InstancePool {
	private static HashMap<Class<?>, Object> fVisitorMap;
	
	private InstancePool() { }
	
	static {
		fVisitorMap = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T instance(Class<? extends T> clazz) {
		 if(fVisitorMap.containsKey(clazz)) {
			 return (T) fVisitorMap.get(clazz);
		 }
		 
		 try {
			Object newInstance = clazz.newInstance();
			fVisitorMap.put(clazz, newInstance);
			return (T) newInstance;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T instance(String className) {
		try {
			return (T) instance(Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
