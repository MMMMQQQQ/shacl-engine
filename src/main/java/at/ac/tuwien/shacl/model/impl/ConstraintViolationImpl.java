package at.ac.tuwien.shacl.model.impl;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.metamodel.ConstraintViolation;
import at.ac.tuwien.shacl.util.Config;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class ConstraintViolationImpl extends SHACLResourceImpl implements ConstraintViolation {
	//key: lang, value: message
	private Map<String, String> messages;
	
	//sh:detail: links a (parent) constraint violation with one or more other (child) constraint violations to provide further details about the cause of the (parent) violation; may include failures of nested constraints (via sh:valueShape)
	private Object detail;
	
	//linkable through "rdfs:subClassOf sh:ConstraintViolation"
	//three currently: warning, error and fatalerror
	private Resource type;
	
	//sh:root: IRI or blank node that produced the violation, usually the focus node
	private Resource root;
	
	private Resource subject;
	
	private Property predicate;
	
	private Resource object;
	
	//sh:source: constraint violation (one sh:Constraint) that caused the violation
	private Resource source;

	public ConstraintViolationImpl(Resource type, Resource root, Resource subject, Property predicate, Resource object) {
		this.setType(type);
		this.setRoot(root);
		this.setSubject(subject);
		this.setPredicate(predicate);
		this.setObject(object);
	}
	
	public Resource getType() {
		return type;
	}

	public void setType(Resource type) {
		if(type == null) {
			this.type = Config.DEFAULT_SEVERITY;
		} else if(ConstraintViolationType.isConstraintViolationType(type)) {
			this.type = type;
		}
	}

	public Resource getRoot() {
		return root;
	}

	public void setRoot(Resource root) {
		this.root = root;
	}

	public Resource getSubject() {
		return subject;
	}

	public void setSubject(Resource subject) {
		this.subject = subject;
	}

	public Resource getObject() {
		return object;
	}

	public void setObject(Resource object) {
		this.object = object;
	}

	public Resource getSource() {
		return source;
	}

	public void setSource(Resource source) {
		this.source = source;
	}

	public Property getPredicate() {
		return predicate;
	}

	public void setPredicate(Property predicate) {
		this.predicate = predicate;
	}
	
	public Model getModel() {
		Model errorModel = ModelFactory.createDefaultModel();
		
		Resource error = errorModel.createResource(this.type);

		if(this.getObject() != null) error.addProperty(SHACL.object, this.getObject());
		if(this.getPredicate() != null) error.addProperty(SHACL.predicate, this.getPredicate());
		if(this.getSubject() != null) error.addProperty(SHACL.subject, this.getSubject());
		if(this.getRoot() != null) error.addProperty(SHACL.root, this.getRoot());
		for(Map.Entry<String, String> message : messages.entrySet()) {
			if(message.getValue() != null) {
				error.addProperty(SHACL.message, message.getValue());
			}
		}
		
		return errorModel;
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
	
	public void addMessage(String message) {
		this.addMessage(Config.DEFAULT_LANG, message);
	}

	public Object getDetail() {
		return detail;
	}

	public void setDetail(Object detail) {
		this.detail = detail;
	}
}
