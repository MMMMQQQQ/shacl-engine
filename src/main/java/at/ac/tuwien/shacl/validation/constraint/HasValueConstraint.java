package at.ac.tuwien.shacl.validation.constraint;

import at.ac.tuwien.shacl.validation.Validator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class HasValueConstraint implements Validator {

	public Model validateNode(Resource resource, Property property,
			RDFNode... nodes) {
		
		// TODO Auto-generated method stub
		return null;
	}

	public Model validateAll(Model model) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected Model getErrorModel() {
		return null;
	}
}
