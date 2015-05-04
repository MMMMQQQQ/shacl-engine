package at.ac.tuwien.shacl.model;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class SHACLResource {
	//only one value (label) per language (key)
	private Map<String, String> labels;
	
	//only one value (comment) per language (key)
	private Map<String, String> comments;
	
	//use this, if there is only one value for label or comment
	private String defaultLang;

	public Map<String, String> getLabels() {
		return labels;
	}
	
	public void addLabel(String lang, String label) {
		if(this.labels == null) {
			this.labels = new HashMap<String, String>();
		}
		this.labels.put(lang, label);
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public Map<String, String> getComments() {
		return comments;
	}
	
	public void addComment(String lang, String comment) {
		if(this.comments == null) {
			this.comments = new HashMap<String, String>();
		}
		this.comments.put(lang, comment);
	}

	public void setComments(Map<String, String> comments) {
		this.comments = comments;
	}

	public String getDefaultLang() {
		return defaultLang;
	}

	public void setDefaultLang(String defaultLang) {
		this.defaultLang = defaultLang;
	}
}
