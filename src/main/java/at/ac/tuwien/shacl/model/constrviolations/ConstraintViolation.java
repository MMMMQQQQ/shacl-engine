package at.ac.tuwien.shacl.model.constrviolations;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class ConstraintViolation {
	private Resource root;
	
	private Resource subject;
	
	private Property predicate;
	
	private Resource object;
	
	private Resource source;
	
	private Resource detail;
	
	private String message;
	
	private Resource violationType;

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

	public Property getPredicate() {
		return predicate;
	}

	public void setPredicate(Property predicate) {
		this.predicate = predicate;
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

	public Resource getDetail() {
		return detail;
	}

	public void setDetail(Resource detail) {
		this.detail = detail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Resource getViolationType() {
		return violationType;
	}

	public void setViolationType(Resource violationType) {
		this.violationType = violationType;
	}
}
