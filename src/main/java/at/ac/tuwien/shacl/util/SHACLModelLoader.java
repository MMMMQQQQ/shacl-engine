package at.ac.tuwien.shacl.util;

import at.ac.tuwien.shacl.sparql.queries.DatatypeQuery;
import at.ac.tuwien.shacl.sparql.queries.HasValueQuery;
import at.ac.tuwien.shacl.validation.DatatypeBuilder;
import at.ac.tuwien.shacl.validation.HasValueBuilder;

/**
 * Create the SHACL model and all necessary actions to work with the engine.
 * 
 * @author Xiashuo Lin
 *
 */
public class SHACLModelLoader {
	private static ModelRegistry registry;
	
	public static void init() {
		registry = new ModelRegistry();
		
		initRegistry();
	}
	
	private static void initRegistry() {
		registry.register(HasValueBuilder.class, HasValueQuery.class);
		//TODO bind in sparql needs sh:valueCount defined first
		//registry.register(MinMaxCountBuilder.class, MinMaxCountQuery.class);
		
		//FIXME bindings get changed in querysolutionmap
		//registry.register(AllowedValuesBuilder.class, AllowedValuesQuery.class);
		registry.register(DatatypeBuilder.class, DatatypeQuery.class);
	}

	public static ModelRegistry getModelRegistry() {
		if(registry == null) {
			init();
		}
		
		return registry;
	}
}
