package at.ac.tuwien.shacl.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.arq.functions.AskFunctionFactory;
import at.ac.tuwien.shacl.arq.functions.SelectFunctionFactory;
import at.ac.tuwien.shacl.model.Argument;
import at.ac.tuwien.shacl.model.ConstraintTemplate;
import at.ac.tuwien.shacl.model.Function;
import at.ac.tuwien.shacl.sparql.QueryBuilder;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.util.SHACLResourceBuilder;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.function.FunctionRegistry;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Register every known constraint and (built-in) function of the SHACL definition model 
 * 
 * @author xlin
 *
 */
public class SHACLMetaModelRegistry {
	private static SHACLMetaModelRegistry registry;
	
	public static SHACLMetaModelRegistry getInstance() {
		if(registry == null) {
			registry = new SHACLMetaModelRegistry();
		}
		
		return registry;
	}
	
	private Map<String, ConstraintTemplate> constraintTemplates;
	
	private Map<String, Function> functions;
	
	public SHACLMetaModelRegistry() {
		this.constraintTemplates = new HashMap<String, ConstraintTemplate>();
		this.functions = new HashMap<String, Function>();
		this.register(SHACL.getModel());
	}
	
	public void register(Model model) {
		this.registerPropertyConstraints(model);
		this.registerFunctions(model);
	}
	
	public Set<String> getConstraintsURIs() {
		return this.constraintTemplates.keySet();
	}
	
	public ConstraintTemplate getConstraintTemplate(String constraintURI) {
		return this.constraintTemplates.get(constraintURI);
	}
	
	public void registerFunctions(Model model) {
		List<Statement> pcStatements = model.listStatements(
				null, RDF.type, SHACL.Function).toList();
		
		//extract values for property constraint
		for(Statement s : pcStatements) {
			//properties of each function definition
			List<Statement> properties = s.getSubject().asResource().listProperties().toList();
			//System.out.println("function: "+s.getSubject());

			//populate function object
			Function function = SHACLResourceBuilder.build(properties, new Function());
			
			//TODO remove constraint eventually
			if(function.getExecutableBody() != null) {
				functions.put(s.getSubject().getURI(), function);
				QueryBuilder q = new QueryBuilder(function.getExecutableBody(), model.getNsPrefixMap());
				if(SPARQLQueryExecutor.isAskQuery(q.getQueryString())) {
					FunctionRegistry.get().put(s.getSubject().getURI(), new AskFunctionFactory());
				} else if(SPARQLQueryExecutor.isSelectQuery(q.getQueryString())) {
					FunctionRegistry.get().put(s.getSubject().getURI(), new SelectFunctionFactory());
				} else {
					//TODO throw exception
				}
				
			}
		}
	}
	
	public Function getFunction(String uri) {
		return this.functions.get(uri);
	}
	
	public void registerPropertyConstraints(Model model) {
		//get all meta model nodes of property constraints
		List<Statement> pcStatements = model.listStatements(
				SHACL.PropertyConstraint, RDFS.subClassOf, (RDFNode) null).toList();
		
		//extract values for property constraint
		for(Statement s : pcStatements) {
			//properties of each constraint definition
			List<Statement> properties = s.getObject().asResource().listProperties().toList();
			
			//populate property constraint object
			ConstraintTemplate propertyConstraint = (ConstraintTemplate)SHACLResourceBuilder.build(properties, new ConstraintTemplate());
			for(Argument argument : propertyConstraint.getArguments()) {
				if(!argument.getPredicate().getURI().equals(SHACL.predicate.getURI())) {
					constraintTemplates.put(argument.getPredicate().getURI(), propertyConstraint);
				}
			}
		}
	}
}
