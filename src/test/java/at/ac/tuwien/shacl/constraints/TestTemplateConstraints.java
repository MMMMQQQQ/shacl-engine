package at.ac.tuwien.shacl.constraints;

import org.junit.Test;

import at.ac.tuwien.shacl.test.util.HelperClass;

public class TestTemplateConstraints extends AbstractConstraintTester {

	@Test
	public void testTemplateConstraintInvalid() {
		this.testModelInvalid("queryTemplateConstraintInvalid.ttl");
	}
	
	@Test
	public void testTemplateConstraintValid() {
		this.testModelValid("queryTemplateConstraintValid.ttl");
	}
	
	@Override
	protected String getBaseDir() {
		return HelperClass.TemplateConstraint_dir;
	}

}
