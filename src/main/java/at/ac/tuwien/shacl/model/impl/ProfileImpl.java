package at.ac.tuwien.shacl.model.impl;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.model.Profile;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Resource;


public class ProfileImpl extends SHACLResourceImpl implements Profile {
	
	private Map<String, Resource> members;
	
	public ProfileImpl(Node node, EnhGraph graph) {
		super(node, graph);
		this.members = new HashMap<String, Resource>();
	}

	@Override
	public Map<String, Resource> getMembers() {
		return this.members;
	}
}
