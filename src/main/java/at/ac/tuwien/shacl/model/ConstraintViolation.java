package at.ac.tuwien.shacl.model;

import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public interface ConstraintViolation extends SHACLResource {
	public Resource getType();

	public void setType(Resource type);

	public Resource getRoot();

	public void setRoot(Resource root);
	
	public Resource getSubject();

	public void setSubject(Resource subject);

	public Resource getObject();

	public void setObject(Resource object);

	public Resource getSource();

	public void setSource(Resource source);

	public Property getPredicate();

	public void setPredicate(Property predicate);
	
	public Model getModel();
	
	public void addMessage(String lang, String message);
	
	public Map<String, String> getMessages();
	
	public void setMessages(Map<String, String> messages);

	public Object getDetail();

	public void setDetail(Object detail);
}
