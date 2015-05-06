package at.ac.tuwien.shacl.registry;

import java.util.List;

import org.junit.Test;

import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;

public class TestTemplateRegistry {
	@Test
	public void testPropertyConstraintTemplates() {
		Model model = SHACL.getModel();
		List<Statement> pcStatements = model.listStatements(
				ResourceFactory.createResource("dfdfaf"), RDFS.subClassOf, (RDFNode) null).toList();
		System.out.println(pcStatements.size());
	}
}
