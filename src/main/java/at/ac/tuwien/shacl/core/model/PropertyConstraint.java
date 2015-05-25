package at.ac.tuwien.shacl.core.model;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.util.Config;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

//implements the sh:property and sh:inverseProperty of a Shape
public class PropertyConstraint extends Constraint {
	private Property predicate;
	
	private Map<String, String> labels;
	
	private Map<String, String> comments;
	
	//key: name of the property constraint, value: value of the property constraint
	private Map<Property, RDFNode> constraints;

	public Property getPredicate() {
		return predicate;
	}

	public void setPredicate(Property predicate) {
		this.predicate = predicate;
	}
	
	public void addLabel(String lang, String label) {
		if(this.labels == null) {
			this.labels = new HashMap<String, String>();
		}
		this.labels.put(lang, label);
	}
	
	public void addLabel(String label) {
		this.addLabel(Config.DEFAULT_LANG, label);
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}
	
	public void addComment(String lang, String comment) {
		if(this.comments == null) {
			this.comments = new HashMap<String, String>();
		}
		this.comments.put(lang, comment);
	}
	
	public void addComment(String comment) {
		this.addComment(Config.DEFAULT_LANG, comment);
	}

	public void setComments(Map<String, String> comments) {
		this.comments = comments;
	}

	public Map<Property, RDFNode> getConstraints() {
		return constraints;
	}
	
	public void addConstraint(Property name, RDFNode value) {
		if(this.constraints == null) {
			this.constraints = new HashMap<Property, RDFNode>();
		}
		
		this.constraints.put(name, value);
	}

	public void setConstraints(Map<Property, RDFNode> propertyConstraints) {
		this.constraints = propertyConstraints;
	}
}
