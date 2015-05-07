package at.ac.tuwien.shacl.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.model.impl.ArgumentImpl;
import at.ac.tuwien.shacl.model.impl.ConstraintViolationImpl;
import at.ac.tuwien.shacl.registry.ModelRegistry;
import at.ac.tuwien.shacl.registry.SHACLMetaModelRegistry;
import at.ac.tuwien.shacl.sparql.QueryBuilder;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Covers section 11, supported operations
 * @author xlin
 *
 */
public class SHACLValidator {
	private SHACLMetaModelRegistry registry;
	
	//TODO change
	private Model model;
	
	public SHACLValidator(Model model) {
		this.registry = SHACLMetaModelRegistry.getRegistry();
		this.model = model;
		ModelRegistry.setCurrentModel(model);
	}
	
	public Model validateGraph() {
		List<Statement> shapes = this.getInstantiatedShapeTriplets();
		Model errorModel = ModelFactory.createDefaultModel();
		System.out.println("number of shape data: "+shapes.size());
		for(Statement shape : shapes) {
			System.out.println();
			//errorModel.add(this.validateNodeAgainstShape(shape.getSubject(), shape.getObject().asResource()));
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

			RDFNode res = objectProps.getObject();
			
			Property predicate = objectProps.getPredicate();
			
			if(!predicate.getURI().equals(SHACL.predicate.getURI())) {
				tempStore.put(predicate.getURI(), res);

				if(propertyConstraints.contains(predicate.getURI())) {
					QueryBuilder qb = new QueryBuilder(
							registry.getPropertyConstraintTemplate(predicate.getURI()).getExecutableBody(),
							model.getNsPrefixMap());
					boolean isComplete = true;
					for(ArgumentImpl a : registry.getPropertyConstraintTemplate(predicate.getURI()).getArguments()) {
						if(tempStore.containsKey(a.getPredicate().getURI())) {
							qb.addBinding(a.getPredicate().getLocalName(), tempStore.get(a.getPredicate().getURI()));
							
						} else {
							if(!a.isOptional()) {
								isComplete = false;
								break;
							}
						}
					}
					if(isComplete) {
						qb.addBinding(SHACL.predicate.getLocalName(), tempStore.get(SHACL.predicate.getURI()));
						qb.addThisBinding(focusNode);
						if(!SPARQLQueryExecutor.isQueryValid(qb.getQueryString(), model, qb.getBindings())) {
							ConstraintViolationImpl error = new ConstraintViolationImpl(
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
		//statements.addAll(model.listStatements(null, RDF.type, (RDFNode)null).toList());
		System.out.println("data triplets: "+model.listStatements().toList());
		//TODO implement rdf:type
		return statements;
	}
}
