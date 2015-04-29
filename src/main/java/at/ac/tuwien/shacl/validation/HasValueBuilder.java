package at.ac.tuwien.shacl.validation;

import at.ac.tuwien.shacl.model.HasValue;
import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class HasValueBuilder implements ModelBuilder {
	
	public HasValue build(Resource focusNode, Resource constraint) {
		Statement hasValue = constraint.getProperty(SHACL.hasValue);
		
		HasValue hasValueEntity = null;
		
		if(hasValue != null) {
			Resource predicate = constraint.getProperty(SHACL.predicate).getObject().asResource();
			hasValueEntity = new HasValue(focusNode, predicate, hasValue.getObject());
		}
		
		return hasValueEntity;
	}
	
	public Model buildViolationModel(SHACLEntity entity) {
		Model errorModel = ModelFactory.createDefaultModel();
		HasValue hv = (HasValue) entity;
		
		Resource error = errorModel.createResource(SHACL.Error);
		error.addProperty(SHACL.root, hv.getFocusNode());
		error.addProperty(SHACL.path, hv.getPredicate());
		error.addProperty(SHACL.message, "Missing required value " + hv.getHasValue());
		errorModel.write(System.out, "TURTLE");
		return errorModel;
	}
}
