package at.ac.tuwien.shacl.metamodel;

import java.util.Map;

import com.hp.hpl.jena.rdf.model.Resource;

public interface Profile {
	public void addMember(Resource member);

	public Map<String, Resource> getMembers();
	
	public Resource getMember(String uri);

	void setMembers(Map<String, Resource> members);
}
