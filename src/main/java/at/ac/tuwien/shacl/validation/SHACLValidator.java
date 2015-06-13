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
import at.ac.tuwien.shacl.registry.NamedModels;
import at.ac.tuwien.shacl.registry.SHACLMetaModelRegistry;
import at.ac.tuwien.shacl.sparql.QueryBuilder;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;
import at.ac.tuwien.shacl.util.Config;
import at.ac.tuwien.shacl.util.SHACLParsingException;
import at.ac.tuwien.shacl.util.SHACLResourceBuilder;
import at.ac.tuwien.shacl.util.ValueInjector;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Covers section 11, supported operations
 * @author xlin
 *
 */
public class SHACLValidator {
	private ModelRegistry registry;

	private static SHACLValidator validator;
	
	private static NamedModels namedModels;

	//TODO refactor
	private Resource currentShape;
	
	public SHACLValidator() {
		validator = this;
		
		System.out.println("--------------initializing done");
	}
	
	/*
	 * This operation validates a whole graph against all shapes associated with its resources, 
	 * based on in-graph mappings.
	 * 
	 * ?minSeverity	rdfs:Class	The minimum severity class, e.g. sh:Error specifying which constraints to exclude/include.
	 */
	public Model validateGraph(Model model) throws SHACLParsingException {
		namedModels = new NamedModels();
		this.registry = new ModelRegistry(model);
		return this.validateGraph(Config.DEFAULT_NAMED_MODEL.getURI(), model);
	}
	
	public Model validateGraph(String uri, Model model) throws SHACLParsingException {
		namedModels.addNamedGraph(uri, model);
		namedModels.setDefaultModel(model);
		
		List<ShapeInstanceMap> shapes = this.getInstantiatedShapes(model);
		Model errorModel = ModelFactory.createDefaultModel();

		System.out.println("number of shape data: "+shapes.size());
		
		for(ShapeInstanceMap shape : shapes) {
			errorModel.add(this.validateNodeAgainstShape(shape.getInstance(), shape.getShape(), model));
		}

		System.out.println("*******constraint violations*******");
		if(errorModel.isEmpty()) {
			System.out.println("{}");
		} else {
			errorModel.write(System.out, "Turtle");
		}
		
		//namedModels.removeNamedGraph(uri);
		
		return errorModel;
	}
	
