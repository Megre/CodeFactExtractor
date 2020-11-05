package group.spart.kg.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.*;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.ARQInternalErrorException;
import org.apache.jena.sparql.core.Prologue;

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
		final String queryFile = baseDir + "\\query.sparql";
		
		final JavaVisitorLayout<AbstractASTNodeVisitor> visitorLayout = new JavaVisitorLayout<>(visitorLayoutPath);
		final JavaProject project = new JavaProject(projectPath, new String[] { classEntry }, new String[] { srcEntry });
		final OntologyModelNet ontNet = new OntologyModelNet(ontModelPath);
		final FactExtractor extractor = new FactExtractor(project, ontNet.loadOntModel(), visitorLayout);
		extractor.registerVisitor(visitorLayout.entryVisitorClass());
		extractor.extract();
		
		final Model inferredModel = extractor.infer(ontNet.getAllAttachedRuleFiles());
		
		execSelect(loadSelectQueries(queryFile), inferredModel);
		ModelUtil.save(inferredModel, savePath);
		
	}
	
	private static void execSelect(List<String> sparqls, Model model) {
		for(String sparql: sparqls) {
			try(QueryExecution qexec = QueryExecutionFactory.create(sparql, model)) {
			    ResultSet results = qexec.execSelect() ;
			    out.println(ResultSetFormatter.asText(results, new Prologue(PrefixMapping.Factory.create().setNsPrefixes(model.getNsPrefixMap()))));
			}
		    catch(QueryParseException e) {
		    	e.printStackTrace();
			}
			catch(ARQInternalErrorException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static List<String> loadSelectQueries(String file) {
		StringBuffer prefixes = new StringBuffer();
		StringBuffer queryBody = new StringBuffer();
		
		List<String> sparqls = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))))) {
			String line = null;
			while((line = br.readLine()) != null) {
				if(trimComment(line).trim().toLowerCase().startsWith("select")) break;
				
				prefixes.append(line).append("\n");
			}
			
			while(line != null) {
				if(trimComment(line).contains(";")) {
					queryBody.append(line);
					queryBody.delete(queryBody.indexOf(";"), queryBody.length());
					sparqls.add(prefixes.toString() + queryBody.toString());
					queryBody.delete(0, queryBody.length()-1);
				}
				
				queryBody.append(line).append("\n");
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return sparqls;
	}
	
	private static String trimComment(String line) {
		int index = line.indexOf('#');
		if(index != -1) {
			return line.substring(0, index);
		}
		return line;
	}

}
