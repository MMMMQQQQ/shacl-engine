package at.ac.tuwien.shacl.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.shacl.metamodel.ConstraintTemplate;
import at.ac.tuwien.shacl.util.Config;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class ConstraintTemplateImpl extends TemplateImpl implements ConstraintTemplate  {
	private Resource severity;
	
	private Map<String, String> messages;
	
	private Property predicate;
	
	private List<ResultAnnotation> resultAnnotations;
	
	public ConstraintTemplateImpl() {
		this.messages = new HashMap<String, String>();
		this.resultAnnotations = new ArrayList<ResultAnnotation>();
	}
	
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
		messages.put(lang, message);
	}
	
	public Map<String, String> getMessages() {
		return this.messages;
	}
	
	public String getMessage() {
		return this.messages.get(Config.DEFAULT_LANG);
	}
	
	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

//	public Property getPredicate() {
//		return predicate;
//	}
//
//	public void setPredicate(Property predicate) {
//		this.predicate = predicate;
//	}

	public List<ResultAnnotation> getResultAnnotations() {
		return resultAnnotations;
	}

	public void setResultAnnotations(List<ResultAnnotation> resultAnnotations) {
		this.resultAnnotations = resultAnnotations;
	}
	
	public void addResultAnnotation(ResultAnnotation annotation) {
		this.resultAnnotations.add(annotation);
	}
}
