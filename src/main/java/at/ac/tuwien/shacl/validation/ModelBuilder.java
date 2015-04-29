package at.ac.tuwien.shacl.validation;

import at.ac.tuwien.shacl.model.SHACLEntity;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public interface ModelBuilder {
	/**
	 * Build a SHACL entity from the RDF nodes.
	 * 
	 * @param focusNode
	 * @param constraint
	 * @return
	 */
	public SHACLEntity build(Resource focusNode, Resource constraint);
	
	/**
	 * Build a violation model from a SHACL entity.
	 * 
	 * @param entity
	 * @return
	 */
	public Model buildViolationModel(SHACLEntity entity);
}
