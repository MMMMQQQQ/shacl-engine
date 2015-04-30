package at.ac.tuwien.shacl.validation;

import at.ac.tuwien.shacl.model.AllowedValues;
import at.ac.tuwien.shacl.model.HasValue;
import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class AllowedValuesBuilder extends ModelBuilder {

	@Override
	public AllowedValues build(Resource focusNode, Resource constraint) {
		Statement allowedValues = constraint.getProperty(SHACL.allowedValues);
		
		AllowedValues allowedValuesEntity = null;
		
		if(allowedValues != null && allowedValues.getObject().isResource()) {
			Resource predicate = this.getPredicate(constraint);
			allowedValuesEntity = new AllowedValues(focusNode, predicate, allowedValues.getObject());
			System.out.println("allowedValues: "+allowedValues.getObject());
		}
		
		return allowedValuesEntity;
	}

	@Override
	public Model buildViolationModel(SHACLEntity entity) {
		Model errorModel = ModelFactory.createDefaultModel();
		AllowedValues avEntity = (AllowedValues) entity;
		Resource error = errorModel.createResource(SHACL.Error);
		error.addProperty(SHACL.root, avEntity.getFocusNode());
		error.addProperty(SHACL.path, avEntity.getPredicate());
		error.addProperty(SHACL.value, avEntity.getAllowedValuesNode());
		error.addProperty(SHACL.message, "Value " + avEntity.getAllowedValuesNode() + " not among the allowed values set ");

		return errorModel;
	}

}
