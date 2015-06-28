package at.ac.tuwien.shacl.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.shacl.executable.Executables;
import at.ac.tuwien.shacl.executable.sparql.SPARQLExecutableLanguage;
import at.ac.tuwien.shacl.model.Constraint;
import at.ac.tuwien.shacl.model.ConstraintViolation;
import at.ac.tuwien.shacl.model.Shape;
import at.ac.tuwien.shacl.model.Template;
import at.ac.tuwien.shacl.model.impl.SHACLResourceFactory;
import at.ac.tuwien.shacl.model.impl.constraints.NativeConstraint;
import at.ac.tuwien.shacl.model.impl.constraints.PropertyConstraint;
import at.ac.tuwien.shacl.model.impl.constraints.TemplateConstraint;
import at.ac.tuwien.shacl.registry.NamedModelsRegistry;
import at.ac.tuwien.shacl.registry.SHACLModelRegistry;
import at.ac.tuwien.shacl.util.Config;
import at.ac.tuwien.shacl.util.SHACLParsingException;
import at.ac.tuwien.shacl.util.ValueInjector;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
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
	private SHACLModelRegistry registry;

	private static SHACLValidator validator;
	
	private static NamedModelsRegistry namedModels;

	//TODO refactor
	private Resource currentShape;
	
	public SHACLValidator() {
		validator = this;
		namedModels = new NamedModelsRegistry();
		System.out.println("--------------initializing done");
		
		//add sparql executable
		Executables.addExecutable(new SPARQLExecutableLanguage());
	}
	
	/*
	 * This operation validates a whole graph against all shapes associated with its resources, 
	 * based on in-graph mappings.
	 * 
	 * ?minSeverity	rdfs:Class	The minimum severity class, e.g. sh:Error specifying which constraints to exclude/include.
	 */
	public Model validateGraph(Model model) throws SHACLParsingException {
		this.registry = SHACLModelRegistry.get();
		this.registry.register(model);
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

		return errorModel;
	}
	
	/* This operation validates a single node against all constraints associated with a given shape.

		Argument	Type	Description
		?focusNode	rdfs:Resource	The focus node to validate
		?shape	sh:Shape	The shape that has the constraints.
		?minSeverity	rdfs:Class	The minimum severity class, e.g. sh:Error specifying which constraints to exclude/include.
	 */
	public Model validateNodeAgainstShape(Resource focusNode, Resource shapeNode, Model model) throws SHACLParsingException {
		System.out.println("*****************************");
		
		Model errorModel = ModelFactory.createDefaultModel();
		currentShape = shapeNode;

		Shape shape = this.registry.getNamedShape(shapeNode.getURI());

		for(Constraint constraint : shape.getConstraints()) {
			errorModel.add(this.validateConstraint(focusNode, constraint, model));
		}
		
		return errorModel;
	}

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
	public Model validateConstraint(Resource focusNode, Resource constraint, Model model) {
		return this.validateConstraint(focusNode, registry.getNamedConstraint(constraint.getURI()), model);
	}
	
	protected Model validateConstraint(Resource focusNode, Constraint constraint, Model model) {
		Model errorModel = ModelFactory.createDefaultModel();
		
		//TODO distinguish between sh:property and sh:inverseProperty
		if(constraint.getConstraintType().equals(SHACL.property) || 
				constraint.getConstraintType().equals(SHACL.inverseProperty)) {
			errorModel.add(this.validatePropertyConstraint(focusNode, constraint, errorModel));
		} else if(constraint.getConstraintType().equals(SHACL.constraint)) {
			if(this.isNativeConstraint(constraint)) {
				errorModel.add(this.validateNativeConstraint(focusNode, constraint, errorModel));
			} else {
				errorModel.add(this.validateTemplateConstraint(focusNode, constraint, errorModel));
			}
		} else {
			//TODO allow for custom defined constraint types?
		}
		
		return errorModel;
	}
	
	private Model validateTemplateConstraint(Resource focusNode, Constraint constraint, Model model) {
		Model errorModel = ModelFactory.createDefaultModel();
		
		TemplateConstraint templateCst= SHACLResourceFactory.createTemplateConstraint(constraint);
		for(Map.Entry<Template, Map<String, RDFNode>> instance : templateCst.getTemplateInstances().entrySet()) {
			System.out.println("~~~~instance:");
			String executable = instance.getKey().getExecutableBody(SHACL.sparql);
			Dataset dataset = namedModels.getDataset();
			Map<String, RDFNode> mappings = this.createInitialMapping(focusNode, Config.DEFAULT_NAMED_MODEL, currentShape);
			mappings.putAll(instance.getValue());
			System.out.println("templates: "+instance.getKey());
			Map<String, RDFNode> result = Executables.getExecutable(SHACL.sparql).executeAsMultipleValues(executable, dataset, mappings);

			if(result.size() > 0) {
				//TODO allow custom severity
				ConstraintViolation violation = SHACLResourceFactory.createConstraintViolation(
						SHACL.Error, focusNode, focusNode, null, null);

				Map<String, String> messages = instance.getKey().getMessages();
				messages = this.injectToMessage(messages, result);
				
				violation.setMessages(messages);

				errorModel.add(violation.getModel());
			}
			System.out.println("result: "+result);
		}
		
		return errorModel;
	}
	
	private Model validateNativeConstraint(Resource focusNode, Constraint constraint, Model model) {
		Model errorModel = ModelFactory.createDefaultModel();
		
		NativeConstraint nativeConstraint = SHACLResourceFactory.createNativeConstraint(constraint);
		
		String executable = nativeConstraint.getExecutableBody(SHACL.sparql);
		Dataset dataset = namedModels.getDataset();
		Map<String, RDFNode> mappings = this.createInitialMapping(focusNode, Config.DEFAULT_NAMED_MODEL, currentShape);

		Map<String, RDFNode> result = Executables.getExecutable(SHACL.sparql).executeAsMultipleValues(executable, dataset, mappings);

		if(result.size() > 0) {
			ConstraintViolation violation = SHACLResourceFactory.createConstraintViolation(
					nativeConstraint.getSeverity(), focusNode, focusNode, nativeConstraint.getPredicate(), null);

			Map<String, String> messages = nativeConstraint.getMessages();
			messages = this.injectToMessage(messages, result);
			
			violation.setMessages(messages);

			errorModel.add(violation.getModel());
		}
		System.out.println("result: "+result);
		
		return errorModel;
	}
	
	private Model validatePropertyConstraint(Resource focusNode, Constraint constraint, Model model) {
		Model errorModel = ModelFactory.createDefaultModel();
		
		PropertyConstraint propCst= SHACLResourceFactory.createPropertyConstraint(constraint);
		for(Map.Entry<Template, Map<String, RDFNode>> instance : propCst.getTemplateInstances().entrySet()) {
			String executable = instance.getKey().getExecutableBody(SHACL.sparql);
			Dataset dataset = namedModels.getDataset();
			Map<String, RDFNode> mappings = this.createInitialMapping(focusNode, Config.DEFAULT_NAMED_MODEL, currentShape);
			mappings.putAll(instance.getValue());
			System.out.println("templates: "+instance.getKey());
			Map<String, RDFNode> result = Executables.getExecutable(SHACL.sparql).executeAsMultipleValues(executable, dataset, mappings);

			if(result.size() > 0) {
				//TODO allow custom severity
				ConstraintViolation violation = SHACLResourceFactory.createConstraintViolation(
						SHACL.Error, focusNode, focusNode, propCst.getPredicate(), null);

				Map<String, String> messages = instance.getKey().getMessages();
				messages = this.injectToMessage(messages, result);
				
				violation.setMessages(messages);

				errorModel.add(violation.getModel());
			}
			System.out.println("result: "+result);
		}
		
		return errorModel;
	}
	
	private Map<String, RDFNode> createInitialMapping(RDFNode focusNode, RDFNode shapesGraph, RDFNode currentShape) {
		Map<String, RDFNode> mapping = new HashMap<String, RDFNode>();
		
		mapping.put("this", focusNode);
		mapping.put("shapesGraph", shapesGraph);
		mapping.put("currentShape", currentShape);
		
		return mapping;
	}
	
	private boolean isNativeConstraint(Resource constraint) {
		System.out.println("constraint: "+constraint.getProperty(RDF.type));
		if(constraint.getProperty(RDF.type) == null || 
				constraint.getProperty(RDF.type).getObject().equals(SHACL.NativeConstraint)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Inject variable values to message
	 * 
	 * @param messages
	 * @param variables
	 * @return
	 */
	private Map<String, String> injectToMessage(Map<String, String> messages,
			Map<String, RDFNode> variables) {
		
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
		private Shape shape;
		private Resource instance;
		
		public ShapeInstanceMap(Shape shape, Resource instance) {
			this.shape = shape;
			this.instance = instance;
		}
		
		public Shape getShape() {
			return this.shape;
		}
		
		public Resource getInstance() {
			return this.instance;
		}
	}
	
	public List<Shape> getInstantiatedShapes(Resource focusNode) {
		List<Shape> attachedShapes = new ArrayList<Shape>();
		
		/**
		 * 
			forEach ?shape := (?focusNode sh:nodeShape ?shape)
				validateNodeAgainstShape(?focusNode, ?shape, ?minSeverity)
			
			forEach ?type := (?focusNode rdf:type/rdfs:subClassOf* ?type)
				if (?type instanceof sh:Shape)
					validateNodeAgainstShape(?focusNode, ?type, ?minSeverity)
				forEach ?shape := (?shape sh:scopeClass ?type)
					validateNodeAgainstShape(?focusNode, ?shape, ?minSeverity)
		 */
		
		return attachedShapes;
	}

	/**
	 *  Determine which nodes need to be evaluated against which shapes.
	 *  There are three shape selection properties currently: sh:nodeShape, sh:scopeClass and rdf:type
	 *  (Section 11.1)
	 */
	//TODO consider anonymous shapes too
	public List<ShapeInstanceMap> getInstantiatedShapes(Model model) {
		List<ShapeInstanceMap> shapeInstances = new ArrayList<ShapeInstanceMap>();

		for(Statement shapeS : model.listStatements(null, RDF.type, SHACL.Shape).toList()) {
			for(Statement s : model.listStatements(null, SHACL.nodeShape, shapeS.getSubject()).toList()) {
				shapeInstances.add(new ShapeInstanceMap(this.registry.getNamedShape(s.getResource().getURI()), s.getSubject()));
			}
			
			for(Statement s : model.listStatements(null, RDF.type, shapeS.getSubject()).toList()) {
				shapeInstances.add(new ShapeInstanceMap(this.registry.getNamedShape(s.getResource().getURI()), s.getSubject()));
			}

			if(shapeS.getSubject().getProperty(SHACL.scopeClass) != null) {
				Resource scopeClass = shapeS.getSubject().getProperty(SHACL.scopeClass).getResource();
				if(scopeClass != null) {
					List<Statement> scopeStatements = model.listStatements(null, RDF.type, scopeClass).toList();
					for(Statement s : scopeStatements) {
						shapeInstances.add(new ShapeInstanceMap(this.registry.getNamedShape(s.getResource().getURI()), s.getSubject()));
					}
				}
			}
		}
		
		for(Statement shapeS : model.listStatements(null, RDF.type, SHACL.ShapeClass).toList()) {
			for(Statement s : model.listStatements(null, RDF.type, shapeS.getSubject()).toList()) {
				shapeInstances.add(new ShapeInstanceMap(this.registry.getNamedShape(s.getResource().getURI()), s.getSubject()));
			}
		}
		
		return shapeInstances;
	}
	
	public static SHACLValidator getDefaultValidator() {
		if(validator == null) {
			System.out.println("VALIDATOR CREATED");
			validator = new SHACLValidator();
		}
		
		return validator;
	}
}
