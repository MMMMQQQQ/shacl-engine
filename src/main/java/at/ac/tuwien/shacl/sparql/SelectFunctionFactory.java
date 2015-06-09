package at.ac.tuwien.shacl.sparql;

import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.expr.NodeValue;

public class SelectFunctionFactory extends SHACLFunctionFactory {
	@Override
	protected NodeValue executeQuery(String query, Model model,
			QuerySolutionMap qsm) {
		System.out.println("qsm: "+qsm);
		System.out.println("*********model***********");
		model.write(System.out, "TURTLE");
		System.out.println("*********model***********");
		RDFNode result = SPARQLQueryExecutor.execSelect(query, model, qsm);
		
		return NodeValue.makeNode(result.asNode());
	}
}
