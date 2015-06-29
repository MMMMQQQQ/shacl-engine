package at.ac.tuwien.shacl.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.model.Template;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * A convenience class to make selection of property constraint templates easier.
 * 
 * @author xlin
 *
 */
public class SHACLPropertyRegistry {
	private Map<String, String> propertyPredicates;

	public SHACLPropertyRegistry(boolean isInverse) {
		this.propertyPredicates = new HashMap<String, String>();
		this.init(isInverse);
	}
	
	private void init(boolean isInverse) {
		Model model = SHACL.getModel();
		Resource pcNode = isInverse ? SHACL.InversePropertyConstraint : SHACL.PropertyConstraint;
		
		for(RDFNode template : model.listObjectsOfProperty(pcNode, RDFS.subClassOf).toList()) {
			for(RDFNode args : model.listObjectsOfProperty(template.asResource(), SHACL.argument).toList()) {
				this.propertyPredicates.put(args.asResource().getProperty(SHACL.predicate).getResource().getURI(), 
						template.asResource().getURI());
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
	
	private static SHACLPropertyRegistry pcRegistry;
	
	private static SHACLPropertyRegistry inversePcRegistry;
	
	public static SHACLPropertyRegistry get() {
		if(pcRegistry == null) {
			pcRegistry = new SHACLPropertyRegistry(false);
		}
		
		return pcRegistry;
	}
	
	public static SHACLPropertyRegistry getInverse() {
		if(inversePcRegistry == null) {
			inversePcRegistry = new SHACLPropertyRegistry(true);
		}
		
		return inversePcRegistry;
	}
}
