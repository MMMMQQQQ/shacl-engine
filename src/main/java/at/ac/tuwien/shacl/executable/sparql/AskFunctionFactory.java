package at.ac.tuwien.shacl.executable.sparql;

import java.util.Map;

import at.ac.tuwien.shacl.executable.Executables;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.expr.NodeValue;

public class AskFunctionFactory extends AbstractFunctionFactory {
	@Override
	protected NodeValue executeQuery(String query, Model model,
			Map<String, RDFNode> bindings) {
	//boolean result = new SPARQLQueryExecutor().execAsk(query, model, qsm);
	RDFNode result = Executables.getExecutable(SHACL.sparql).executeAsSingleValue(query, model, bindings);

	return NodeValue.makeBoolean(result.asLiteral().getBoolean());
	}
}


