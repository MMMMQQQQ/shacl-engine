package at.ac.tuwien.shacl.model.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.model.Constraint;
import at.ac.tuwien.shacl.model.Shape;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Property;

public class ConstraintImpl extends SHACLResourceImpl implements Constraint {

	private Property constraintType;
	
	public ConstraintImpl(Node node, EnhGraph graph) {
		super(node, graph);
	}

	/**
	 * TODO
	 *  Shapes or constraints may have a property sh:scopeShape to declare scopes that serves as a 
	 *  pre-conditions to narrow down when the constraint(s) applies. 
	 *  The values of sh:scopeShape must be shape definitions. 
	 *  These shapes must be evaluated before the constraint is evaluated. 
	 *  If the scope shape returns an error-level constraint violation, then the constraint must be ignored. 
	 *  Values of sh:scopeShape that are blank nodes do not require an rdf:type triple - the default type is sh:Shape. 
	 * 
	 */
	@Override
	public Set<Shape> getScopeShapes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Property getConstraintType() {
		return constraintType;
	}

	@Override
	public void setConstraintType(Property constraintProperty) {
		//TODO throw exception, if this variable isnt null and input parameter and class variable differ from each other
		this.constraintType = constraintProperty;
	}

}
