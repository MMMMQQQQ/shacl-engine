package at.ac.tuwien.shacl.model;

import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public interface NativeConstraint extends SHACLResource {
	public void setExecutableBody(String executable);
	
	public String getExecutableBody();
	
	public void setSeverity(Resource severity);
	
	public Resource getSeverity();
	
	public void setMessages(Map<String, String> messages);
	
	public void addMessage(String lang, String message);
	
	public Map<String, String> getMessages(Map<String, String> messages);
	
	public void setPredicate(Property predicate);
	
	public Property getPredicate();
	
	public void setResultAnnotations(List<ResultAnnotation> resultAnnotations);
	
	public void addResultAnnotation(ResultAnnotation resultAnnotation);
	
	public List<ResultAnnotation> getResultAnnotations();
}
