package at.ac.tuwien.shacl.sparql;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.shacl.registry.SHACLMetaModelRegistry;
import at.ac.tuwien.shacl.util.SHACLParsingException;
import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
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
 * This function implements sh:hasShape
 * Since the SHACL definition contains no current SPARQL definition of sh:hasShape,
 * a custom function is defined here.
 * This function will be removed, once a SPARQL definition is published.
 * 
 * @author xlin
 *
 */
public class HasShapeFunction implements Function, FunctionFactory {
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

		Argument	Value type	Description
		?arg1	Any IRI or blank node	The node to validate
		?arg2	sh:Shape or rdfs:Class	The shape to match against
		?arg3	rdfs:Resource	The URI of the current shapes graph (variable ?shapesGraph in constraints)
	
		This function must perform constraint validation equivalent to the validateNodeAgainstShape operation. 
		The function must return true if the operation returns no error-level constraint violations, 
		false if any error-level constraint violations exist.
	 */
	@Override
	public NodeValue exec(Binding binding, ExprList args, String uri,
			FunctionEnv env) {
        
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
        System.out.println("was in hasShape");
        System.out.println("arg2: "+evalArgs.get(1));
        
        Resource arg2 = model.getResource(evalArgs.get(1).asString());
        evalArgs.get(0);
        System.out.println("arg1 is: "+arg1);
        System.out.println("in hasShape");
        Model errorModel = null;
        System.out.println("still in hasShape");
		try {
			System.out.println("stil still in hasShape");
			errorModel = SHACLValidator.getValidator().validateNodeAgainstShape(arg1, arg2, model);
		
		} catch (SHACLParsingException e1) {
			e1.printStackTrace();
		}
		
		
        if(errorModel.isEmpty()) {
        	System.out.println("returned has shape result true");
            
        	return NodeValue.makeBoolean(true);
        } else {
        	System.out.println("returned has shape result false");
            
        	return NodeValue.makeBoolean(false);
        }
	}
}
