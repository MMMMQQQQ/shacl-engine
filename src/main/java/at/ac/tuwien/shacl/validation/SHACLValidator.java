package at.ac.tuwien.shacl.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.core.model.ConstraintSet;
import at.ac.tuwien.shacl.core.model.NativeConstraint;
import at.ac.tuwien.shacl.core.model.PropertyConstraint;
import at.ac.tuwien.shacl.metamodel.Argument;
import at.ac.tuwien.shacl.metamodel.ConstraintTemplate;
import at.ac.tuwien.shacl.model.impl.ConstraintViolationImpl;
import at.ac.tuwien.shacl.registry.ModelRegistry;
import at.ac.tuwien.shacl.registry.SHACLMetaModelRegistry;
import at.ac.tuwien.shacl.sparql.QueryBuilder;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.util.SHACLResourceBuilder;
import at.ac.tuwien.shacl.util.ValueInjector;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.ResultSetGraphVocab;
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
		System.out.println("--------------initializing done");
	}
	
	/*
	 * This operation validates a whole graph against all shapes associated with its resources, 
	 * based on in-graph mappings.
	 * 
	 * ?minSeverity	rdfs:Class	The minimum severity class, e.g. sh:Error specifying which constraints to exclude/include.
	 */
	public Model validateGraph() {
		List<ShapeInstanceMap> shapes = this.getInstantiatedShapes();
		Model errorModel = ModelFactory.createDefaultModel();
		System.out.println("number of shape data: "+shapes.size());
		
		//TODO implement graph-level constraints
		for(ShapeInstanceMap shape : shapes) {
			errorModel.add(this.validateNodeAgainstShape(shape.getInstance(), shape.getShape()));
		}

		errorModel.write(System.out, "Turtle");

		return errorModel;
	}
	
	/* This operation validates a single node against all constraints associated with a given shape.

		Argument	Type	Description
		?focusNode	rdfs:Resource	The focus node to validate
		?shape	sh:Shape	The shape that has the constraints.
		?minSeverity	rdfs:Class	The minimum severity class, e.g. sh:Error specifying which constraints to exclude/include.
	 */
	public Model validateNodeAgainstShape(Resource focusNode, Resource shape) {
		
		Model errorModel = ModelFactory.createDefaultModel();
		for(Statement p : shape.listProperties(SHACL.property).toList()) {
			System.out.println("validating");
			if(p.getObject().asResource().getProperty(RDF.type) == null || 
					p.getObject().asResource().getProperty(RDF.type).getObject().asResource().equals(SHACL.PropertyConstraint)) {
				System.out.println("focus node: "+focusNode + " object: "+p.getObject().asResource());
				errorModel.add(this.validatePropertyConstraint(focusNode, p.getObject().asResource(), false));
				System.out.println("res: "+p.getObject().asResource());
			} else {
				//TODO invalid constraint -> create fatal error and end validation
			}
		}
		for(Statement s : shape.listProperties(SHACL.inverseProperty).toList()) {
			if(s.getObject().asResource().getProperty(RDF.type) == null || 
					s.getObject().asResource().getProperty(RDF.type).getObject().asResource().getURI()
					.equals(SHACL.PropertyConstraint.getURI())) {
				errorModel.add(this.validatePropertyConstraint(focusNode, s.getObject().asResource(), true));
			} else {
				//TODO invalid constraint -> create fatal error and end validation
			}
		}
		for(Statement c : shape.listProperties(SHACL.constraint).toList()) {
			System.out.println("constraint triple: "+c);
			System.out.println(c.getObject().asResource().getProperty(RDF.type));
			
			//check, if it's a native constraint
			//native constraint are either unmarked or have rdf:type sh:NativeConstraint
			if(c.getObject().asResource().getProperty(RDF.type) == null || 
					c.getObject().asResource().getProperty(RDF.type).getObject().asResource().equals(SHACL.NativeConstraint)) {
				errorModel.add(this.validateNativeConstraint(focusNode, c.getObject().asResource()));
			} //else if(c.getObject().asResource())
			
		}
		
		return errorModel;
	}
	
//	private boolean isObjectConstraintType(Statement s, Resource constraintType) {
//		
//	}
	
	/* This operation evaluates a single constraint and produces constraint violations.

		Argument	Type	Description
		?constraint	sh:Constraint	The constraint to evalate
		?focusNode	rdfs:Resource (Optional)	The focus node, if present.
		This operation assumes that the ?constraint is either a native constraint or a template constraint.
	
		For native constraints, the operation selects one of the provided executable bodies. 
		For example if the constraint has a sh:sparql query and the engine is capable of executing SPARQL,
		it should follow the execution rules specified in the SPARQL-based Constraints section, 
		using the provided ?focusNode if present. A sh:Warning should be produced if no suitable executable body was found.
	
		For template constraints, the operation traverses the associated template as well as all its superclasses. 
		For each of those templates, it selects the best suitable executable body. 
		For example, if the engine is capable of executing SPARQL, it should follow the execution rules specified 
		in the SPARQL-based Templates section, using the provided ?focusNode if present. 
		A sh:Warning should be produced if no suitable executable body was found.
	 */
