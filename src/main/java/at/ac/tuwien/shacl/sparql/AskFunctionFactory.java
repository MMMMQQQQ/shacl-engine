package at.ac.tuwien.shacl.sparql;

import at.ac.tuwien.shacl.registry.ModelRegistry;
import at.ac.tuwien.shacl.registry.SHACLMetaModelRegistry;

import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.Function;
import com.hp.hpl.jena.sparql.function.FunctionBase2;
import com.hp.hpl.jena.sparql.function.FunctionFactory;

public class AskFunctionFactory extends SHACLFunctionFactory {
	@Override
	protected NodeValue executeQuery(String query, Model model,
			QuerySolutionMap qsm) {
		System.out.println("qsm: "+qsm);
	boolean result = SPARQLQueryExecutor.execAsk(query, model, qsm);

	System.out.println("ask result: "+result);
	return NodeValue.makeBoolean(result);
	}
}


