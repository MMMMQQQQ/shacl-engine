package at.ac.tuwien.shacl.core.model;

import at.ac.tuwien.shacl.metamodel.Template;

//implement a general shape constraint
public class TemplateConstraint extends Constraint {
	//reference to the template type (rdf:type)
	private Template template;
	
	public TemplateConstraint(Template template) {
		this.template = template;
	}

	public Template getTemplate() {
		return template;
	}
	
	public String getTemplateExecutableBody() {
		return template.getExecutableBody();
	}
}
