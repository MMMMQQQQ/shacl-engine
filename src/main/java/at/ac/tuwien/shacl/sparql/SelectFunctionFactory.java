package at.ac.tuwien.shacl.sparql;

import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.expr.NodeValue;

public class SelectFunctionFactory extends SHACLFunctionFactory {
	@Override
	protected NodeValue executeQuery(String query, Model model,
			QuerySolutionMap qsm) {
		RDFNode result = new SPARQLQueryExecutor().execSelect(query, model, qsm);
		return NodeValue.makeNode(result.asNode());
	}
}
