package at.ac.tuwien.shacl.model;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public interface Argument extends SHACLResource {
	public Resource getPredicate();

	public Boolean isOptional();

	public RDFNode getDefaultValue();

	public Resource getValueClass();

	public Resource getDatatype();

	public Boolean getOptionalWhenInherited();

	public Resource getNodeKind();
}
