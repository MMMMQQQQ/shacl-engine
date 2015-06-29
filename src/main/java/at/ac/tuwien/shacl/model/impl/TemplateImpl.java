package at.ac.tuwien.shacl.model.impl;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.model.Template;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;


/**
 *  - Used to encapsulate and parameterize executable bodies based on arguments
	- Templates can be instantiated anywhere where a native constraint may appear (e.g. at sh:constraint)
	- Constraint templates are represented as IRI nodes that are instances of the class sh:ConstraintTemplate
	- A more general superclass sh:Template may be used for other kinds of templates (rules, stored queries, etc.)
	- Well-defined, non-abstract templates must provide at least one executable body using sh:sparql

 * @author xlin
 *
 */
public class TemplateImpl extends MacroImpl implements Template {
	private Map<String, String> labelTemplates;
	
	public TemplateImpl(Node node, EnhGraph graph) {
		super(node, graph);
		this.labelTemplates = new HashMap<String, String>();
	}

	@Override
	public Map<String, String> getLabelTemplates() {
		//TODO implement
		if(this.labelTemplates == null) {
			this.labelTemplates = new HashMap<String, String>();
		}
		return labelTemplates;
	}

	/**
	 * A template must either be abstract or have an executable body.
	 * Check, if template is valid under those circumstances.
	 * 
	 * @return
	 */
	protected boolean isValidTemplate() {		
		if(!this.isAbstract() && !this.hasExecutableBody()) {
			return false;
		} else {
			return true;
		}
	}
}
