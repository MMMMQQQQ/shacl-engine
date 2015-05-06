package at.ac.tuwien.shacl;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import at.ac.tuwien.shacl.util.HelperClass;
import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class TestBasicPropertyConstraints {
	@Test
	public void testInvalidAllowedValues() {
		this.testModelInvalid("queryAllowedValuesInvalid.ttl");
	}
	
	@Test
	public void testInvalidDatatype() {
		this.testModelInvalid("query2.ttl");
	}
	
	@Test
	public void testInvalidHasValue() {
		this.testModelInvalid("query3.ttl");
	}
	
	@Test
	public void testValidHasValue() {
		this.testModelValid("query4.ttl");
	}
	
	@Test
	public void testValidAllowedValues() {
		this.testModelValid("queryAllowedValuesValid.ttl");
	}
	
	@Test
	public void testDatatypeInvalid() {
		this.testModelInvalid("queryDatatypeInvalid.ttl");
	}
	
	@Test
	public void testDatatypeValid() {
		this.testModelValid("queryDatatypeValid.ttl");
	}
	
	@Test
	public void testInvalidCount() {
		this.testModelInvalid("queryMinMaxInvalid.ttl");
	}
	
	@Test
	public void testValidCount() {
		this.testModelValid("queryMinMaxValid.ttl");
	}
	
	@Test
	public void testInvalidNodeKind() {
		this.testModelInvalid("queryNodeKind.ttl");
	}
	
	@Test
	public void testValidNodeKind() {
		this.testModelValid("queryNodeKindValid.ttl");
	}
	
	@Test
	public void testInvalidValueType() {
		this.testModelInvalid("queryValueTypeInvalid.ttl");
	}
	
	@Test
	public void testValidValueType() {
		this.testModelValid("queryValueTypeValid.ttl");
	}

	private void testModel(String filename, boolean validModel) {
		Model model;

		model = ModelFactory.createDefaultModel();
		model.read(HelperClass.Base_dir+HelperClass.Base_propConst_dir+filename);
		SHACLValidator validator = new SHACLValidator(model);
		Model errorModel = validator.validateGraph();
		if(validModel) {
			assertFalse(!errorModel.isEmpty());
		} else {
			assertFalse(errorModel.isEmpty());
		}
	}
	
	private void testModelValid(String filename) {
		this.testModel(filename, true);
	}
	
	private void testModelInvalid(String filename) {
		this.testModel(filename, false);
	}
}
