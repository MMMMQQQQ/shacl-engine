package at.ac.tuwien.shacl;

import java.io.FileNotFoundException;

import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Example of a constraint validation.
 * The validation process will return a constraint violation model
 * against sh:datatype.
 * 
 * @author xlin
 *
 */
public class Example {
	public static void main(String[] args) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel();

		model.read("query2.ttl", "TURTLE");
		
		System.out.println("***********Input Model***********");
		model.write(System.out, "TURTLE");
		
		SHACLValidator validator = new SHACLValidator();
		Model errorModel = validator.validateGraph(model);
		
		System.out.println("***********Constraint Violations***********");
		errorModel.write(System.out, "TURTLE");
	}
}
