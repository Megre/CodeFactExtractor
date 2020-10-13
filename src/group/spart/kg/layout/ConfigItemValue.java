package group.spart.kg.layout;

/** 
* The base of {@link SingleValue}, {@link ListValue}, and {@link MultipleListValue}.
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 6, 2020 2:55:12 PM 
*   
*/
public abstract class ConfigItemValue {
	protected String fValue;
	
	public ConfigItemValue(String value) {
		fValue = value.trim();
	}
	
	public SingleValue asSingleValue() {
		return (SingleValue) this;
	}
	
	public ListValue asListValue() {
		if(this instanceof ListValue) return (ListValue) this;
		
		return new ListValue(fValue);
	}
	
	public MultipleListValue asMultipleListValue() {
		if(this instanceof MultipleListValue) return (MultipleListValue) this;
		
		return new MultipleListValue(fValue);
	}
	
	public String getRawValue() {
		return fValue;
	}
	
	@Override
	public String toString() {
		return fValue;
	}
	
	/**
	 * Appends the value.
	 * @param value the value that appends to the current value.
	 * @return appended item value.
	 */
	public abstract ConfigItemValue append(String value);
}
