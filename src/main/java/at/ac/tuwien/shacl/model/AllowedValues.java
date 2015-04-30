package at.ac.tuwien.shacl.model;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class AllowedValues implements PropertyConstraint {
	private Resource focusNode;
	
	private Resource predicate;
	
	private RDFNode allowedValuesNode;

	public AllowedValues(Resource focusNode, Resource predicate,
			RDFNode allowedValuesNode) {
		super();
		this.focusNode = focusNode;
		this.predicate = predicate;
		this.allowedValuesNode = allowedValuesNode;
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

	public RDFNode getAllowedValuesNode() {
		return allowedValuesNode;
	}

	public void setAllowedValuesNode(RDFNode allowedValuesNode) {
		this.allowedValuesNode = allowedValuesNode;
	}
}
