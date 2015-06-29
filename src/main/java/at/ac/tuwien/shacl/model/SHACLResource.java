package at.ac.tuwien.shacl.model;

import java.util.Map;

import com.hp.hpl.jena.rdf.model.Resource;

public interface SHACLResource extends Resource {
	public Map<String, String> getMessages();
}
