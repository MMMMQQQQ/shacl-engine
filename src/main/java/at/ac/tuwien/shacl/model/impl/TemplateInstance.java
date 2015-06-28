package at.ac.tuwien.shacl.model.impl;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.model.Argument;
import at.ac.tuwien.shacl.model.Template;

import com.hp.hpl.jena.rdf.model.RDFNode;

public class TemplateInstance {
	private Template template;
	
	private Map<Argument, RDFNode> variables;
	
	public TemplateInstance(Template template) {
		this.template = template;
		this.variables = new HashMap<Argument, RDFNode>();
		
		for(Argument a : template.getArguments()) {
			variables.put(a, null);
		}
	}
	
	public void addArgumentValue(Argument argument, RDFNode value) {
		if(variables.containsKey(argument) && variables.get(argument) == null) {
			variables.put(argument, value);
		} else {
			//TODO throw exception
		}
	}
	
	public Map<Argument, RDFNode> getArgumentValues() {
		return this.variables;
	}
	
	public Template getTemplate() {
		return this.template;
	}
	
	public boolean isComplete() {
		for(Map.Entry<Argument,RDFNode> variable : variables.entrySet()) {
			if(variable.getValue() == null && !variable.getKey().isOptional()) {
				return false;
			}
		}
		
		return true;
	}
	
//	public Map<String, RDFNode> createMappingFromArgs() {
//		Map<String, RDFNode> mapping = 
//	}
}
