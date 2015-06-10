package at.ac.tuwien.shacl.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.metamodel.Argument;
import at.ac.tuwien.shacl.metamodel.ConstraintTemplate;
import at.ac.tuwien.shacl.metamodel.Function;
import at.ac.tuwien.shacl.model.impl.ConstraintTemplateImpl;
import at.ac.tuwien.shacl.model.impl.FunctionImpl;
import at.ac.tuwien.shacl.sparql.AskFunctionFactory;
import at.ac.tuwien.shacl.sparql.HasShapeFunction;
import at.ac.tuwien.shacl.sparql.QueryBuilder;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.sparql.SelectFunctionFactory;
import at.ac.tuwien.shacl.util.GraphTraverser;
import at.ac.tuwien.shacl.util.SHACLParsingException;
import at.ac.tuwien.shacl.util.SHACLResourceBuilder;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
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
	
	public static SHACLMetaModelRegistry getRegistry() {
		if(registry == null) {
			registry = new SHACLMetaModelRegistry();
		}
		
		return registry;
	}
	
	//key: uri of template, value: constraintTemplate
	private Map<String, ConstraintTemplate> templates;
	
	//all registered property constraints
	//key: name of property name, value: uri of template
	private Map<String, String> propertyPredicates;
	
	//all registered inverse property constraints
	//key: name of inverse property name, value: uri of template
	private Map<String, String> inversePropertyPredicates;

	//all defined functions of the meta model and potential user defined functions
	private Map<String, Function> functions;
	
	private Set<String> generalConstraints;
	
	public SHACLMetaModelRegistry() {
		this.templates = new HashMap<String, ConstraintTemplate>();
		this.functions = new HashMap<String, Function>();
		this.propertyPredicates = new HashMap<String, String>();
		this.inversePropertyPredicates = new HashMap<String, String>();
		this.generalConstraints = new HashSet<String>();
		this.register(SHACL.getModel());
	}
	
	private void register(Model model) {
		this.registerTemplates(model);
		this.registerFunctions(model);
	}
	
	public Set<String> getInversePropertyConstraintsURIs() {
		return this.inversePropertyPredicates.keySet();
	}
	
	public Set<String> getPropertyConstraintsURIs() {
		return this.propertyPredicates.keySet();
	}
	
	public ConstraintTemplate getPropertyConstraintTemplate(String constraintUri) {
		String templateUri = this.propertyPredicates.get(constraintUri);
		return templates.get(templateUri);
	}
	
	public ConstraintTemplate getInversePropertyConstraintTemplate(String constraintUri) {
		String templateUri = this.inversePropertyPredicates.get(constraintUri);
		return templates.get(templateUri);
	}
	
	public ConstraintTemplate getGeneralConstraintTemplate(String constraintUri) {
		//throw exception, if unknown in generalConstraints
		return templates.get(constraintUri);
	}

	private void registerTemplates(Model model) {
		this.registerCoreConstraints(model);
	}
	
	/**
	 * Register all constraints of the SHACL core
	 * 
	 * @param model
	 */
	//FIXME sh:NotConstraint, sh:AndConstraint, sh:OrConstraint, sh:XorConstraint and sh:valueShape 
	//do not work since they require sh:hasShape which is not defined yet in sparql
	//(see registerFunctions method)
	private void registerCoreConstraints(Model model) {		
		//get all constraint template nodes
		List<Resource> constraintTemplates = model.listSubjectsWithProperty(RDF.type, SHACL.ConstraintTemplate).toList();
		
		//extract values for each constraint template (each SHACL constraint is of type ConstraintTemplate)
		for(Resource rConstraintTempl : constraintTemplates) {
			List<Statement> properties = rConstraintTempl.listProperties().toList();
			////System.out.println("*****");
			////System.out.println("properties: "+properties);
			////System.out.println("template uri: "+r.getURI());
			
			ConstraintTemplate constraintTemplate = null;
			try {
				constraintTemplate = (ConstraintTemplateImpl) SHACLResourceBuilder.build(
						properties, new ConstraintTemplateImpl());
			} catch (SHACLParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//add template, if it's not already contained in template list and 
			//if it has an executable body
			if(!templates.containsKey(rConstraintTempl.getURI()) && constraintTemplate.getExecutableBody() != null) {
				templates.put(rConstraintTempl.getURI(), constraintTemplate);
			}

			//add constraint to map, if template is a (inverse) property constraint
			if(this.isSuperclass(rConstraintTempl, SHACL.PropertyConstraint, model)) {
				for(Argument a : constraintTemplate.getArguments()) {
					//argument sh:predicate is built from superclass
					//add property name to constraint list, if it's not sh:predicate
					if(!a.getPredicate().equals(SHACL.predicate)) {
						this.propertyPredicates.put(a.getPredicate().getURI(), rConstraintTempl.getURI());
						//System.out.println("property constraint added: "+a.getPredicate().getURI());
					}
				}
			} else if(this.isSuperclass(rConstraintTempl, SHACL.InversePropertyConstraint, model)) {
				for(Argument a : constraintTemplate.getArguments()) {
					if(!a.getPredicate().equals(SHACL.predicate)) {
						this.inversePropertyPredicates.put(a.getPredicate().getURI(), rConstraintTempl.getURI());
						//System.out.println("inverse property constraint added: "+a.getPredicate().getURI());
					}
				}
			} else if(rConstraintTempl.getProperty(RDFS.subClassOf).getObject().equals(SHACL.TemplateConstraint) &&
					rConstraintTempl.getProperty(SHACL.abstract_) == null) {
				this.generalConstraints.add(rConstraintTempl.getURI());
				//System.out.println("general constraint added: " + rConstraintTempl.getURI());
			}
		}
	}
	
	/**
	 * Check, if the superclass is indeed a superclass of subclass
	 * 
	 * @param superclass
	 * @param subclass
	 * @return
	 */
	private boolean isSuperclass(Resource superclass, Resource subclass, Model model) {
		List<Statement> superclasses = GraphTraverser.listDirectSuperclassesOfNodeAsObject(subclass, model);
		boolean isSuperclass = false;
		
		for(Statement s : superclasses) {
			if(s.getObject().asResource().getURI().equals(superclass.getURI())) {
				isSuperclass = true;
				break;
			}
		}
			
		return isSuperclass;
	}

	public void registerFunctions(Model model) {
		
		List<Statement> pcStatements = model.listStatements(
				null, RDF.type, SHACL.Function).toList();
		
		//extract values for property constraint
		for(Statement s : pcStatements) {
			//properties of each function definition

			List<Statement> properties = s.getSubject().asResource().listProperties().toList();
			////System.out.println("function: "+s.getSubject());

			//populate function object
			FunctionImpl function = null;
			try {
				function = SHACLResourceBuilder.build(properties, new FunctionImpl());
			} catch (SHACLParsingException e) {
				e.printStackTrace();
			}

			if(function.getExecutableBody() != null) {
				functions.put(s.getSubject().getURI(), function);
				QueryBuilder q = new QueryBuilder(function.getExecutableBody(), model.getNsPrefixMap());
				if(SPARQLQueryExecutor.isAskQuery(q.getQueryString())) {
					FunctionRegistry.get().put(s.getSubject().getURI(), new AskFunctionFactory());
				} else if(SPARQLQueryExecutor.isSelectQuery(q.getQueryString())) {
					FunctionRegistry.get().put(s.getSubject().getURI(), new SelectFunctionFactory());
				}
			} else if(s.getSubject().equals(SHACL.hasShape)) {
				FunctionRegistry.get().put(s.getSubject().getURI(), new HasShapeFunction());
			}
		}
	}
	
	public Function getFunction(String uri) {
		return this.functions.get(uri);
	}
}
