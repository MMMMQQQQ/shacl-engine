package at.ac.tuwien.shacl.validation;

import at.ac.tuwien.shacl.model.Datatype;
import at.ac.tuwien.shacl.model.HasValue;
import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class DatatypeBuilder extends ModelBuilder {

	@Override
	public SHACLEntity build(Resource focusNode, Resource constraint) {
		Statement datatype= constraint.getProperty(SHACL.datatype);
		System.out.println("in build");
		Datatype datatypeEntity = null;
		
		if(datatype != null) {
			Resource predicate = this.getPredicate(constraint);
			datatypeEntity = new Datatype(focusNode, predicate, datatype.getObject());
		}

		return datatypeEntity;
	}

	@Override
	public Model buildViolationModel(SHACLEntity entity) {
		Model errorModel = ModelFactory.createDefaultModel();
		Datatype e = (Datatype) entity;
		
		Resource error = errorModel.createResource(SHACL.Error);
		error.addProperty(SHACL.root, e.getFocusNode());
		error.addProperty(SHACL.path, e.getPredicate());
		error.addProperty(SHACL.value, e.getDatatypeNode());
		error.addProperty(SHACL.message, "Value does not have node type " + e.getDatatypeNode());
		// TODO Auto-generated method stub
		return null;
	}

}
