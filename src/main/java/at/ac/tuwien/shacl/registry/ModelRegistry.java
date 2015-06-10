package at.ac.tuwien.shacl.registry;

import java.util.Set;

import at.ac.tuwien.shacl.metamodel.ConstraintTemplate;
import at.ac.tuwien.shacl.metamodel.Function;

import com.hp.hpl.jena.rdf.model.Model;

public class ModelRegistry {
	private SHACLMetaModelRegistry metaregistry;
	private SHACLMetaModelRegistry registry;
	
	public ModelRegistry(Model model) {
		metaregistry = SHACLMetaModelRegistry.getRegistry();
		registry = new SHACLMetaModelRegistry();
		registry.register(model);
		thiz = this;
	}


	public Set<String> getInversePropertyConstraintsURIs() {
		Set<String> set = metaregistry.getInversePropertyConstraintsURIs();
		set.addAll(registry.getInversePropertyConstraintsURIs());
		return set;
	}

	public Set<String> getPropertyConstraintsURIs() {
		Set<String> set = metaregistry.getPropertyConstraintsURIs();
		set.addAll(registry.getPropertyConstraintsURIs());
		return set;
	}

	public ConstraintTemplate getPropertyConstraintTemplate(String constraintUri) {
		ConstraintTemplate templ = metaregistry.getPropertyConstraintTemplate(constraintUri);
		
		if(templ == null) {
			templ = registry.getPropertyConstraintTemplate(constraintUri);
		}

		return templ;
	}

	public ConstraintTemplate getInversePropertyConstraintTemplate(String constraintUri) {
		ConstraintTemplate templ = metaregistry.getInversePropertyConstraintTemplate(constraintUri);
		
		if(templ == null) {
			templ = registry.getInversePropertyConstraintTemplate(constraintUri);
		}

		return templ;
	}

	public ConstraintTemplate getGeneralConstraintTemplate(String constraintUri) {
		ConstraintTemplate templ = metaregistry.getGeneralConstraintTemplate(constraintUri);
		
		if(templ == null) {
			templ = registry.getGeneralConstraintTemplate(constraintUri);
		}

		System.out.println("template is:"+templ+ " looking for: "+constraintUri);
		return templ;
	}
	
	public Function getFunction(String uri) {
		Function func = metaregistry.getFunction(uri);
		
		if(func == null) {
			func = registry.getFunction(uri);
		}

		return func;
	}
	
	private static ModelRegistry thiz;
	
	public static ModelRegistry getRegistry() {
		return thiz;
	}
}
