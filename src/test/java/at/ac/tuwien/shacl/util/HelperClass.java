package at.ac.tuwien.shacl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class HelperClass {
	public static final String Base_propConst_dir = "basic_propertyconstraints/";

	public static final String Base_dir = "src/test/resources/";
	
	public static Model getModelFromFile(String filename) {
		Model model = ModelFactory.createDefaultModel();
		try {
			model.read(new FileInputStream(new File(Config.Base_res_dir+filename)), null, "TURTLE");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return model;
	}
}
