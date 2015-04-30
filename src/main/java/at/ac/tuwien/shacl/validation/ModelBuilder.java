package at.ac.tuwien.shacl.validation;

import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public abstract class ModelBuilder {
	protected Resource getPredicate(Resource constraint) {
		return constraint.getProperty(SHACL.predicate).getObject().asResource();
	}
	
	/**
	 * Build a SHACL entity from the RDF nodes.
	 * 
	 * @param focusNode
	 * @param constraint
	 * @return
	 */
	public abstract SHACLEntity build(Resource focusNode, Resource constraint);
	
	/**
	 * Build a violation model from a SHACL entity.
	 * 
	 * @param entity
	 * @return
	 */
	public abstract Model buildViolationModel(SHACLEntity entity);
}
