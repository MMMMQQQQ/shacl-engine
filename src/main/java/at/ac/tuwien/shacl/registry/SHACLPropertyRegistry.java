package at.ac.tuwien.shacl.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.model.Template;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.RDFS;

public class SHACLPropertyRegistry {
	//all registered property constraints
	private Map<String, String> propertyPredicates;

	public SHACLPropertyRegistry() {
		this.propertyPredicates = new HashMap<String, String>();
		this.init();
	}
	
	private void init() {
		Model model = SHACL.getModel();
		
		for(RDFNode template : model.listObjectsOfProperty(SHACL.PropertyConstraint, RDFS.subClassOf).toList()) {
			for(RDFNode args : model.listObjectsOfProperty(template.asResource(), SHACL.argument).toList()) {
				this.propertyPredicates.put(args.asResource().getProperty(SHACL.predicate).getResource().getURI(), 
						template.asResource().getURI());
				System.out.println("registered "+args.asResource().getProperty(SHACL.predicate).getResource().getURI() + "with template" + template.asResource().getURI());
			}
		}
	}
	
	public Template getTemplate(String constraintUri) {
		return SHACLModelRegistry.get().getTemplate(propertyPredicates.get(constraintUri));
	}
	
	public Set<String> getPropertyConstraintsURIs() {
		return this.propertyPredicates.keySet();
	}
	
	public void setPropertyConstraint(String uri, String templateUri) {
		this.propertyPredicates.put(uri, templateUri);
	}
	
	private static SHACLPropertyRegistry registry;
	
	public static SHACLPropertyRegistry get() {
		if(registry == null) {
			registry = new SHACLPropertyRegistry();
		}
		
		return registry;
	}
}
