package at.ac.tuwien.shacl.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.tuwien.shacl.util.HelperClass;

import com.hp.hpl.jena.rdf.model.Model;

public class TestAllowedValues {
	private static Model validAllowedValuesModel;
	private static Model invalidAllowedValuesModel;
	
	@BeforeClass
	public static void setup() {
		//validModels.add(HelperClass.getModelFromFile("queryMinMaxValid.ttl"));
		//validModels.add(HelperClass.getModelFromFile("queryAllowedValues"));
		
		validAllowedValuesModel = HelperClass.getModelFromFile("queryAllowedValuesValid.ttl");
		invalidAllowedValuesModel = HelperClass.getModelFromFile("queryAllowedValuesInvalid.ttl");
	}
	
	@Test
	public void testValidAllowedValuesQuery() {
		SPARQLValidator val = new SPARQLValidator(validAllowedValuesModel);
		Model model = val.validateAll();
		System.out.println(model);
		assertTrue(val.validateAll().isEmpty());
	}
	
	@Test
	public void testInvalidAllowedValuesQuery() {
		SPARQLValidator val = new SPARQLValidator(invalidAllowedValuesModel);
		Model model = val.validateAll();
		System.out.println(model);
		assertTrue(!val.validateAll().isEmpty());
	}
}
