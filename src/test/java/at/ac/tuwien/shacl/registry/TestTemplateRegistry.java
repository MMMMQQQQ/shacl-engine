package at.ac.tuwien.shacl.registry;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;

public class TestTemplateRegistry {
	@Test
	public void testPropertyConstraintTemplates() {
		SHACLMetaModelRegistry registry = SHACLMetaModelRegistry.getRegistry();

		assertTrue(registry.getPropertyConstraintsURIs().size()>0);
		assertTrue(registry.getInversePropertyConstraintsURIs().size()>0);
	}
}
