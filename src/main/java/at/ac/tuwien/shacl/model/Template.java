package at.ac.tuwien.shacl.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  - Used to encapsulate and parameterize executable bodies based on arguments
	- Templates can be instantiated anywhere where a native constraint may appear (e.g. at sh:constraint)
	- Constraint templates are represented as IRI nodes that are instances of the class sh:ConstraintTemplate
	- A more general superclass sh:Template may be used for other kinds of templates (rules, stored queries, etc.)
	- Well-defined, non-abstract templates must provide at least one executable body using sh:sparql

 * @author xlin
 *
 */
public class Template extends Macro {
	private boolean isAbstract;

	private String labelTemplate;

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public String getLabelTemplate() {
		return labelTemplate;
	}

	public void setLabelTemplate(String labelTemplate) {
		this.labelTemplate = labelTemplate;
	}
	
	
}
