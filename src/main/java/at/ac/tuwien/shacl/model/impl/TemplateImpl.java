package at.ac.tuwien.shacl.model.impl;


/**
 *  - Used to encapsulate and parameterize executable bodies based on arguments
	- Templates can be instantiated anywhere where a native constraint may appear (e.g. at sh:constraint)
	- Constraint templates are represented as IRI nodes that are instances of the class sh:ConstraintTemplate
	- A more general superclass sh:Template may be used for other kinds of templates (rules, stored queries, etc.)
	- Well-defined, non-abstract templates must provide at least one executable body using sh:sparql

 * @author xlin
 *
 */
public class TemplateImpl extends SHACLResourceImpl {
	private String labelTemplate;

	public String getLabelTemplate() {
		return labelTemplate;
	}

	public void setLabelTemplate(String labelTemplate) {
		this.labelTemplate = labelTemplate;
	}
	
	/**
	 * A template must either be abstract or have an executable body.
	 * Check, if template is valid under those circumstances.
	 * 
	 * @return
	 */
	public boolean isValidTemplate() {
		if(this.isAbstract() && this.getExecutableBody()==null) {
			return true;
		} else if(!this.isAbstract() && this.getExecutableBody()!=null) {
			return true;
		} else {
			return false;
		}
	}
}
