package at.ac.tuwien.shacl.executable.sparql;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
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
 * This function implements sh:hasShape
 * Since the SHACL definition contains no current SPARQL definition of sh:hasShape,
 * a custom function is defined here.
 * 
 * @author xlin
 *
 */
public class HasShapeFunction implements Function, FunctionFactory {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
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
	/**
	 * The function sh:hasShape returns true if	a given node (?arg1) matches a given shape (?arg2). 
	   The return type of this function is xsd:boolean.

		This function must perform constraint validation equivalent to the validateNodeAgainstShape operation. 
		The function must return true if the operation returns no error-level constraint violations, 
		false if any error-level constraint violations exist.
	 */
	@Override
	public NodeValue exec(Binding binding, ExprList args, String uri,
			FunctionEnv env) {
        
		log.debug("has shape function executed");
		
		if ( args == null) {
        	throw new ARQInternalErrorException("Null args list") ;
        } else if(args.size() != 3) {
        	throw new ExprEvalException("Missing arguments");
        }

        List<NodeValue> evalArgs = new ArrayList<>() ;
        
        for ( Expr e : args )
        {
            NodeValue x = e.eval( binding, env );
            evalArgs.add( x );
        }
        
        Model model = ModelFactory.createModelForGraph(env.getActiveGraph());
        
        Resource arg1 = model.getResource(evalArgs.get(0).asString());
        
        Resource arg2 = model.getResource(evalArgs.get(1).asString());
        evalArgs.get(0);

        Model errorModel = null;

		errorModel = SHACLValidator.getDefaultValidator().validateNodeAgainstShape(arg1, arg2, model);
		
        if(errorModel.isEmpty()) {
        	return NodeValue.makeBoolean(true);
        } else {
        	return NodeValue.makeBoolean(false);
        }
	}
}
