package at.ac.tuwien.shacl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import at.ac.tuwien.shacl.test.util.HelperClass;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;

public class TestGraphTraverser {
//	@Test
//	public void testSubclassTraversing() {
//		Model model = SHACL.getModel();
//		
//		List<Statement> result = GraphTraverser.listAllSubclassesOfNodeAsSubject(ResourceFactory.createResource(SHACL.NS+"Macro"), model);
//		
//		//sh:Macro has 3 direct and indirect subclasses: sh:Template, sh:Function and sh:ConstraintTemplate
//		assertEquals(3, result.size());
//		
//		Set<Resource> shaclR = new HashSet<Resource>();
//		shaclR.add(SHACL.Template);
//		shaclR.add(SHACL.Function);
//		shaclR.add(SHACL.ConstraintTemplate);
//		
//		for(Statement s : result) {
//			assertTrue(shaclR.contains(s.getSubject()));
//		}
//	}
//	
//	@Test
//	public void testSuperclassTraversing() {
//		Model model = SHACL.getModel();
//		List<Statement> result = GraphTraverser.listDirectSuperclassesOfNodeAsObject(SHACL.GlobalNativeConstraint, model);
//		
//		assertEquals(2, result.size());
//		
//		//has two superclasses: sh:GlobalConstraint and sh:NativeConstraint
//		Set<Resource> shaclR = new HashSet<Resource>();
//		shaclR.add(SHACL.GlobalConstraint);
//		shaclR.add(SHACL.NativeConstraint);
//		
//		for(Statement s : result) {
//			assertTrue(shaclR.contains(s.getObject().asResource()));
//		}
//	}
//	
//	@Test
//	public void testRdfTypeSubclassing() {
//		Model model = HelperClass.getModelFromFile(HelperClass.GlobalNativeConstraint_dir+"globalNativeConstraint1.ttl");
//		model.add(SHACL.getModel());
//		List<Statement> result = GraphTraverser.listAllAndSubclassesOfRdfTypeAsSubject(SHACL.Constraint, model);
//		assertEquals(2, result.size());
//	}
}
