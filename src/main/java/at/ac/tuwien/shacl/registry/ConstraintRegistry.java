package at.ac.tuwien.shacl.registry;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Contains all known constraints. Constraints can either be part of the SHACL language or custom added.
 * @author xlin
 *
 */
public class ConstraintRegistry {
	
	public ConstraintRegistry() {
		
	}
	
	//register all known constraints extracted from the model
	public void register(Model model) {
		
	}
	
	public void registerPropertyConstraint() {
		
	}
	
	public void registerGlobalNativeConstraint() {
		
	}
	
	
	public void registrerGlobalTemplateConstraint() {
		
	}
	
	//sh:inverseProperty
	public void registerInversePropertyConstraint() {
		
	}
	
	//rdf:type of 
	public void registerConstraintTemplate() {
		
	}
	
	//sh:orConstraint
	public void registerDisjunctiveConstraint() {
		
	}
	
	//sh:constraint
	public void registerGeneralShapeConstraint() {
		
	}
	
	private static ConstraintRegistry registry;
	
	public static ConstraintRegistry getRegistry() {
		if(registry == null) {
			registry = new ConstraintRegistry();
		}
		return registry;
	}
}
