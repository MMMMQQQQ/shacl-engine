package at.ac.tuwien.shacl.validation;

import at.ac.tuwien.shacl.model.MinMaxCount;
import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class MinMaxCountBuilder extends ModelBuilder {

	@Override
	public MinMaxCount build(Resource focusNode, Resource constraint) {
		MinMaxCount entity = null;
		
		RDFNode minCount = constraint.getProperty(SHACL.minCount).getObject();
		RDFNode maxCount = constraint.getProperty(SHACL.maxCount).getObject();
		Resource predicate = this.getPredicate(constraint);
		if(minCount != null || maxCount != null) {
			entity = new MinMaxCount(focusNode, predicate, minCount, maxCount);
		}
		return entity;
	}

	@Override
	public Model buildViolationModel(SHACLEntity entity) {
		Model errorModel = ModelFactory.createDefaultModel();
		MinMaxCount mmCount = (MinMaxCount) entity;
		
		//TODO find a way to get count value for error message
		//int count = mmCount.getFocusNode().listProperties(mmCount.getPredicate().).toList().size()

		Resource error = errorModel.createResource(SHACL.Error);
		error.addProperty(SHACL.root, mmCount.getFocusNode());
		error.addProperty(SHACL.path, mmCount.getPredicate());
		error.addProperty(SHACL.message, "Cardinality does not match [" + 
				(mmCount.getMinCountNode() != null ? mmCount.getMinCountNode().asLiteral().getLexicalForm() : "") + ".." +
				(mmCount.getMinCountNode() != null ? mmCount.getMinCountNode().asLiteral().getLexicalForm() : "") + "]");
		return null;
	}

}
