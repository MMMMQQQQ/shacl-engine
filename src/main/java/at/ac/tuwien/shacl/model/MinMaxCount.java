package at.ac.tuwien.shacl.model;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class MinMaxCount implements PropertyConstraint {
	private Resource focusNode;
	
	private Resource predicate;
	
	private RDFNode minCountNode;

	private RDFNode maxCountNode;
	
	public MinMaxCount(Resource focusNode, Resource predicate,
			RDFNode minCountNode, RDFNode maxCountNode) {
		super();
		this.focusNode = focusNode;
		this.predicate = predicate;
		this.minCountNode = minCountNode;
		this.maxCountNode = maxCountNode;
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

	public RDFNode getMinCountNode() {
		return minCountNode;
	}

	public void setMinCountNode(RDFNode minCountNode) {
		this.minCountNode = minCountNode;
	}

	public RDFNode getMaxCountNode() {
		return maxCountNode;
	}

	public void setMaxCountNode(RDFNode maxCountNode) {
		this.maxCountNode = maxCountNode;
	}
}
