package at.ac.tuwien.shacl.model;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class Datatype implements PropertyConstraint {
	private Resource focusNode;
	
	private Resource predicate;
	
	private RDFNode datatypeNode;

	public Datatype(Resource focusNode, Resource predicate, RDFNode datatypeNode) {
		super();
		this.focusNode = focusNode;
		this.predicate = predicate;
		this.datatypeNode = datatypeNode;
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

	public RDFNode getDatatypeNode() {
		return datatypeNode;
	}

	public void setDatatypeNode(RDFNode datatypeNode) {
		this.datatypeNode = datatypeNode;
	}
}
