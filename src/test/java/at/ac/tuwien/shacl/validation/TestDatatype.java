package at.ac.tuwien.shacl.validation;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.tuwien.shacl.util.HelperClass;

import com.hp.hpl.jena.rdf.model.Model;

public class TestDatatype {
	private static Model validModel;
	private static Model invalidModel;
	
	@BeforeClass
	public static void setup() {
		//validModels.add(HelperClass.getModelFromFile("queryMinMaxValid.ttl"));
		//validModels.add(HelperClass.getModelFromFile("queryAllowedValues"));
		
		//validModel = HelperClass.getModelFromFile("queryDatatypeValid.ttl");
		invalidModel = HelperClass.getModelFromFile("queryDatatypeInvalid.ttl");
	}
	
//	@Test
//	public void testValidAllowedValuesQuery() {
//		SPARQLValidator val = new SPARQLValidator(validModel);
//		Model model = val.validateAll();
//		System.out.println(model);
//		assertTrue(val.validateAll().isEmpty());
//	}
	
	@Test
	public void testInvalidAllowedValuesQuery() {
		SPARQLValidator val = new SPARQLValidator(invalidModel);
		Model model = val.validateAll();
		System.out.println(model);
		assertTrue(!val.validateAll().isEmpty());
	}
}
