package at.ac.tuwien.shacl.model;

import java.util.Map;

import com.hp.hpl.jena.rdf.model.Resource;

public interface SHACLResource extends Resource {
//	public boolean isSubclassOf(Resource resource);
//	
//	public boolean isTypeOf(Resource resource);
//	
//	public List<Resource> listSubclasses();
	
	public Map<String, String> getMessages();
}
