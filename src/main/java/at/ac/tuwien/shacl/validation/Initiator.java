package at.ac.tuwien.shacl.validation;

import at.ac.tuwien.shacl.model.constraints.HasValue;
import at.ac.tuwien.shacl.sparql.querying.VocabularyRegistry;
import at.ac.tuwien.shacl.vocabulary.PropertyConstraint;

public class Initiator {
	private VocabularyRegistry registry;
	
	public void Initiator() {
		this.registry = new VocabularyRegistry();
	}
	
	private void registerSHACLModel() {
		//register property constraints
		registry.register(PropertyConstraint.shHasValue, HasValue.class);
	}
}
