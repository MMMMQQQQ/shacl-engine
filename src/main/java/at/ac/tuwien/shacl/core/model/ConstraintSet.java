package at.ac.tuwien.shacl.core.model;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.metamodel.Argument;
import at.ac.tuwien.shacl.metamodel.Template;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class ConstraintSet {
	private Map<Property, RDFNode> constraints;
	
	private Template template;
	
	public ConstraintSet(Template template) {
		this.constraints = new HashMap<Property, RDFNode>();
		this.template = template;
		for(Argument a : template.getArguments()) {
			constraints.put(a.getPredicate(), null);
		}
	}
	
	public boolean isComplete() {
		boolean isComplete = true;
		
		for(Map.Entry<Property, RDFNode> c : constraints.entrySet()) {
			if(c.getValue() == null) {
				isComplete = false;
				break;
			}
		}
		
		return isComplete;
	}
	
	public void addConstraint(Property name, RDFNode value) {
		//TODO add check that only properties are set that are part of this set
		this.constraints.put(name, value);
	}
	
	public Map<Property, RDFNode> getConstraints() {
		return this.constraints;
	}
	
	public Template getTemplate() {
		return this.template;
	}
}
