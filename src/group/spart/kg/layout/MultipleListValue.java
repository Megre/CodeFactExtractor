package group.spart.kg.layout;

/** 
* Represents a list of lists. In its raw value, different lists are seperated by ";".
* Within each list, different values are sperated by "," (e.g. "value1, value2; value3, value4, value5").
* 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 6, 2020 3:05:36 PM 
*   
*/
public class MultipleListValue extends ListValue {

	private String[][] fMultipleList;
	protected final static String SEPERATOR = ";";
	
	/**
	 * @param value
	 */
	public MultipleListValue(String value) {
		super(value, SEPERATOR);
		fMultipleList = new String[fList.length][];
		
		for(int i=0; i<fList.length; ++i) {
			fMultipleList[i] = fList[i].split(ListValue.SEPERATOR);
			for(int j=0; j<fMultipleList[i].length; ++j) {
				fMultipleList[i][j] = fMultipleList[i][j].trim();
			}
		}
	}
	
	public String[][] getMultipleList() {
		return fMultipleList;
	}
	
	/* (non-Javadoc)
	 * @see group.spart.kg.visitor.layout.ListValue#append(java.lang.String)
	 */
	@Override
	public ConfigItemValue append(String value) {
		return new MultipleListValue(getRawValue() + SEPERATOR + value);
	}
}
