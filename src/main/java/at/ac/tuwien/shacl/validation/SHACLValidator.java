package at.ac.tuwien.shacl.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.mgt.Explain;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Covers section 11, supported operations. Kick starts the validation process.
 * @author xlin
 *
 */
public class SHACLValidator {
	private final Logger log = LoggerFactory.getLogger(SHACLValidator.class);

	private SHACLModelRegistry registry;

	private static SHACLValidator validator;
	
	private static NamedModelsRegistry namedModels;

	private Resource currentShape;
	
	public SHACLValidator() {
		validator = this;
		namedModels = NamedModelsRegistry.get();
		
		//add sparql executable
		Executables.addExecutable(new SPARQLExecutableLanguage());
	}
	
	/**
	 * This operation validates a whole graph against all shapes associated with its resources, 
	 * based on in-graph mappings.
	 */
	public Model validateGraph(Model model) {
		this.registry = SHACLModelRegistry.createNewInstance();
		this.registry.register(model);

		return this.validateGraph(Config.DEFAULT_NAMED_MODEL.getURI(), model);
	}
	
	public Model validateGraph(String uri, Model model) {
		namedModels.addNamedGraph(uri, model);
		namedModels.setDefaultModel(model);
		
		List<ShapeInstanceMap> shapes = this.getInstantiatedShapes(model);
		Model errorModel = ModelFactory.createDefaultModel();

		log.debug("number of shape data: "+shapes.size());
		
		for(ShapeInstanceMap shape : shapes) {
			errorModel.add(this.validateNodeAgainstShape(shape.getInstance(), shape.getShape(), model));
		}

		log.info("*******constraint violations*******");
		
		if(errorModel.isEmpty()) {
			log.info("{}");
		} else {
			log.info(errorModel.getGraph().toString());
		}

		return errorModel;
	}
	
	/** This operation validates a single node against all constraints associated with a given shape.
	 */
	public Model validateNodeAgainstShape(Resource focusNode, Resource shapeNode, Model model) {
		Model errorModel = ModelFactory.createDefaultModel();
		currentShape = shapeNode;

		
		Shape shape = this.registry.getNamedShape(shapeNode.getURI());

		for(Constraint constraint : shape.getConstraints()) {
			errorModel.add(this.validateConstraint(focusNode, constraint, model));
		}
		
		return errorModel;
	}

	/** This operation evaluates a single constraint and produces constraint violations.

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
	
	/** This operation validates a single node against all shapes associated with it, based on in-graph mappings.
	*/
	public Model validateNode(Resource focusNode, Model model) throws SHACLParsingException {
		Model errorModel = ModelFactory.createDefaultModel();
		
		for(Shape shape : this.getInstantiatedShapes(focusNode, model)) {
			errorModel.add(this.validateNodeAgainstShape(focusNode, shape, model));
		}
		
		return errorModel;
	}
	
	protected Model validateConstraint(Resource focusNode, Constraint constraint, Model model) {
		Model errorModel = ModelFactory.createDefaultModel();
		
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
			String executable = instance.getKey().getExecutableBody(SHACL.sparql);
			Dataset dataset = namedModels.getDataset();
			Map<String, RDFNode> mappings = this.createInitialMapping(focusNode, Config.DEFAULT_NAMED_MODEL, currentShape);
			mappings.putAll(instance.getValue());

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
			log.debug("result of validation: "+result);
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
		log.debug("result of validation: "+result);
		
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
			log.debug("result of validation: "+result);
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

	public Set<Shape> getInstantiatedShapes(Resource focusNode, Model model) {
		Set<Shape> shapes = new HashSet<Shape>();

		for(Statement stmt : focusNode.listProperties(SHACL.nodeShape).toList()) {
			Shape shape = this.registry.getNamedShape(stmt.getResource().getURI());
			if(shape != null) {
				shapes.add(shape);
			} else {
				//TODO add possiblity for anonymous shapes
			}
		}
		
		//TODO only one rdf:type is recognized currently
		for(Statement stmt : focusNode.listProperties(RDF.type).toList()) {
			Resource potentialShape = stmt.getResource();
			if(potentialShape.listProperties(RDF.type).toSet().contains(SHACL.Shape) || 
					potentialShape.listProperties(RDF.type).toSet().contains(SHACL.ShapeClass)) {
				shapes.add(this.registry.getNamedShape(potentialShape.getURI()));
			}

			if(model.listObjectsOfProperty(
					SHACL.scopeClass).toSet().contains(potentialShape.getProperty(RDF.type))) {
				shapes.add(this.registry.getNamedShape(potentialShape.getURI()));
			}
		}
		
		return shapes;
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
						shapeInstances.add(new ShapeInstanceMap(this.registry.getNamedShape(shapeS.getSubject().getURI()), s.getSubject()));
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
			validator = new SHACLValidator();
		}
		
		return validator;
	}
}
