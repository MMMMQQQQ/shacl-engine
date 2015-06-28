package at.ac.tuwien.shacl.model;

import java.util.Map;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * TODO re-implement profile according to newest version
 * 
 * @author xlin
 *
 */
public interface Profile extends SHACLResource {
	public Map<String, Resource> getMembers();
}
