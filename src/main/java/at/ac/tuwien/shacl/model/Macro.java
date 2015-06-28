package at.ac.tuwien.shacl.model;

import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Property;

public interface Macro extends SHACLResource {
	public Boolean isAbstract();
	
	public String getExecutableBody(Property property);
	
	public Boolean hasExecutableBody();
	
	public Map<Property, String> getExecutableBodies();
	
	public Set<Argument> getArguments();

	public Boolean isFinal();
	
	public Boolean isPrivate();
}
