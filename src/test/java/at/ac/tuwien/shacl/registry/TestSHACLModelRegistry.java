package at.ac.tuwien.shacl.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.tuwien.shacl.executable.Executables;
import at.ac.tuwien.shacl.executable.sparql.SPARQLExecutableLanguage;
import at.ac.tuwien.shacl.vocabulary.SHACL;

public class TestSHACLModelRegistry {
	private static SHACLModelRegistry registry;
	
	@BeforeClass
	public static void setupClass() {
		Executables.addExecutable(new SPARQLExecutableLanguage());
		registry = SHACLModelRegistry.get();
	}
	
	@Test
	public void testTemplates() {
		assertTrue(registry.getTemplates().size()>0);
		assertNotNull(registry.getTemplate(SHACL.NS + "AbstractCountPropertyConstraint"));
		assertNotNull(registry.getTemplate(SHACL.NS + "XorConstraint"));

		System.out.println(registry.getFunction(SHACL.NS + "valueCount").getExecutableBody(SHACL.sparql));
	}
	
	@Test
	public void testFunctions() {
		assertTrue(registry.getFunctions().size()>0);
		assertNotNull(registry.getFunction(SHACL.NS + "valueCount"));
		assertEquals(registry.getFunction(SHACL.NS + "valueCount").getURI(), SHACL.NS + "valueCount");
	}
}