	/* This operation validates a single node against all constraints associated with a given shape.

		Argument	Type	Description
		?focusNode	rdfs:Resource	The focus node to validate
		?shape	sh:Shape	The shape that has the constraints.
		?minSeverity	rdfs:Class	The minimum severity class, e.g. sh:Error specifying which constraints to exclude/include.
	 */
	public Model validateNodeAgainstShape(Resource focusNode, Resource shape, Model model) throws SHACLParsingException {
		System.out.println("*****************************");
		
		Model errorModel = ModelFactory.createDefaultModel();
		currentShape = shape;
		
		for(Statement p : shape.listProperties(SHACL.property).toList()) {
			if(p.getObject().isResource()) {
				if(p.getObject().asResource().getProperty(RDF.type) == null || 
						p.getResource().getProperty(RDF.type).getObject().asResource().equals(SHACL.PropertyConstraint)) {
					errorModel.add(this.validatePropertyConstraint(focusNode, p.getObject().asResource(), model, false));
				}
			} else {
				throw new SHACLParsingException("sh:property must contain a constraint definition, but was " + p.getObject());
			}
				
		}
		for(Statement s : shape.listProperties(SHACL.inverseProperty).toList()) {
			if(s.getObject().isResource()) {
				if(s.getObject().asResource().getProperty(RDF.type) == null || 
						s.getObject().asResource().getProperty(RDF.type).getObject().asResource().getURI()
						.equals(SHACL.PropertyConstraint.getURI())) {
					errorModel.add(this.validatePropertyConstraint(focusNode, s.getObject().asResource(), model, true));
				}
			} else {
				throw new SHACLParsingException("sh:inverseProperty must contain a constraint definition, but was " + s.getObject());
			}
			
			
		}
		for(Statement c : shape.listProperties(SHACL.constraint).toList()) {
			if(c.getObject().isResource()) {
				//check, if it's a native constraint
				//native constraint are either unmarked or have rdf:type sh:NativeConstraint
				Statement stType = c.getObject().asResource().getProperty(RDF.type);
				
				if(stType == null || stType.getResource().equals(SHACL.NativeConstraint)) {
					errorModel.add(this.validateNativeConstraint(focusNode, c.getObject().asResource(), model));
				} else {
					ConstraintTemplate template = registry.getGeneralConstraintTemplate(stType.getResource().getURI());
					
					if(template != null) {
						errorModel.add(this.validateGeneralConstraint(focusNode, c.getObject().asResource(), model, template));
					} else {
						
					}
					//TODO implement template constraints here
					//throw new SHACLParsingException("Unrecognized constraint type " + c.getObject().asResource().getProperty(RDF.type).getObject());
				}
			} else {
				throw new SHACLParsingException("sh:constraint type unrecognized" + c.getObject());
			}
			
			
			
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
//	}
	
	private Model validateGeneralConstraint(Resource focusNode, Resource constraint, Model model, ConstraintTemplate template) throws SHACLParsingException {
		Model errorModel = ModelFactory.createDefaultModel();
				
		QueryBuilder qb = new QueryBuilder(
				template.getExecutableBody(),
				model.getNsPrefixMap());
		qb.addPreBinding(focusNode, Config.DEFAULT_NAMED_MODEL, currentShape);
		
		for(Argument a : template.getArguments()) {
			System.out.println("template arguments: "+a.getPredicate());
			if(!a.getPredicate().equals(SHACL.predicate)) {
				qb.addBinding(a.getPredicate().getLocalName(), constraint.getProperty(a.getPredicate()).getObject());
			}
		}
		
		//System.out.println("queryString: "+template.getExecutableBody());
		Map<String, Object> result = new SPARQLQueryExecutor().getMapResultsForQuery(qb.getQueryString(), namedModels.getDataset(), qb.getBindings());
		System.out.println("result size: "+result.size());
		if(result.size() > 0) {
			
			ConstraintViolationImpl violation = new ConstraintViolationImpl(
					SHACL.Error, focusNode, focusNode, null,
					null);
			
			Map<String, String> messages = (template.getMessages());
			messages = this.injectToMessage(messages, result);
			
			violation.setMessages(messages);

			errorModel.add(violation.getModel());

		}
		
		return errorModel;
	}
	
	/**
	 * Validates a native constraint.
	 * A native constraint is represented in a sh:constraint node
	 * 
	 * @param focusNode
	 * @param constraint
	 * @return
	 * @throws SHACLParsingException 
	 */
	private Model validateNativeConstraint(Resource focusNode, Resource constraint, Model model) throws SHACLParsingException {
		Model errorModel = ModelFactory.createDefaultModel();
		
		NativeConstraint nativeConstraint = SHACLResourceBuilder.build(constraint.listProperties().toList(), new NativeConstraint());
		
		QueryBuilder qb = new QueryBuilder(
				nativeConstraint.getExecutableBody(),
				model.getNsPrefixMap());
		
		qb.addPreBinding(focusNode, Config.DEFAULT_NAMED_MODEL, currentShape);

		//System.out.println("queryString: "+nativeConstraint.getExecutableBody());
		Map<String, Object> result = new SPARQLQueryExecutor().getMapResultsForQuery(qb.getQueryString(), namedModels.getDataset(), qb.getBindings());
		
		if(result.size() > 0) {
			ConstraintViolationImpl violation = new ConstraintViolationImpl(
					nativeConstraint.getSeverity(), focusNode, focusNode, 
					nativeConstraint.getPredicate(), 
					null);
			
			Map<String, String> messages = (nativeConstraint.getMessages());
			messages = this.injectToMessage(messages, result);
			
			violation.setMessages(messages);
			
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
	 * @throws SHACLParsingException 
	 */
	private Model validatePropertyConstraint(Resource focusNode, Resource constraint, Model model, boolean isInverse) throws SHACLParsingException {
		Model errorModel = ModelFactory.createDefaultModel();

		PropertyConstraint propertyConstraint = SHACLResourceBuilder.build(constraint.listProperties().toList(), new PropertyConstraint(), false);
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
				qb.addPreBinding(focusNode, Config.DEFAULT_NAMED_MODEL, currentShape);
				for(Map.Entry<Property, RDFNode> c : cs.getConstraints().entrySet()) {
					if(c.getValue() != null) {
						qb.addBinding(c.getKey().getLocalName(), c.getValue());
					}
				}

				Map<String, Object> result = new SPARQLQueryExecutor().getMapResultsForQuery(qb.getQueryString(), namedModels.getDataset(), qb.getBindings());
				
				if(result.size() > 0) {
					ConstraintViolationImpl violation = new ConstraintViolationImpl(
							((ConstraintTemplate)cs.getTemplate()).getSeverity(), focusNode, focusNode, 
							propertyConstraint.getPredicate(), 
							null);
					
					Map<String, String> messages = ((ConstraintTemplate)cs.getTemplate()).getMessages();
					messages = this.injectToMessage(messages, result);
					
					violation.setMessages(messages);

					errorModel.add(violation.getModel());

				}
			}
		}

		
		return errorModel;
	}
	
	/**
	 * Inject variable values to message
	 * 
	 * @param messages
	 * @param variables
	 * @return
	 */
	private Map<String, String> injectToMessage(Map<String, String> messages,
			Map<String, Object> variables) {
		
		for(Map.Entry<String, String> message : messages.entrySet()) {
			message.setValue(ValueInjector.inject(message.getValue(), variables));
		}
		
		return messages;
	}

	/** This operation validates a single node against all shapes associated with it, based on in-graph mappings.

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
	public List<ShapeInstanceMap> getInstantiatedShapes(Model model) {
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
	
	public static SHACLValidator getValidator() {
		if(validator == null) {
			System.out.println("VALIDATOR CREATED");
			validator = new SHACLValidator();
		}
		
		return validator;
	}
}
