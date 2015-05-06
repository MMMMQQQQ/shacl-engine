package at.ac.tuwien.shacl;

import org.junit.Test;

import at.ac.tuwien.shacl.sparql.QueryBuilder;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.util.Config;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class DummyTestClass {
	@Test
	public void dummy() {
		String sparql = "SELECT ?this (?this AS ?subject) (?prop AS ?object)" +
			"WHERE {"+
				"{"+
					"?this sh:property ?prop ."+
					"FILTER (isBlank(?prop) && NOT EXISTS { ?prop a ?any }) ."+
				"}"+
				"FILTER (!sh:hasShape(?prop, sh:PropertyConstraint, ?shapesGraph)) ."+
			"}";
		
		Model model = ModelFactory.createDefaultModel();
		model.read(Config.Base_res_dir+"shacl.shacl.ttl");
		QueryBuilder qb = new QueryBuilder(sparql, model.getNsPrefixMap());
		SPARQLQueryExecutor.isQueryValid(qb.getQueryString(), model, null);
	}
}
