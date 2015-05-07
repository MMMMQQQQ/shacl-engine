package at.ac.tuwien.shacl;

import org.junit.Test;

import at.ac.tuwien.shacl.registry.ConstraintExtractor;
import at.ac.tuwien.shacl.test.util.HelperClass;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;

public class TestGlobalNativeConstraints {
	@Test
	public void testConstraint1() {
		Model model = HelperClass.getModelFromFile(HelperClass.GlobalNativeConstraint_dir+"globalNativeConstraint1.ttl");
		model.add(SHACL.getModel());
		ConstraintExtractor.getGlobalNativeConstraintDefinitions(model);
	}
	
	@Test
	public void testConstraint2() {
		Model model = HelperClass.getModelFromFile(HelperClass.GlobalNativeConstraint_dir+"globalNativeConstraint2.ttl");
	}
	
	@Test
	public void testConstraint3() {
		Model model = HelperClass.getModelFromFile(HelperClass.GlobalNativeConstraint_dir+"globalNativeConstraint3.ttl");
	}
}
