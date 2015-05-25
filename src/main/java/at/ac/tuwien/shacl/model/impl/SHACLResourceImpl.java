package at.ac.tuwien.shacl.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SHACLResourceImpl {
	//only one value (label) per language (key)
	private Map<String, String> labels;
	
	//only one value (comment) per language (key)
	private Map<String, String> comments;
	
	//use this, if there is only one value for label or comment
	private String defaultLang;
	
	private boolean isAbstract;

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
	
	public boolean isAbstract() {
		return this.isAbstract;
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	

	private String executableBody;
	
	private Set<ArgumentImpl> arguments;
	
	public String getExecutableBody() {
		return executableBody;
	}

	public void setExecutableBody(String executableBody) {
		this.executableBody = executableBody;
	}
	
	public Set<ArgumentImpl> getArguments() {
		return arguments;
	}

	public void setArguments(Set<ArgumentImpl> arguments) {
		this.arguments = arguments;
	}
	
	public void addArgument(ArgumentImpl argument) {
		if(this.arguments == null) {
			arguments = new HashSet<ArgumentImpl>();
		}
		arguments.add(argument);
	}
	
	public void addArguments(Set<ArgumentImpl> arguments) {
		if(this.arguments == null) {
			this.arguments = new HashSet<ArgumentImpl>();
		}
		this.arguments.addAll(arguments);
	}
}
