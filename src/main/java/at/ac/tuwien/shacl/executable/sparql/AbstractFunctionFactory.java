package at.ac.tuwien.shacl.executable.sparql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.shacl.registry.SHACLModelRegistry;
import at.ac.tuwien.shacl.vocabulary.SHACL;

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

/**
 * Defines the basic functionality of a SPARQL function and implements a FunctionFactory
 * for registration in ARQ.
 * 
 * @author xlin
 *
 */
public abstract class AbstractFunctionFactory implements FunctionFactory, Function {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public AbstractFunctionFactory() {
		initBuiltinFunctions();
	}
	
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
		log.debug("executing function "+uri);
		
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

		String query = SHACLModelRegistry.get().getFunction(uri).getExecutableBody(SHACL.sparql);
		Map<String, RDFNode> variables = new HashMap<String, RDFNode>();
		
		int i = 1;

		for(NodeValue node : nodeValues) {
			String arg = "arg"+i;

			if(node.isLiteral()) {
				variables.put(arg, ResourceFactory.createTypedLiteral(node.asNode().getLiteral().getIndexingValue()));
			} else {
				variables.put(arg, model.getRDFNode(node.asNode()));
			}
			
			i++;
		}
		log.debug("processing function "+uri);
		NodeValue nodeValue = executeQuery(query, model, variables);

		log.debug("returned sparql result: "+nodeValue+ " uri: "+uri);
		return nodeValue;
	}
	
	protected abstract NodeValue executeQuery(String query, Model model, Map<String, RDFNode> bindings);
	
	private static void initBuiltinFunctions() {
		
	}
}
