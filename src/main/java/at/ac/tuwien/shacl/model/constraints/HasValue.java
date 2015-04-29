package at.ac.tuwien.shacl.model.constraints;

import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class HasValue implements Constraint {
	private Resource focusNode;
	
	private Property predicate;
	
	private RDFNode hasValue;
	
	public HasValue(Resource focusNode, Property predicate, RDFNode hasValue) {
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

	public Property getPredicate() {
		return predicate;
	}

	public void setPredicate(Property predicate) {
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
