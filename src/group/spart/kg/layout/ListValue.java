package group.spart.kg.layout;

/** 
* Represents a list of values. Its raw value is seperated by "," (e.g. "value1, value2, value3").
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 6, 2020 2:58:54 PM 
*/
public class ListValue extends ConfigItemValue {

	protected String[] fList;
	protected final static String SEPERATOR = ",";
	
	/**
	 * @param value
	 */
	public ListValue(String value) {
		this(value, SEPERATOR);
	}
	
	protected ListValue(String value, String sperator) {
		super(value);
		
		fList = value.split(sperator);
		for(int i=0; i<fList.length; ++i)
			fList[i] = fList[i].trim();
	}

	public String[] getList() {
		return fList;
	}

	/* (non-Javadoc)
	 * @see group.spart.kg.layout.ConfigItemValue#append(java.lang.String)
	 */
	@Override
	public ConfigItemValue append(String value) {
		return new ListValue(getRawValue() + SEPERATOR + value);
	}
	
}
