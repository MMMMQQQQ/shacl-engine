package at.ac.tuwien.shacl.constraints;
import org.junit.Test;

import at.ac.tuwien.shacl.test.util.HelperClass;

public class TestBasicPropertyConstraints extends AbstractConstraintTester {
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
	public void testInvalidValueClass() {
		this.testModelInvalid("queryValueClassInvalid.ttl");
	}
	
	@Test
	public void testValidValueClass() {
		this.testModelValid("queryValueClassValid.ttl");
	}

	@Override
	protected String getBaseDir() {
		return HelperClass.Basic_propConst_dir;
	}
}
