package group.spart.kg.layout;

/** 
* Represents a single string.
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 6, 2020 2:57:43 PM 
*/
public class SingleValue extends ConfigItemValue {

	/**
	 * @param value raw value
	 */
	public SingleValue(String value) {
		super(value);
	}

	public String getValue() {
		return fValue;
	}
	
	@Override
	public String toString() {
		return getValue();
	}

	/* (non-Javadoc)
	 * @see group.spart.kg.visitor.layout.ConfigItemValue#append(java.lang.String)
	 */
	@Override
	public ConfigItemValue append(String value) {
		return new ListValue(getRawValue() + ListValue.SEPERATOR + value);
	}
}
