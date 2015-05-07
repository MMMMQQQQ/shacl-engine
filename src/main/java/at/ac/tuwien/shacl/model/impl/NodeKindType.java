package at.ac.tuwien.shacl.model.impl;

import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Resource;

public enum NodeKindType {
	IRI(SHACL.IRI),
	BlankNode(SHACL.BlankNode),
	Literal(SHACL.Literal);
	
	private Resource nodeKind;
	
	private NodeKindType(Resource nodeKind) {
		this.nodeKind= nodeKind;
	}
	
	public Resource getNodeKindType() {
		return this.nodeKind;
	}
	
	public static boolean isNodeKindType(Resource resource) {
		boolean isNodeKindType = false;
		
		for(NodeKindType n : NodeKindType.values()) {
			if(n.getNodeKindType().getURI().equals(resource.getURI())) {
				isNodeKindType = true;
				break;
			}
		}
		
		return isNodeKindType;
	}
}
