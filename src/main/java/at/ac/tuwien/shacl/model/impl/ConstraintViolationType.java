package at.ac.tuwien.shacl.model.impl;

import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Resource;

public enum ConstraintViolationType {
	Error(SHACL.Error),
	Warning(SHACL.Warning),
	FatalError(SHACL.FatalError);
	
	private Resource violation;
	
	private ConstraintViolationType(Resource violation) {
		this.violation = violation;
	}
	
	public Resource getViolationType() {
		return this.violation;
	}
	
	public static boolean isConstraintViolationType(Resource resource) {
		boolean isViolation = false;
		
		for(ConstraintViolationType c : ConstraintViolationType.values()) {
			if(c.getViolationType().getURI().equals(resource.getURI())) {
				isViolation = true;
			}
		}
		
		return isViolation;
	}
}
