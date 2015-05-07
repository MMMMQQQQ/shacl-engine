package at.ac.tuwien.shacl.model.impl;

import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import at.ac.tuwien.shacl.model.NativeConstraint;
import at.ac.tuwien.shacl.model.ResultAnnotation;

public class NativeConstraintImpl extends SHACLResourceImpl implements NativeConstraint {
	private String executableBody;
	
	private Resource severity;
	
	private Property predicate;
	
	@Override
	public void setExecutableBody(String executable) {
		this.executableBody = executable;
	}

	@Override
	public String getExecutableBody() {
		return this.executableBody;
	}

	@Override
	public void setSeverity(Resource severity) {
		//implement
	}

	@Override
	public Resource getSeverity() {
		return null;
	}

	@Override
	public void setMessages(Map<String, String> messages) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMessage(String lang, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> getMessages(Map<String, String> messages) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPredicate(Property predicate) {
		this.predicate = predicate;
	}

	@Override
	public Property getPredicate() {
		return this.predicate;
	}

	@Override
	public void setResultAnnotations(List<ResultAnnotation> resultAnnotations) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addResultAnnotation(ResultAnnotation resultAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ResultAnnotation> getResultAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

}
