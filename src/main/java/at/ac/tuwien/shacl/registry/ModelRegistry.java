package at.ac.tuwien.shacl.registry;

import com.hp.hpl.jena.rdf.model.Model;

public class ModelRegistry {
	private static Model model;
	private static SHACLMetaModelRegistry registry;
	
	public static void setCurrentModel(Model model) {
		ModelRegistry.model = model;
	}
	
	public static Model getCurrentModel() {
		return model;
	}
	
	public static void unregisterModel() {
		model = null;
	}
}
