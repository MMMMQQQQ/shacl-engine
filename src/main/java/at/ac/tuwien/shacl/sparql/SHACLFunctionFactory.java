package at.ac.tuwien.shacl.sparql;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.shacl.registry.ModelRegistry;
import at.ac.tuwien.shacl.registry.SHACLMetaModelRegistry;

import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.sparql.ARQInternalErrorException;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprEvalException;
import com.hp.hpl.jena.sparql.expr.ExprList;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.Function;
import com.hp.hpl.jena.sparql.function.FunctionEnv;
import com.hp.hpl.jena.sparql.function.FunctionFactory;

public abstract class SHACLFunctionFactory implements FunctionFactory, Function {
	//from function factory
	@Override
	public Function create(String uri) {
		return this;
	}

	//from function
	@Override
	public void build(String uri, ExprList args) {
	}

	//from function
	@Override
	public NodeValue exec(Binding binding, ExprList args, String uri,
			FunctionEnv env) {
        if ( args == null ) {
        	throw new ARQInternalErrorException("Null args list") ;
        }
        
        List<NodeValue> evalArgs = new ArrayList<>() ;
        
        for ( Expr e : args )
        {
            NodeValue x = e.eval( binding, env );
            evalArgs.add( x );
        }
        
        NodeValue nv =  processNodeValues(evalArgs, uri, env) ;
        return nv ;
	}
	
	private NodeValue processNodeValues(List<NodeValue> nodeValues, String uri, FunctionEnv env) {
		if(nodeValues.size() == 0) {
			throw new ExprEvalException("Missing arguments");
		}

		Model model = ModelFactory.createModelForGraph(env.getActiveGraph());
		
		QueryBuilder qb = new QueryBuilder(SHACLMetaModelRegistry.getRegistry().getFunction(uri).getExecutableBody(),
				model.getNsPrefixMap());
		
		int i = 1;
		
		for(NodeValue node : nodeValues) {
			String arg = "arg"+i;
			
			if(node.isLiteral()) {
				qb.addBinding(arg, ResourceFactory.createTypedLiteral(node.asNode().getLiteral().getIndexingValue()));
			} else {
				qb.addBinding(arg, ResourceFactory.createResource(node.asString()));
			}
			
			i++;
		}
		
		NodeValue nodeValue = executeQuery(qb.getQueryString(), model, qb.getBindings());

		return nodeValue;
	}
	
	protected abstract NodeValue executeQuery(String query, Model model, QuerySolutionMap qsm);
}
