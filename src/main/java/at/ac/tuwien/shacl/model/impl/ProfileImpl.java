package at.ac.tuwien.shacl.model.impl;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.metamodel.Profile;

import com.hp.hpl.jena.rdf.model.Resource;

public class ProfileImpl implements Profile {
	
	private Map<String, Resource> members;
	
	public ProfileImpl() {
		this.members = new HashMap<String, Resource>();
	}

	@Override
	public void addMember(Resource member) {
		this.members.put(member.getURI(), member);
	}

	@Override
	public void setMembers(Map<String, Resource> members) {
		this.members = members;
	}

	@Override
	public Map<String, Resource> getMembers() {
		return this.members;
	}

	@Override
	public Resource getMember(String uri) {
		return this.members.get(uri);
	}
}
