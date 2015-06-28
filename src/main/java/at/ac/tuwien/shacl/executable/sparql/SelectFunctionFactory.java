package at.ac.tuwien.shacl.executable.sparql;

import java.util.Map;

import at.ac.tuwien.shacl.executable.Executables;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.expr.NodeValue;

public class SelectFunctionFactory extends AbstractFunctionFactory {
	@Override
	protected NodeValue executeQuery(String query, Model model,
			Map<String, RDFNode> bindings) {
		//RDFNode result = new SPARQLQueryExecutor().execSelect(query, model, qsm);
		
		RDFNode result = Executables.getExecutable(SHACL.sparql).executeAsSingleValue(query, model, bindings);
		
		return NodeValue.makeNode(result.asNode());
	}
}
