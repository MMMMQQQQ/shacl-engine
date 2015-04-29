package at.ac.tuwien.shacl.util;

import at.ac.tuwien.shacl.sparql.queries.HasValueQuery;
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
	}

	public static ModelRegistry getModelRegistry() {
		if(registry == null) {
			init();
		}
		
		return registry;
	}
}
