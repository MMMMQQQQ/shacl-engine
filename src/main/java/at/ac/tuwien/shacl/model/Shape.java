package at.ac.tuwien.shacl.model;

import java.util.Set;

import com.hp.hpl.jena.rdf.model.Resource;

public interface Shape extends SHACLResource {
	public Constraint getConstraint(String uri);
	
	public Set<Constraint> getConstraints();
	
	public Resource getScopeClass();

	public Set<Shape> getScopeShapes();
}
