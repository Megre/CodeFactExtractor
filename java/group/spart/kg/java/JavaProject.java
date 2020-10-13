package group.spart.kg.java;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 4, 2020 9:27:03 PM 
*/
public class JavaProject {

	private String fPath;
	private String[] fSourcePaths;
	private String[] fClassPaths;
	
	public JavaProject(String path, String[] classPaths, String[] sourcePaths) {
		fPath = path;
		fSourcePaths = sourcePaths;
		fClassPaths = classPaths;
	}
	
	public String getProjectPath() {
		return fPath;
	}
	
	public String[] getSourcePaths() {
		return fSourcePaths;
	}
	
	public String[] getClassPaths() {
		return fClassPaths;
	}
	
	protected String[] listSourceFiles() {
		List<String> fileList = new ArrayList<>();
		Deque<File> dirStack = new ArrayDeque<>();
		for(String srcPath: fSourcePaths) {
			dirStack.push(new File(srcPath));
			while(!dirStack.isEmpty()) {
				File currentDir = dirStack.pop();
				for(File file: currentDir.listFiles()) {
					if(file.isDirectory()) dirStack.add(file);
					else if(file.isFile() && file.getName().toLowerCase().endsWith(".java")) {
						fileList.add(file.getAbsolutePath());
					}
				}
			}
		}
		
		final String[] sourceFiles = new String[fileList.size()];
		fileList.toArray(sourceFiles);
		return sourceFiles;
	}
	

}