//	public Model validateConstraint(Resource focusNode, Resource constraint) {
//		Model errorModel = ModelFactory.createDefaultModel();
//		Set<String> propertyConstraints = registry.getConstraintsURIs();
//		System.out.println("constraint: "+constraint.getProperty(SHACL.predicate).getObject());
//		Map<String, RDFNode> tempStore = new HashMap<String, RDFNode>();
//	
//		//add sh:predicate
//		Statement constraintPredicate = constraint.getProperty(SHACL.predicate);
//		tempStore.put(constraintPredicate.getPredicate().getURI(), constraintPredicate.getObject().asResource());
//
//		for(Statement objectProps : constraint.listProperties().toList()) {
//			RDFNode res = objectProps.getObject();
//			
//			Property predicate = objectProps.getPredicate();
//			
//			if(!predicate.getURI().equals(SHACL.predicate.getURI())) {
//				tempStore.put(predicate.getURI(), res);
//
//				if(propertyConstraints.contains(predicate.getURI())) {
//					QueryBuilder qb = new QueryBuilder(
//							registry.getPropertyConstraintTemplate(predicate.getURI()).getExecutableBody(),
//							model.getNsPrefixMap());
//					boolean isComplete = true;
//					for(ArgumentImpl a : registry.getPropertyConstraintTemplate(predicate.getURI()).getArguments()) {
//						if(tempStore.containsKey(a.getPredicate().getURI())) {
//							qb.addBinding(a.getPredicate().getLocalName(), tempStore.get(a.getPredicate().getURI()));
//							
//						} else {
//							if(!a.isOptional()) {
//								isComplete = false;
//								break;
//							}
//						}
//					}
//					if(isComplete) {
//						qb.addBinding(SHACL.predicate.getLocalName(), tempStore.get(SHACL.predicate.getURI()));
//						qb.addThisBinding(focusNode);
//						if(!SPARQLQueryExecutor.isQueryValid(qb.getQueryString(), model, qb.getBindings())) {
//							ConstraintViolationImpl error = new ConstraintViolationImpl(
//									SHACL.Error, focusNode, focusNode, 
//									ResourceFactory.createProperty(((Resource)tempStore.get(SHACL.predicate.getURI())).getURI()), 
//									null);
//							errorModel.add(error.getModel());
//						}
//					}
//				}
//			}
//		}
//		
//		return errorModel;
//	}
	
	/**
	 * Validates a native constraint.
	 * A native constraint is represented in a sh:constraint node
	 * 
	 * @param focusNode
	 * @param constraint
	 * @return
	 */
	private Model validateNativeConstraint(Resource focusNode, Resource constraint) {
		Model errorModel = ModelFactory.createDefaultModel();
		
		NativeConstraint nativeConstraint = SHACLResourceBuilder.build(constraint.listProperties().toList(), new NativeConstraint());

		QueryBuilder qb = new QueryBuilder(
				nativeConstraint.getExecutableBody(),
				model.getNsPrefixMap());
		qb.addThisBinding(focusNode);
		//qb.addShapesGraphBinding(model.);
		System.out.println("queryString: "+nativeConstraint.getExecutableBody());
		if(!SPARQLQueryExecutor.isQueryValid(qb.getQueryString(), model, qb.getBindings())) {
			ConstraintViolationImpl violation = new ConstraintViolationImpl(
					nativeConstraint.getSeverity(), focusNode, focusNode, 
					nativeConstraint.getPredicate(), 
					null);
			violation.addMessage(nativeConstraint.getMessage());
			errorModel.add(violation.getModel());
		}
		
		return errorModel;
	}
	
	/**
	 * Validates a property constraint.
	 * A property constraint is represented in a sh:property node
	 * 
	 * @param focusNode
	 * @param constraint
	 * @return
	 */
	private Model validatePropertyConstraint(Resource focusNode, Resource constraint, boolean isInverse) {
		Model errorModel = ModelFactory.createDefaultModel();
		
		PropertyConstraint propertyConstraint = SHACLResourceBuilder.build(constraint.listProperties().toList(), new PropertyConstraint(), false);
		System.out.println("constraints:"+propertyConstraint.getPredicate());
		Map<Property, RDFNode> constraints = propertyConstraint.getConstraints();
		Set<ConstraintSet> constraintSets = new HashSet<ConstraintSet>();
		
		for(Map.Entry<Property, RDFNode> p : constraints.entrySet()) {
				ConstraintTemplate template;
				
				if(!isInverse) {
					template = registry.getPropertyConstraintTemplate(p.getKey().getURI());
				} else {
					template = registry.getInversePropertyConstraintTemplate(p.getKey().getURI());
				}
						
				boolean constraintSetExists = false;
				
				for(ConstraintSet c : constraintSets) {
					if(c.getTemplate() == template) {
						c.addConstraint(p.getKey(), p.getValue());
						constraintSetExists = true;
						break;
					}
				}
				
				if(!constraintSetExists) {
					ConstraintSet c = new ConstraintSet(template);
					c.addConstraint(p.getKey(), p.getValue());
					c.addConstraint(SHACL.predicate, propertyConstraint.getPredicate());
					constraintSets.add(c);
				}
		}

		for(ConstraintSet cs : constraintSets) {
			boolean isComplete = false;
			//fill value from template definition, if a default value is defined and the constraint does not have a value
			if(!cs.isComplete()) {
				for(Map.Entry<Property, RDFNode> c : cs.getConstraints().entrySet()) {
					if(c.getValue() == null) {
						for(Argument a : cs.getTemplate().getArguments()) {
							if(a.getPredicate().equals(c.getKey())) {
								if(a.getDefaultValue() != null) {
									c.setValue(a.getDefaultValue());
									isComplete = true;
								} else if(!a.isOptional()) {
									//TODO create constraint violation
								} else if(a.isOptional()) {
									isComplete = true;
								}
							}
						}
					}
				}
			} else {
				isComplete = true;
			}
			
			if(isComplete) {
				QueryBuilder qb = new QueryBuilder(cs.getTemplate().getExecutableBody(), model.getNsPrefixMap());
				qb.addThisBinding(focusNode);
				for(Map.Entry<Property, RDFNode> c : cs.getConstraints().entrySet()) {
					if(c.getValue() != null) {
						qb.addBinding(c.getKey().getLocalName(), c.getValue());
					}
				}
				
				Map<String, Object> result = SPARQLQueryExecutor.getMapResultsForQuery(qb.getQueryString(), model, qb.getBindings());

				if(result.size() > 0) {
					ConstraintViolationImpl violation = new ConstraintViolationImpl(
							((ConstraintTemplate)cs.getTemplate()).getSeverity(), focusNode, focusNode, 
							propertyConstraint.getPredicate(), 
							null);
					
					String message = ValueInjector.inject(((ConstraintTemplate)cs.getTemplate()).getMessage(), result);
					violation.addMessage(message);
					
					
					errorModel.add(violation.getModel());

				}
			}
		}
		
		return errorModel;
	}

	/* This operation validates a single node against all shapes associated with it, based on in-graph mappings.

		Argument	Type	Description
		?focusNode	rdfs:Resource	The focus node to validate
		?minSeverity	rdfs:Class	The minimum severity class, e.g. sh:Error specifying which constraints to exclude/include.
	*/
	public Model validateNode(Resource focusNode) {
		return null;
	}
	
	private class ShapeInstanceMap {
		private Resource shape;
		private Resource instance;
		
		public ShapeInstanceMap(Resource shape, Resource instance) {
			this.shape = shape;
			this.instance = instance;
		}
		
		public Resource getShape() {
			return this.shape;
		}
		
		public Resource getInstance() {
			return this.instance;
		}
	}

	/**
	 *  Determine which nodes need to be evaluated against which shapes.
	 *  There are three shape selection properties currently: sh:nodeShape, sh:scopeClass and rdf:type
	 *  (Section 11.1)
	 */
	public List<ShapeInstanceMap> getInstantiatedShapes() {
		List<ShapeInstanceMap> shapeInstances = new ArrayList<ShapeInstanceMap>();

		for(Statement shapeS : model.listStatements(null, RDF.type, SHACL.Shape).toList()) {
			for(Statement s : model.listStatements(null, SHACL.nodeShape, shapeS.getSubject()).toList()) {
				shapeInstances.add(new ShapeInstanceMap(s.getObject().asResource(), s.getSubject()));
				System.out.println("added shape");
			}
			
			for(Statement s : model.listStatements(null, RDF.type, shapeS.getSubject()).toList()) {
				shapeInstances.add(new ShapeInstanceMap(s.getObject().asResource(), s.getSubject()));
			}

			if(shapeS.getSubject().getProperty(SHACL.scopeClass) != null) {
				Resource scopeClass = shapeS.getSubject().getProperty(SHACL.scopeClass).getResource();
				if(scopeClass != null) {
					List<Statement> scopeStatements = model.listStatements(null, RDF.type, scopeClass).toList();
					for(Statement s : scopeStatements) {
						shapeInstances.add(new ShapeInstanceMap(shapeS.getSubject(), s.getSubject()));
					}
				}
			}
		}
		
		for(Statement shapeS : model.listStatements(null, RDF.type, SHACL.ShapeClass).toList()) {
			for(Statement s : model.listStatements(null, RDF.type, shapeS.getSubject()).toList()) {
				shapeInstances.add(new ShapeInstanceMap(s.getObject().asResource(), s.getSubject()));
			}
		}
		
		return shapeInstances;
	}
}
