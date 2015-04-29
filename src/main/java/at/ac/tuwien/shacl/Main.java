package at.ac.tuwien.shacl;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.shacl.validation.SPARQLValidator;
import at.ac.tuwien.shacl.vocabulary.SHACL;
import at.ac.tuwien.shacl.vocabulary.SelectionProperty;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public class Main {
	public static void main(String[] args) {
		
		Model model = ModelFactory.createDefaultModel();
		//violates constraint
		model.read("query3.ttl");
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
