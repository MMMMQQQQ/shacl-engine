package at.ac.tuwien.shacl.model;

import java.util.Set;

import com.hp.hpl.jena.rdf.model.Property;

public interface Constraint extends SHACLResource {
//	public Model validateConstraint(Resource focusNode, Model model);
//	
	public Set<Shape> getScopeShapes();

	/**
	 * Link to the shape
	 * 
	 * @param shapeUri
	 * @return
	 */
	public Property getConstraintType();
	
	public void setConstraintType(Property constraintProperty);
}
