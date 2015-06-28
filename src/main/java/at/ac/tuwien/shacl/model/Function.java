package at.ac.tuwien.shacl.model;

import com.hp.hpl.jena.rdf.model.Resource;

public interface Function extends Macro {
	public Resource getReturnType();

	public Boolean isCachable();
}
