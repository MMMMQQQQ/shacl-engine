package at.ac.tuwien.shacl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Test;

import at.ac.tuwien.shacl.sparql.QueryBuilder;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.test.util.HelperClass;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.RDF;

public class DummyTestClass {
	@Test
	public void dummy() {
		String sparql = 
		"SELECT ?this (?this AS ?subject) ?predicate ?object "+
		"WHERE { "+
			"?this ?predicate ?object . "+
			"FILTER NOT EXISTS { "+
				"GRAPH ?shapesGraph { "+
					"?allowedValues (rdf:rest*)/rdf:first ?object . "+
				"}"+
			"}"+
		"}";
		
		Model model = ModelFactory.createDefaultModel();
		model.read(HelperClass.Base_dir+HelperClass.Basic_propConst_dir+"queryAllowedValuesValid.ttl");
		QueryBuilder qb = new QueryBuilder(sparql, model.getNsPrefixMap());
		
		System.out.println(model.listStatements(null, SHACL.property, (RDFNode) null).toList().get(0).getObject().asResource().getProperty(SHACL.allowedValues).getObject());
		qb.addBinding("allowedValues", model.listStatements(null, SHACL.property, (RDFNode) null).toList().get(0).getObject().asResource().getProperty(SHACL.allowedValues).getObject());
		qb.addBinding("predicate", model.getProperty("http://example.com/ns#someProperty"));
		qb.addThisBinding(model.getResource("http://example.com/ns#AllowedValuesExampleInvalidResource"));
		System.out.println(qb.getBindings());
		SPARQLQueryExecutor.isQueryValid(qb.getQueryString(), model, qb.getBindings());
	}
	
	@Test
	public void createFile() throws FileNotFoundException {
		Model model = SHACL.getModel();
		model.write(new FileOutputStream(new File("shacl.rdf")), "RDF/XML-ABBREV");
	}
}
