package at.ac.tuwien.shacl.model;

import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class HasValue implements PropertyConstraint {
	private Resource focusNode;
	
	private Resource predicate;
	
	private RDFNode hasValue;
	
	public HasValue(Resource focusNode, Resource predicate, RDFNode hasValue) {
		this.focusNode = focusNode;
		this.predicate = predicate;
		this.hasValue = hasValue;
	}
	
	public Resource getFocusNode() {
		return focusNode;
	}

	public void setFocusNode(Resource focusNode) {
		this.focusNode = focusNode;
	}

	public Resource getPredicate() {
		return predicate;
	}

	public void setPredicate(Resource predicate) {
		this.predicate = predicate;
	}

	public RDFNode getHasValue() {
		return hasValue;
	}

	public void setHasValue(RDFNode hasValue) {
		this.hasValue = hasValue;
	}

	public String getURI() {
		return SHACL.hasValue.getURI();
	}
}
