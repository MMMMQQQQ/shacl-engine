package at.ac.tuwien.shacl.validation;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public interface Validator {
	public Model validateNode(Resource focusNode, Property property, RDFNode... nodes);

	public Model validateAll(Model model);
	
}
