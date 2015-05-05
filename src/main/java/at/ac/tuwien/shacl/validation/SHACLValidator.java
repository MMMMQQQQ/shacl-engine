package at.ac.tuwien.shacl.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.model.Argument;
import at.ac.tuwien.shacl.model.ConstraintViolation;
import at.ac.tuwien.shacl.registry.SHACLMetaModelRegistry;
import at.ac.tuwien.shacl.sparql.QueryBuilder;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * Covers section 11, supported operations
 * @author xlin
 *
 */
public class SHACLValidator {
	private SHACLMetaModelRegistry registry;
	
	private Model model;
	
	public SHACLValidator(Model model) {
		this.registry = new SHACLMetaModelRegistry();
		this.model = model;
	}
	
	public Model validateGraph() {
		List<Statement> shapes = this.getInstantiatedShapeTriplets();
		Model errorModel = ModelFactory.createDefaultModel();
		System.out.println("number of shape data: "+shapes.size());
		for(Statement shape : shapes) {
			errorModel.add(this.validateNodeAgainstShape(shape.getSubject(), shape.getObject().asResource()));
		}
		
		return errorModel;
	}
	
	public Model validateNodeAgainstShape(Resource focusNode, Resource shape) {
		Model errorModel = ModelFactory.createDefaultModel();
		System.out.println("shape: "+shape);
		for(Statement p : shape.listProperties(SHACL.property).toList()) {
			errorModel.add(this.validateConstraint(focusNode, p.getObject().asResource()));
		}
		return errorModel;
	}
	
	public Model validateConstraint(Resource focusNode, Resource constraint) {
		Model errorModel = ModelFactory.createDefaultModel();
		Set<String> propertyConstraints = registry.getConstraintsURIs();
		System.out.println("constraint: "+constraint.getProperty(SHACL.predicate).getObject());
		Map<String, RDFNode> tempStore = new HashMap<String, RDFNode>();
	
		//add sh:predicate
		Statement constraintPredicate = constraint.getProperty(SHACL.predicate);
		tempStore.put(constraintPredicate.getPredicate().getURI(), constraintPredicate.getObject().asResource());

		for(Statement objectProps : constraint.listProperties().toList()) {
//			System.out.println("objectProps: "+objectProps.getObject());
			RDFNode res = objectProps.getObject();
			
			Property predicate = objectProps.getPredicate();
			
			if(!predicate.getURI().equals(SHACL.predicate.getURI())) {
				tempStore.put(predicate.getURI(), res);

				if(propertyConstraints.contains(predicate.getURI())) {
					QueryBuilder qb = new QueryBuilder(
							registry.getConstraintTemplate(predicate.getURI()).getExecutableBody(),
							model.getNsPrefixMap());
					boolean isComplete = true;
					
					for(Argument a : registry.getConstraintTemplate(predicate.getURI()).getArguments()) {
						if(tempStore.containsKey(a.getPredicate().getURI())) {
							qb.addBinding(a.getPredicate().getLocalName(), tempStore.get(a.getPredicate().getURI()));
						} else {
							isComplete = false;
							break;
						}
					}
					
					if(isComplete) {
						qb.addBinding(SHACL.predicate.getLocalName(), tempStore.get(SHACL.predicate.getURI()));
						qb.addThisBinding(focusNode);

						if(!SPARQLQueryExecutor.isQueryValid(qb.getQueryString(), model, qb.getBindings())) {
							ConstraintViolation error = new ConstraintViolation(
									SHACL.Error, focusNode, focusNode, 
									ResourceFactory.createProperty(((Resource)tempStore.get(SHACL.predicate.getURI())).getURI()), 
									null);
							errorModel.add(error.getModel());
						}
					}
				}
			}
		}
		
		return errorModel;
	}

	public Model validateNode(Resource focusNode) {
		return null;
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
		
		statements.addAll(model.listStatements(null, SHACL.nodeShape, (RDFNode)null).toList());
		System.out.println(model.listStatements().toList());
		//TODO implement rdf:type
		return statements;
	}
}
