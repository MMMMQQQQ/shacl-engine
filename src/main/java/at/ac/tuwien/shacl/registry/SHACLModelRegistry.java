package at.ac.tuwien.shacl.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.shacl.executable.ExecutableLanguage;
import at.ac.tuwien.shacl.executable.Executables;
import at.ac.tuwien.shacl.model.Constraint;
import at.ac.tuwien.shacl.model.Function;
import at.ac.tuwien.shacl.model.Shape;
import at.ac.tuwien.shacl.model.Template;
import at.ac.tuwien.shacl.model.impl.SHACLResourceFactory;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class SHACLModelRegistry {
	private Map<String, Function> functions;
	
	private Map<String, Template> templates;
	
	private Map<String, Constraint> namedConstraints;
	
	private Map<String, Shape> namedShapes;

	private String language;
	
	public SHACLModelRegistry(String language) {
		this.init(language);
	}

	/**
	 * Initialize the registry with all registered function and templates of the input registry.
	 * 
	 * @param modelRegistry registry whose templates and functions are added to this registry.
	 */
	public SHACLModelRegistry(SHACLModelRegistry modelRegistry) {
		this.init(modelRegistry.getValidationLanguage());
		this.addRegistry(modelRegistry);
	}

	private void init(String language) {
		this.functions = new HashMap<String, Function>();
		this.templates = new HashMap<String, Template>();
		this.namedConstraints = new HashMap<String, Constraint>();
		this.namedShapes = new HashMap<String, Shape>();
		this.language = language;
	}
	
	public void register(Model model) {
		this.registerFunctions(model);
		this.registerNamedTemplates(model);
		this.registerNamedShapes(model);
		this.registerNamedConstraints(model);
	}
	
	//TODO consider subclass of sh:Function too
	public void registerFunctions(Model model) {
		ResIterator it = model.listResourcesWithProperty(RDF.type, SHACL.Function);
		
		while(it.hasNext()) {
			Resource r = it.next();
			//ignore anonymous functions
			if(r.getURI() != null) {
				Function function = SHACLResourceFactory.createFunction(r);
				
				functions.put(function.getURI(), function);

				//register function in all known executables
				for(Map.Entry<Property, String> e : function.getExecutableBodies().entrySet()) {
					ExecutableLanguage lang = Executables.getExecutable(e.getKey());
					if(lang != null) {
						lang.registerFunction(function);
					} else {
						//executable language is not registered in executables -> remove from function?
					}
				}
			}
		}
	}
	
	/**
	 * register all templates with an URI to the registry.
	 * 
	 * @param model
	 */
	public void registerNamedTemplates(Model model) {
		this.registerNamedTemplates(model, SHACL.Template);
	}
	
	public void registerNamedTemplates(Model model, Resource resourceName) {
		ResIterator it = model.listResourcesWithProperty(RDF.type, resourceName);
		
		while(it.hasNext()) {
			Resource r = it.next();
			//ignore anonymous functions
			if(r.getURI() != null) {
				Template t = SHACLResourceFactory.createTemplate(r);
				templates.put(t.getURI(), t);
			}
		}
		
		//register templates of all subclasses
		for(Resource subR : model.listResourcesWithProperty(RDFS.subClassOf, resourceName).toList()) {
			this.registerNamedTemplates(model, subR);
		}
		
		//register templates subclassing from the metamodel
		if(model != SHACL.getModel()) {
			for(Resource subR : SHACL.getModel().listResourcesWithProperty(RDFS.subClassOf, resourceName).toList()) {
				this.registerNamedTemplates(model, subR);
			}
		}
	}
	
	/**
	 * Register all shapes with an URI to the registry
	 * 
	 * @param model
	 */
	//TODO consider subclasses of sh:Shape/sh:ShapeClass too
	public void registerNamedShapes(Model model) {
		List<Resource> shapes = model.listResourcesWithProperty(RDF.type, SHACL.Shape).toList();
		shapes.addAll(model.listResourcesWithProperty(RDF.type, SHACL.ShapeClass).toList());
		
		for(Resource r : shapes) {
			//ignore anonymous functions
			if(r.getURI() != null) {
				Shape s = SHACLResourceFactory.createShape(r);
				namedShapes.put(s.getURI(), s);
			}
		}
	}
	
	public void registerNamedConstraint(Constraint constraint) {
		if(constraint.getURI() != null) {
			this.namedConstraints.put(constraint.getURI(), constraint);
		}
	}
	
	/**
	 * Register all constraints with an URI to the registry.
	 * 
	 * @param model
	 */
	public void registerNamedConstraints(Model model) {
		//TODO extract named constraints from model
	}
	
	/**
	 * Add templates and functions of another registry. If the URI of a new template or function already
	 * exists, it will be overwritten by the new value.
	 * 
	 * @param templates
	 */
	public void addRegistry(SHACLModelRegistry modelRegistry) {
		this.functions.putAll(modelRegistry.getFunctions());
		this.templates.putAll(modelRegistry.getTemplates());
	}
	
	public Map<String, Function> getFunctions() {
		return functions;
	}
	
	public Map<String, Template> getTemplates() {
		return templates;
	}
	
	public Map<String, Constraint> getNamedConstraints() {
		return namedConstraints;
	}
	
	public Function getFunction(String uri) {
		return functions.get(uri);
	}
	
	public Template getTemplate(String uri) {
		return templates.get(uri);
	}
	
	public Constraint getNamedConstraint(String uri) {
		return namedConstraints.get(uri);
	}
	
	public Shape getNamedShape(String uri) {
		return namedShapes.get(uri);
	}
	
	public String getValidationLanguage() {
		return language;
	}
	
	private static SHACLModelRegistry registry;
	
	private static SHACLModelRegistry metamodelRegistry;

	protected static SHACLModelRegistry getSHACLModelRegistry() {
		if(metamodelRegistry == null) {
			metamodelRegistry = new SHACLModelRegistry("en");
			Model model = SHACL.getModel();
			
			metamodelRegistry.registerFunctions(model);
			metamodelRegistry.registerNamedTemplates(model);
		}
		
		return metamodelRegistry;
	}
	
	public static SHACLModelRegistry get() {
		if(registry == null) {
			//init registry with data from shacl spec model
			registry = new SHACLModelRegistry(getSHACLModelRegistry());
		}
		
		return registry;
	}
	
	public static SHACLModelRegistry createInstance() {
		return new SHACLModelRegistry(metamodelRegistry);
	}
}
