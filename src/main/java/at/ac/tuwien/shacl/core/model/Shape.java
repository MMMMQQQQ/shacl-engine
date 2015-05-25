package at.ac.tuwien.shacl.core.model;

import java.util.Map;

import com.hp.hpl.jena.rdf.model.Resource;

public class Shape {
	//constraints can be sh:property, sh:constraint and inverseProperty
	private Map<String, Constraint> constraints;
	
	private Map<String, Shape> scopeShapes;
	
	private Resource scopeClass;
	
	public void addConstraint(String predicateName, Constraint constraint) {
		
	}
	
	public Constraint getConstraint(String predicateName) {
		return this.constraints.get(predicateName);
	}
}
