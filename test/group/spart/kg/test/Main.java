package group.spart.kg.test;

import java.io.File;
import java.io.IOException;

import group.spart.kg.OntologyModelNet;
import group.spart.kg.java.FactExtractor;
import group.spart.kg.java.JavaProject;
import group.spart.kg.java.layout.JavaVisitorLayout;
import group.spart.kg.java.visitor.AbstractASTNodeVisitor;
import group.spart.kg.util.ModelUtil;

/** 
* @author megre
* @email renhao.x@seu.edu.cn
* @version created on: Sep 4, 2020 4:21:28 PM
*/
public class Main {
	public static final String BASE_DIR = new File("").getAbsolutePath() + "\\data";
	
	public static void main(String[] args) throws IOException {
		final String baseDir = BASE_DIR;
		final String projectPath = baseDir + "\\JHotDraw5.1";
		final String classEntry = projectPath + "\\bin";
		final String srcEntry = projectPath + "\\src";
		final String ontModelPath = baseDir + "\\ontology models";
		final String visitorLayoutPath = baseDir + "\\visitor_layout.cfg";
		final String savePath = baseDir + "\\JHotDraw5.1.owl";
		
		final JavaVisitorLayout<AbstractASTNodeVisitor> visitorLayout = new JavaVisitorLayout<>(visitorLayoutPath);
		final JavaProject project = new JavaProject(projectPath, new String[] { classEntry }, new String[] { srcEntry });
		final OntologyModelNet ontNet = new OntologyModelNet(ontModelPath);
		final FactExtractor extractor = new FactExtractor(project, ontNet.loadOntModel(), visitorLayout);
		extractor.registerVisitor(visitorLayout.entryVisitorClass());
		extractor.extract();
		
		ModelUtil.save(extractor.getModel(), savePath);
	}

}
