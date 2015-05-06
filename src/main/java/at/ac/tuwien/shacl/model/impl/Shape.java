package at.ac.tuwien.shacl.model.impl;

import java.util.List;

/**
 * A Shape is a group of constraints that have the same focus node(s)
 * Shapes are instances of the class sh:Shape 
 * (or rdfs:Class, see issue 2 above)
 * 
 * @author xlin
 *
 */
public class Shape extends SHACLResourceImpl {
	List<Constraint> constraints;
}
