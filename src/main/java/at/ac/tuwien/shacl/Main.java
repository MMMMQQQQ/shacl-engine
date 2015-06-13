package at.ac.tuwien.shacl;

import java.io.FileNotFoundException;

import at.ac.tuwien.shacl.util.Config;
import at.ac.tuwien.shacl.util.SHACLParsingException;
import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel();
		//model.read("queryOrConstraint.ttl", "TURTLE");
		//model.read("queryOrConstraint.ttl", "TURTLE");
		
		model.read("queryAndConstraint.ttl", "TURTLE");
		//model.read("queryMinMaxInvalid.ttl", "TURTLE");
		model.write(System.out, "TURTLE");
		
		SHACLValidator validator = new SHACLValidator();
		try {
			validator.validateGraph(model);
		} catch (SHACLParsingException e) {
			e.printStackTrace();
		}
	}
}
