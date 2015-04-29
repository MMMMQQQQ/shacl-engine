package at.ac.tuwien.shacl.vocabulary;

import com.hp.hpl.jena.rdf.model.Resource;

public enum PropertyConstraint implements SHACLConstraint {
	shMaxCount(SHACL.maxCount),
	
	shMinCount(SHACL.minCount),
	
	shHasValue(SHACL.hasValue);

	private Resource resource;
	
	private PropertyConstraint(Resource resource) {
		this.resource = resource;
	}
	
	public Resource getResource() {
		return this.resource;
	}
	
	
}
