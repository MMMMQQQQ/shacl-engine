package at.ac.tuwien.shacl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import at.ac.tuwien.shacl.validation.SPARQLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Main {
	private static String MAVEN_RES_PATH = "src/main/resources/";
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Model model = ModelFactory.createDefaultModel();
		model.read(new FileInputStream(new File(MAVEN_RES_PATH+"query3.ttl")), null, "TURTLE");
		//violates constraint
		//model.read("query3.ttl");
		//doesn't violate any constraints. uncomment for testing
		//model.read("query4.ttl");
		//model.write(System.out, "RDF/XML");
		SPARQLValidator val = new SPARQLValidator(model);
//		for(Statement s : model.listStatements(null, SHACL.nodeShape, (RDFNode)null).toList()) {
//			System.out.println("subject: "+s.getSubject());
//			System.out.println("predicate: "+s.getPredicate());
//			System.out.println("object: "+s.getObject());
//			System.out.println("resource: "+s.getResource());
//		}
//		
//		List<Statement> statements = new ArrayList<Statement>();
//		
//		for(SelectionProperty p : SelectionProperty.values()) {
//			System.out.println("property: "+p.getProperty());
//			statements.addAll(model.listStatements(null, p.getProperty(), (RDFNode)null).toList());
//			System.out.println(statements);
//		}
		
		Model errorModel = val.validateAll();
		if(errorModel.isEmpty()) {
			System.out.println("No constraints violated");
		} else {
			System.out.println("*********Errors**********");
			errorModel.write(System.out, "TURTLE");
		}
	}
}
