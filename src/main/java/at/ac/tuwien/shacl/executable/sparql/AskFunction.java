package at.ac.tuwien.shacl.executable.sparql;

import java.util.Map;

import at.ac.tuwien.shacl.executable.Executables;
import at.ac.tuwien.shacl.registry.NamedModelsRegistry;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.expr.NodeValue;

/**
 * Defines the behavior of a SPARQL ASK query.
 * 
 * @author xlin
 *
 */
public class AskFunction extends AbstractFunctionFactory {
	@Override
	protected NodeValue executeQuery(String query, Model model,
			Map<String, RDFNode> bindings) {

	RDFNode result = Executables.getExecutable(SHACL.sparql).executeAsSingleValue(query, NamedModelsRegistry.get().getDataset(), bindings);

	return NodeValue.makeBoolean(result.asLiteral().getBoolean());
	}
}


