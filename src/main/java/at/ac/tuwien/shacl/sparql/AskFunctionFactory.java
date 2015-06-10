package at.ac.tuwien.shacl.sparql;

import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.expr.NodeValue;

public class AskFunctionFactory extends SHACLFunctionFactory {
	@Override
	protected NodeValue executeQuery(String query, Model model,
			QuerySolutionMap qsm) {

	boolean result = new SPARQLQueryExecutor().execAsk(query, model, qsm);

	return NodeValue.makeBoolean(result);
	}
}


