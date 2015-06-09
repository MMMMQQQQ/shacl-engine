package at.ac.tuwien.shacl.util;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import at.ac.tuwien.shacl.model.impl.ConstraintViolationImpl;
import at.ac.tuwien.shacl.vocabulary.SHACL;

public class SHACLParsingException extends Exception {
	public SHACLParsingException() {
		super();
	}
	
	public SHACLParsingException(String e) {
		super(e);
	}
	
	public SHACLParsingException()
	
	private buildFatalError(Resource subject, Property predicate, Resource object) {
		ConstraintViolationImpl violation = new ConstraintViolationImpl(SHACL.FatalError, );
	}
}
