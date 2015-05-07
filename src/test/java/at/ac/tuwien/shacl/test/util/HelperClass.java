package at.ac.tuwien.shacl.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import at.ac.tuwien.shacl.util.Config;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class HelperClass {
	public static final String Basic_propConst_dir = "basic_propertyconstraints/";

	public static final String Base_dir = "src/test/resources/";
	
	public static final String GlobalNativeConstraint_dir = "globalnativeconstraints/";
	
	public static Model getModelFromFile(String filename) {
		Model model = ModelFactory.createDefaultModel();
		
		model.read(Base_dir+filename, "TURTLE");

		return model;
	}
}
