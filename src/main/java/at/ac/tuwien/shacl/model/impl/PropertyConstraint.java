package at.ac.tuwien.shacl.model.impl;
/**
 * - Constraint that defines restrictions on the values of a given property of a focus node
   - The focus node is the subject and the property is the predicate of relevant triples
 * - Property constraints are instances of sh:PropertyConstraint
 * - When used as values of sh:property, property constraints represented as blank nodes do not require rdf:type
 * - sh:property may also have values that are sub-classes of sh:PropertyConstraint, in this case rdf:type is required
 * - sh:property cannot be used for constraints that are not instances of sh:PropertyConstraint
 * 
 * 
 * 
 * @author xlin
 *
 */
public class PropertyConstraint extends Constraint {
	//Property constraints may have a single value for sh:defaultValue. 
	//It does not have fixed semantics in SHACL, 
	//but may be used by user interface tools to pre-populate input widgets.
	private Object defaultValue;
	
	//get type of property constraint (e.g. allowedValues, datatype, etc.)
	//the property (predicate) where the constraint
	//private Object property;
	
	public void setDefaultValue(Object defaultValue) {
		
	}
}
