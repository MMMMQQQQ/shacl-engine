package at.ac.tuwien.shacl;

import java.io.FileNotFoundException;

import at.ac.tuwien.shacl.util.Config;
import at.ac.tuwien.shacl.validation.SHACLValidator;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Main {
	private static String MAVEN_RES_PATH = "src/main/resources/";
	
	public static void main(String[] args) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel();
		model.read(Config.base_res_dir + "query3.ttl");
		
//		SHACLConstraintRegistry registry = new SHACLConstraintRegistry();
//		registry.register(model);
		
		SHACLValidator validator = new SHACLValidator(model);
		Model errorModel = validator.validateGraph();
		errorModel.write(System.out, "TURTLE");
		//get all sh:sparql
//		model.read(Config.base_res_dir + "shacl.shacl.ttl");
//		List<Resource> s = model.listResourcesWithProperty(SHACL.sparql).toList();
//		for(Resource st : s) {
//			System.out.println(st);
//			System.out.println(st.getProperty(SHACL.sparql).getObject());
//			System.out.println("************");
//		}
	}
}
