package at.ac.tuwien.shacl.registry;

import org.junit.Test;

import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import at.ac.tuwien.shacl.vocabulary.SHACL;

public class TestFunctionRegistry {
	@Test
	public void testBuildInFunctions() {
		Model model = SHACL.getModel();
		SHACLMetaModelRegistry registry = new SHACLMetaModelRegistry();
		registry.registerFunctions(model);
	}
}
