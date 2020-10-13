package group.spart.error;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Aug 26, 2020 7:22:06 AM 
*/
public class IllegalInputArgumentExeption extends RuntimeException {

	private static final long serialVersionUID = 3950016156513091838L;
	
	public IllegalInputArgumentExeption(String message) {
		super(message);
	}

}
