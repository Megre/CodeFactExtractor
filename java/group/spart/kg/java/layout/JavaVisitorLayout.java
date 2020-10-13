package group.spart.kg.java.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.ASTNode;

import group.spart.error.Assert;
import group.spart.kg.java.prop.AbstractPropertyHandler;
import group.spart.kg.layout.ConfigItem;
import group.spart.kg.layout.ConfigItemFactory;
import group.spart.kg.layout.ConfigItemValue;
import group.spart.kg.layout.InstancePool;
import group.spart.kg.layout.PackageItem;
import group.spart.kg.layout.PropertyItem;
import group.spart.kg.layout.SearchCache;
import group.spart.kg.layout.VisitorItem;
import group.spart.kg.uri.DefaultNamespace;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Aug 15, 2020 5:31:23 PM 
*   
*/
public class JavaVisitorLayout<T> {
	private List<ConfigItem> fConfigItems;
	private HashMap<String, VisitorItem> fVisitorMap;
	private HashMap<String, ConfigItemValue> fPackageMap;
	private HashMap<String, PropertyItem> fPropertyMap;
	
	private VisitorItem fEntryVisitorItem;
	
	private SearchCache fCache = new SearchCache();
	
	/**
	 * @param configFile path of visitor layout configuration file (.cfg).
	 * @see group.spart.kg.layout.ConfigItem
	 */
	public JavaVisitorLayout(String configFile) {
		fConfigItems = ConfigItemFactory.parse(configFile);
		buildVisitorMap();
		buildPackageMap();
		buildPropertMap();
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends T> entryVisitorClass() {
		final String name = fEntryVisitorItem.getValue("name");
		try {
			return (Class<? extends T>) 
					Class.forName(qualifiedVisitorName(name));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> childVisitors(Class<?> visitorClass) {
		if(fCache.contains("childVisitors", visitorClass.getSimpleName())) 
			return (List<T>) fCache.get("childVisitors", visitorClass.getSimpleName());
			
		final List<T> cv = new ArrayList<T>();
		final VisitorItem item = getVisitorItem(visitorClass);
		if(item != null) {
			final String[] children = item.getList("child");
			for(String child: children) {
				cv.add((T) InstancePool.instance(qualifiedVisitorName(child)));
			}
		}
		fCache.set("childVisitors", visitorClass.getSimpleName(), cv);
		return cv;
	}
	
	@SuppressWarnings("unchecked")
	public List<AbstractPropertyHandler> propertyHandlers(ASTNode node) {
		final String searchId = "propertyHandler", searchKey = node.getClass().getSimpleName();
		
		if(fCache.contains(searchId, searchKey)) 
			return (List<AbstractPropertyHandler>) fCache.get(searchId, searchKey);
		
		final List<PropertyItem> itemList = propertyContext(node);
		final List<AbstractPropertyHandler> handlerList = new ArrayList<>();
		for(PropertyItem item: itemList) {
			AbstractPropertyHandler handler = InstancePool.<AbstractPropertyHandler>instance(qualifiedHandlerName(item.getValue("handler")));
			handler.setPropertyUri(DefaultNamespace.uriOfPrefix(item.getValue("namespace")) + item.getValue("name"));
			handlerList.add(handler);
		}
		
		fCache.set(searchId, searchKey, handlerList);
		
		return handlerList;
	}
	
	public VisitorItem getVisitorItem(Class<?> visitorClass) {
		Assert.ensure(fVisitorMap.containsKey(visitorClass.getSimpleName()), 
				"The visitor '" + visitorClass.getSimpleName() + "' wasn't configured!");
		
		return fVisitorMap.get(visitorClass.getSimpleName());
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(String key: fPackageMap.keySet())
			sb.append(key).append(" = ").append(fPackageMap.get(key).toString()).append("\n");
		sb.append("entry: ").append(fEntryVisitorItem.toString()).append("\n");
		for(String key: fVisitorMap.keySet())
			sb.append(key).append(" = ").append(fVisitorMap.get(key).toString()).append("\n");
		for(String key: fPropertyMap.keySet())
			sb.append(key).append(" = ").append(fPropertyMap.get(key).toString()).append("\n");
		
		return sb.toString();
	}
	
	private String qualifiedVisitorName(String name) {
		return name.contains(".")?name:String.format("%s.%s", fPackageMap.get("visitor").asSingleValue().getValue(), name);
	}
	
	private String qualifiedHandlerName(String name) {
		return name.contains(".")?name:String.format("%s.%s", fPackageMap.get("handler").asSingleValue().getValue(), name);
	}
	
	private List<PropertyItem> propertyContext(ASTNode node) {
		final String searchCtx = node.getClass().getSimpleName();
		final List<PropertyItem> itemList = new ArrayList<>(); 
		
		for(Entry<String, PropertyItem> entry: fPropertyMap.entrySet()) {
			final String[] contexts = entry.getValue().getList("context");
			for(String ctx: contexts) {
				if(ctx.equals(searchCtx)) {
					itemList.add(entry.getValue());
					break;
				}
			}
		}
		return itemList;
	}
	
	private void buildPropertMap() {
		fPropertyMap = new HashMap<>();
		for(ConfigItem item: fConfigItems) {
			if(!(item instanceof PropertyItem)) continue;
			
			fPropertyMap.put(item.get("name").asSingleValue().getValue(), (PropertyItem) item);
		}
	}

	private void buildPackageMap() {
		fPackageMap = new HashMap<>();
		for(ConfigItem item: fConfigItems) {
			if(item instanceof PackageItem) {
				fPackageMap.putAll(item.getKeyValueMap());
				break;
			}
		}
	}

	private void buildVisitorMap() {
		fVisitorMap = new HashMap<>();
		for(ConfigItem item: fConfigItems) {
			if(!(item instanceof VisitorItem)) continue;
			
			if(fEntryVisitorItem == null) fEntryVisitorItem = (VisitorItem) item;
			fVisitorMap.put(item.get("name").asSingleValue().getValue(), (VisitorItem) item);
		}
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		JavaVisitorLayout layout = new JavaVisitorLayout("C:\\Users\\TF\\Documents\\Workspace\\SparT\\data\\ontology\\testFactExtractor\\visitor_layout.cfg");
		System.out.println(layout.toString());
	}
	
}
