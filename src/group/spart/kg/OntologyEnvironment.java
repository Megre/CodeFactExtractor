/**
 * 
 */
package group.spart.kg;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.jena.ext.com.google.common.io.Files;

import group.spart.error.Assert;


/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Nov 14, 2017 10:09:34 AM 
*/
public class OntologyEnvironment {
	public static String ONTOLOGY_PATH;
	public static String RECOGNIZED_PATH;
	public static String COMPONENT_PATH;
	
	static {
		String ontPath = System.getenv("ONT_PATH");
		Assert.ensure(ontPath != null, "System variable wasn't set: ONT_PATH");
		
		ONTOLOGY_PATH = fixPath(ontPath);
		RECOGNIZED_PATH = ONTOLOGY_PATH + "/recognized";
		COMPONENT_PATH = ONTOLOGY_PATH + "/component";
	}
	
	public static boolean initRecognizedDir(String projId, String verId) {
		File f = new File(RECOGNIZED_PATH + "/" + projId + "/" + verId + "/");		
		if(f.exists() && f.isDirectory())
			return true;
		return f.mkdirs();
	}
	
	public static String getRecognizedDir(String projId, String verId) {
		String dir = RECOGNIZED_PATH + "/"  + projId + "/" + verId + "/";
		if(initRecognizedDir(projId, verId))
			return dir;
		return null;
	}
	
	public static String genResultDir(String projId, String verId) {
		String recDir = getRecognizedDir(projId, verId);
		if(recDir != null) {
			File f = new File(recDir + genResultDirName());
			if(!f.exists() || !f.isDirectory()) {
				if(f.mkdir())
					return f.getAbsolutePath() + "/";
			}
		}
		return null;
	}
	
	public static String genResultDirName() {
		return new SimpleDateFormat("yyyyMMdd.HHmmss").format(new Date());
	}
	
	public static String getPrivateOntsDir(String resultDir /*tdb path*/) {
		File ontDir = new File(resultDir + "/ontology");
		if(!ontDir.exists() || !ontDir.isDirectory()) {
			if(!buildPrivateOnts(resultDir))
				return null;
		}
		return ontDir.getAbsolutePath() + "/";
	}
	
	public static boolean buildPrivateOnts(String projId, String verId, String tdbName) {
		String recDir = getRecognizedDir(projId, verId);
		if(recDir != null) {
			return buildPrivateOnts(recDir + tdbName);
		}
		return false;
	}
	
	public static boolean buildPrivateOnts(String resultDir /*tdb path*/) {
		File rstDir = new File(resultDir);
		if(!rstDir.exists() || !rstDir.isDirectory())
			return false;
		
		File ontDir = new File(rstDir.getAbsolutePath() + "/ontology");
		if(!ontDir.exists() || !ontDir.isDirectory()) {
			if(!ontDir.mkdir())
				return false;
		}
			
		File oriOntDir = new File(ONTOLOGY_PATH);
		try {
			for(File f: oriOntDir.listFiles()) {
				if(!f.isFile()) continue;
				
				String fn = f.getAbsolutePath();
				if(fn.endsWith(OWLFileModel.ONT_MODEL_EXT) || fn.endsWith(OWLFileModel.ATTACHED_RULE_EXT))
					Files.copy(f, new File(ontDir.getAbsolutePath() + "/" + f.getName()));
			}	
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private static String fixPath(String path) {
		return path.replaceFirst("(/|\\\\)+$", "");
	}
	
	public static void main(String[] args) {
		buildPrivateOnts("C:/Users/megre/workspace/MRSBW/data/ontology/recognized/1/3/20180308.163058");
	}
}
