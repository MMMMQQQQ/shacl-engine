package at.ac.tuwien.shacl.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.sparql.querying.MissingBindingException;
import at.ac.tuwien.shacl.sparql.querying.SPARQLHasValue;
import at.ac.tuwien.shacl.sparql.querying.SPARQLQueryConstraint;
import at.ac.tuwien.shacl.vocabulary.SHACL;
import at.ac.tuwien.shacl.vocabulary.SelectionProperty;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class SPARQLValidator {
	private Model model;
	
	public SPARQLValidator(Model model) {
		this.model = model;
	}
	
	public void validate(SPARQLQueryConstraint sqc, Model model) throws MissingBindingException {
		SPARQLQueryExecutor executor = new SPARQLQueryExecutor();
		executor.isQueryValid(sqc.getBaseQuery(), sqc.getBindings(), model);
	}

	public Model validateNode(Resource resource, Property property,
			RDFNode... nodes) {
		// TODO Auto-generated method stub
		return null;
	}

	/*public Model validateAll(Model model) {
		Model errorModel = ModelFactory.createDefaultModel();
		
		for(Statement s : model.listStatements(null, SHACL.nodeShape, (RDFNode)null).toList()) {
			Set<Resource> shapes = new HashSet<Resource>();

			addSuperClasses(s.getResource(), shapes);
			for(Resource sh : shapes) {
				for(Statement propertyS : sh.listProperties(SHACL.property).toList()) {
					// Property constraints
					Statement predicateS = propertyS.getResource().getProperty(SHACL.predicate);

					Statement hasValueS = propertyS.getResource().getProperty(SHACL.hasValue);
					if(hasValueS != null) {
						HashMap<String, RDFNode> variables = new HashMap<String, RDFNode>();
						variables.put("this", s.getSubject());
						variables.put("predicate", model.getProperty(predicateS.getResource().getURI()));
						variables.put("hasValue", hasValueS.getObject());
						
						try {
							SPARQLHasValue hvc = new SPARQLHasValue();
							hvc.createBindings(variables);
							
							SPARQLQueryExecutor executor = new SPARQLQueryExecutor();
							boolean isValid;
							
							isValid = executor.isQueryValid(hvc.getBaseQuery(), hvc.getBindings(), model);
						
							if(!isValid) {
								Resource error = errorModel.createResource(SHACL.Error);
								error.addProperty(SHACL.root, s.getSubject());
								error.addProperty(SHACL.path, model.getProperty(predicateS.getResource().getURI()));
								error.addProperty(SHACL.message, "Missing required value " + hasValueS);
							}
						} catch (MissingBindingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
				}
			}
		}
		
		return errorModel;
	}*/
	
	public Model validateAll() {
		Model errorModel = ModelFactory.createDefaultModel();
		
		List<Statement> shapeStatements = this.getInstantiatedShapeTriplets();
		
		for(Statement s : shapeStatements) {
			Resource resource = s.getResource();
			
		}
		
		for(Statement s : model.listStatements(null, SHACL.nodeShape, (RDFNode)null).toList()) {
			Set<Resource> shapes = new HashSet<Resource>();

			addSuperClasses(s.getResource(), shapes);
			for(Resource sh : shapes) {
				for(Statement propertyS : sh.listProperties(SHACL.property).toList()) {
					// Property constraints
					Statement predicateS = propertyS.getResource().getProperty(SHACL.predicate);

					Statement hasValueS = propertyS.getResource().getProperty(SHACL.hasValue);
					if(hasValueS != null) {
						HashMap<String, RDFNode> variables = new HashMap<String, RDFNode>();
						variables.put("this", s.getSubject());
						variables.put("predicate", model.getProperty(predicateS.getResource().getURI()));
						variables.put("hasValue", hasValueS.getObject());
						
						try {
							SPARQLHasValue hvc = new SPARQLHasValue();
							hvc.createBindings(variables);
							
							SPARQLQueryExecutor executor = new SPARQLQueryExecutor();
							boolean isValid;
							
							isValid = executor.isQueryValid(hvc.getBaseQuery(), hvc.getBindings(), model);
						
							if(!isValid) {
								Resource error = errorModel.createResource(SHACL.Error);
								error.addProperty(SHACL.root, s.getSubject());
								error.addProperty(SHACL.path, model.getProperty(predicateS.getResource().getURI()));
								error.addProperty(SHACL.message, "Missing required value " + hasValueS);
							}
						} catch (MissingBindingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
				}
			}
		}
		
		return errorModel;
	}
	
	/**
	 *  Determine which nodes need to be evaluated against which shapes.
	 *  There are two shape selection properties currently: sh:nodeShape and rdf:type
	 *  (Section 11.1)
	 * 
	 * @return a list of statements (triplets) that will each contain:
	 * 		- the instance of the shape (i.e. the concrete data) as subject
	 * 		- the shape definition as object
	 * 		- the selection property (sh:nodeShape/rdf:type) as predicate
	 */
	private List<Statement> getInstantiatedShapeTriplets() {
		List<Statement> statements = new ArrayList<Statement>();
		
		for(SelectionProperty p : SelectionProperty.values()) {
			statements.addAll(model.listStatements(null, p.getProperty(), (RDFNode)null).toList());
		}
		
		return statements;
	}
	
//	
//	protected Model createErrorModel(RDFNode root, RDFNode) {
//		
//	}
//	
	private void addSuperClasses(Resource shape, Set<Resource> shapes) {
		shapes.add(shape);
		for(Statement s : shape.listProperties(RDFS.subClassOf).toList()) {
			System.out.println("in recursion");
			if(!shapes.contains(s.getResource())) {
				addSuperClasses(s.getResource(), shapes);
			}
		}
	}
}
