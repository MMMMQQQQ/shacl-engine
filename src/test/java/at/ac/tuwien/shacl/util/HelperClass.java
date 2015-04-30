package at.ac.tuwien.shacl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class HelperClass {
	private static String MAVEN_RES_PATH = "src/main/resources/";
	
	public static Model getModelFromFile(String filename) {
		Model model = ModelFactory.createDefaultModel();
		try {
			model.read(new FileInputStream(new File(MAVEN_RES_PATH+filename)), null, "TURTLE");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return model;
	}
}
