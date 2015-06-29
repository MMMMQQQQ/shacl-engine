package at.ac.tuwien.shacl.test.util;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class HelperClass {
	public static final String Basic_propConst_dir = "basic_propertyconstraints/";

	public static final String Base_dir = "src/test/resources/";
	
	public static final String NativeConstraint_dir = "nativeconstraints/";
	
	public static final String CoreConstraint_dir = "coreconstraints/";
	
	public static final String TemplateConstraint_dir = "templateconstraints/";
	
	public static final String ShapeSelection_dir = "shapeselection/";
	
	public static Model getModelFromFile(String filename) {
		Model model = ModelFactory.createDefaultModel();
		
		model.read(Base_dir+filename, "TURTLE");

		return model;
	}
}
