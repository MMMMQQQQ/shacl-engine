package at.ac.tuwien.shacl;

import org.junit.Test;

import at.ac.tuwien.shacl.test.util.HelperClass;

public class TestCoreConstraints extends AbstractConstraintTester {

	@Test
	public void testAndConstraint() {
		this.testModelInvalid("queryAndConstraintInvalid.ttl");
		this.testModelValid("queryAndConstraintValid.ttl");
	}
	
	@Test
	public void testNotConstraint() {
		this.testModelInvalid("queryNotConstraintInvalid.ttl");
		this.testModelValid("queryNotConstraintValid.ttl");
	}
	
	@Test
	public void testOrConstraint() {
		this.testModelValid("queryOrConstraintValid.ttl");
		this.testModelInvalid("queryOrConstraintInvalid.ttl");
	}
	
	@Override
	protected String getBaseDir() {
		return HelperClass.CoreConstraint_dir;
	}
}
