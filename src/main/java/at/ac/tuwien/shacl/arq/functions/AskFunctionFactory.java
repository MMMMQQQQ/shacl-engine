package at.ac.tuwien.shacl.arq.functions;

import at.ac.tuwien.shacl.registry.SHACLMetaModelRegistry;
import at.ac.tuwien.shacl.sparql.QueryBuilder;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.Function;
import com.hp.hpl.jena.sparql.function.FunctionBase2;
import com.hp.hpl.jena.sparql.function.FunctionFactory;

public class AskFunctionFactory extends FunctionBase2 implements FunctionFactory {
	String uri;
	
	@Override
	public Function create(String uri) {
		this.uri = uri;
		return this;
	}

	@Override
	public NodeValue exec(NodeValue arg0, NodeValue arg1) {
		boolean result = true;
		
		if(SHACLMetaModelRegistry.getInstance().getFunction(uri)!= null) {
			QueryBuilder qb = new QueryBuilder(SHACLMetaModelRegistry.getInstance().getFunction(uri).getExecutableBody(),
					SHACLValidator.model.getNsPrefixMap());

			//TODO not working
			qb.addBinding("arg1", ResourceFactory.createResource(arg0.toString()));
			qb.addBinding("arg2", ResourceFactory.createResource(arg1.toString()));
			
			//TODO try to find a way to make things work with bindings instead
			String queryString = qb.getQueryString();
			queryString = queryString.replaceAll("\\?arg1", arg0.toString());
			queryString = queryString.replaceAll("\\?arg2", arg1.toString());

			result = SPARQLQueryExecutor.execAsk(queryString, SHACLValidator.model, qb.getBindings());
		}
		
		//System.out.println("result: "+result);
		return NodeValue.makeBoolean(result);
	}
}


