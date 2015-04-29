package at.ac.tuwien.shacl.vocabulary;

import com.hp.hpl.jena.rdf.model.Resource;

public interface SHACLConstraint extends SHACLType {
	public Resource getResource();
}
