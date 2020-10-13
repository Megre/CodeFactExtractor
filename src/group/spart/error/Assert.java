package group.spart.error;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Aug 26, 2020 7:25:27 AM 
*/
public class Assert {
	public static void ensureArgument(boolean test, String message) {
		if(!test) throw new IllegalInputArgumentExeption(message);
	}
	
	public static void ensure(boolean test, String message) {
		if(!test) throw new UnexpectedResultExeption(message);
	}
	
	public static void info(Object message) {
		System.out.println("[INFO] " + message.toString());
	}
	
	public static void info(boolean test, Object message) {
		if(test) {
			System.out.println("[INFO] " + message.toString());
		}
	}
	
	public static void warn(Object message) {
		System.err.println("[WARN] " + message.toString());
	}
	
	public static void warn(boolean test, Object message) {
		if(test) {
			System.err.println("[WARN] " + message.toString());
		}
	}
}
