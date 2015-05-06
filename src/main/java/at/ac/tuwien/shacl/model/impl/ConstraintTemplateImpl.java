package at.ac.tuwien.shacl.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.shacl.model.ConstraintTemplate;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class ConstraintTemplateImpl extends TemplateImpl implements ConstraintTemplate  {
	private Resource severity;
	
	private Map<String, String> messages;
	
	private Property predicate;
	
	private List<ResultAnnotation> resultAnnotations;
	
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
}
