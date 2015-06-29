package at.ac.tuwien.shacl;

import org.junit.Test;

import at.ac.tuwien.shacl.test.util.HelperClass;

/**
 * Tests core constraints with some anonymous shape nodes.
 * 
 * This set of tests will fail currently as this functionality
 * is not supported yet.
 * 
 * @author xlin
 *
 */
public class TestCoreConstraintsAnonShapes extends AbstractConstraintTester {

	//@Test
	public void testAndConstraint() {
		this.testModelInvalid("queryAndConstraintAnonInvalid.ttl");
		this.testModelValid("queryAndConstraintAnonValid.ttl");
	}
	
	@Override
	protected String getBaseDir() {
		return HelperClass.CoreConstraint_dir;
	}
}
