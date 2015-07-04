package at.ac.tuwien.shacl.constraints;

import org.junit.Test;

import at.ac.tuwien.shacl.test.util.HelperClass;

public class TestNativeConstraints extends AbstractConstraintTester {

	@Test
	public void testNativeConstraintsInvalid() {
		this.testModelInvalid("queryNativeConstraintInvalid.ttl");
		this.testModelInvalid("queryNativeConstraint2Invalid.ttl");
	}
	
	@Test
	public void testNativeConstraintValid() {
		this.testModelValid("queryNativeConstraintValid.ttl");
	}
	
	@Override
	protected String getBaseDir() {
		return HelperClass.NativeConstraint_dir;
	}
}