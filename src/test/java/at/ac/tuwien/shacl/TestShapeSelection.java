package at.ac.tuwien.shacl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.ac.tuwien.shacl.test.util.HelperClass;
import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.Model;

public class TestShapeSelection {
	@Test
	public void testScopeClassSelection() {
		Model model = HelperClass.getModelFromFile("shapeselection/scopeclass.ttl");
		SHACLValidator validator = new SHACLValidator();
		validator.validateGraph(model);
		assertEquals(1, validator.getInstantiatedShapes(model).size());
	}
	
	@Test
	public void testRdfTypeSelection() {
		Model model = HelperClass.getModelFromFile("shapeselection/rdftype.ttl");
		SHACLValidator validator = new SHACLValidator();
		validator.validateGraph(model);
		assertEquals(2, validator.getInstantiatedShapes(model).size());
	}
	
	@Test
	public void testNodeShapeSelection() {
		Model model = HelperClass.getModelFromFile("shapeselection/nodeshape.ttl");
		SHACLValidator validator = new SHACLValidator();
		validator.validateGraph(model);
		assertEquals(1, validator.getInstantiatedShapes(model).size());
	}
}
