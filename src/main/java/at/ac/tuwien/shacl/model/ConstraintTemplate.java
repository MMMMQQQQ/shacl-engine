package at.ac.tuwien.shacl.model;

import java.util.List;
import java.util.Map;

import at.ac.tuwien.shacl.model.impl.ResultAnnotation;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public interface ConstraintTemplate extends Template {
	public void setSeverity(Resource severity);
	
	public Resource getSeverity();
	
	public void addMessage(String lang, String message);
	
	public Map<String, String> getMessages();
	
	public void setMessages(Map<String, String> messages);

	public Property getPredicate();

	public void setPredicate(Property predicate);

	public List<ResultAnnotation> getResultAnnotations();
	
	public void setResultAnnotations(
			List<ResultAnnotation> resultAnnotations);
	
	public void addResultAnnotation(ResultAnnotation annotation);
}
