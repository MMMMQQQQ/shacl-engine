package at.ac.tuwien.shacl.constraints;

import static org.junit.Assert.assertFalse;
import at.ac.tuwien.shacl.test.util.HelperClass;
import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public abstract class AbstractConstraintTester {
	protected void testModel(String filename, boolean validModel) {
		Model model;

		model = ModelFactory.createDefaultModel();
		model.read(HelperClass.Base_dir+this.getBaseDir()+filename);
		SHACLValidator validator = new SHACLValidator();

		Model errorModel = validator.validateGraph(model);
		if(validModel) {
			assertFalse(!errorModel.isEmpty());
		} else {
			assertFalse(errorModel.isEmpty());
		}
	}
	
	protected void testModelValid(String filename) {
		this.testModel(filename, true);
	}
	
	protected void testModelInvalid(String filename) {
		this.testModel(filename, false);
	}
	
	protected abstract String getBaseDir();
}
