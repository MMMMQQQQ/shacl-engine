package at.ac.tuwien.shacl.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.shacl.model.impl.ConstraintViolationType;
import at.ac.tuwien.shacl.model.impl.ResultAnnotation;
import at.ac.tuwien.shacl.util.Config;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class NativeConstraint extends Constraint {	
	private Resource severity;
	
	//only one value (message) per language (key)
	private Map<String, String> messages;
	
	private Property predicate;

	//only one value (comment) per language (key)
	private Map<String, String> comments;
	
	private List<ResultAnnotation> resultAnnotations;
	
	private String executableBody;
	
	//FIXME shacl spec needs to clarify entailment, before it can be implemented
	private String sparqlEntailment;
	
	public void setSeverity(Resource severity) {
		if(ConstraintViolationType.isConstraintViolationType(severity)) {
			this.severity = severity;
		} else {
			//TODO throw some error
		}
	}
	
	public Resource getSeverity() {
		return this.severity;
	}
	
	public void setMessage(String message) {
		this.addMessage(Config.DEFAULT_LANG, message);
	}
	
	public String getMessage() {
		return this.messages.get(Config.DEFAULT_LANG);
	}
	
	public void addMessage(String message) {
		this.addMessage(Config.DEFAULT_LANG, message);
	}
	
	public void addMessage(String lang, String message) {
		if(this.messages==null) {
			this.messages = new HashMap<String, String>();
		}
		
		messages.put(lang, message);
	}
	
	public Map<String, String> getMessages() {
		return this.messages;
	}
	
	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

	public Property getPredicate() {
		return predicate;
	}

	public void setPredicate(Property predicate) {
		this.predicate = predicate;
	}

	public List<ResultAnnotation> getResultAnnotations() {
		return resultAnnotations;
	}

	public void setResultAnnotations(List<ResultAnnotation> resultAnnotations) {
		this.resultAnnotations = resultAnnotations;
	}
	
	public void addResultAnnotation(ResultAnnotation annotation) {
		if(resultAnnotations==null) {
			this.resultAnnotations = new ArrayList<ResultAnnotation>();
		}
		this.resultAnnotations.add(annotation);
	}
	
	public String getExecutableBody() {
		return executableBody;
	}

	public void setExecutableBody(String executableBody) {
		this.executableBody = executableBody;
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
}
