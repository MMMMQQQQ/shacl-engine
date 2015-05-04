package at.ac.tuwien.shacl.model;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 8.1 Template Arguments and 12.1 Function Arguments
 * 	- Arguments of a template are attached via sh:argument 
 * 	  to either a template or a function
	- Each (sh:)argument must be an instance of sh:Argument
	- The rdf:type triple of the argument can be omitted, 
	  if it is a blank node with an incoming sh:argument triple
 *  - Each sh:Argument may declare one value for sh:datatype or 
 *    one value for sh:valueType (interpretat)
 * 
 * @author xlin
 *
 */
public class Argument extends SHACLResource {
	//Each sh:Argument must have exactly one value for sh:predicate
	//The values of sh:predicate must be IRIs
	//The local names of the values of sh:predicate must match the conditions:
	//	- local name must be a valid SPARQL VARNAME
	//	- no other declared sh:Argument for the same template that has a sh:predicate with the same local name
	private Resource predicate;

	//sh:Argument with property sh:optional of true indicates that the argument is not mandatory
	private boolean optional;
	
	//sh:Argument with sh:defaultValue can be used to declare default values for template instances
	private Object defaultValue;
	
	//sh:Argument with property sh:valueType can be used to prevent execution of a template with invalid arguments
	private Resource valueType;
	
	//in spec only for functions, but in SHACL definition files,
	//datatypes are also used for templates
	//Each sh:Argument may declare one value for sh:datatype
	private Resource datatype;

	public Resource getPredicate() {
		return predicate;
	}

	public void setPredicate(Resource predicate) {
		this.predicate = predicate;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Resource getValueType() {
		return valueType;
	}

	public void setValueType(Resource valueType) {
		if(this.getDatatype() == null) {
			this.valueType = valueType;
		} else {
			//argument can only have either datatype or valuetype
			//TODO throw something
		}
	}

	public Resource getDatatype() {
		return datatype;
	}

	public void setDatatype(Resource datatype) {
		if(this.getValueType() == null) {
			this.datatype = datatype;
		} else {
			//argument can only have either datatype or valuetype
			//TODO throw something
		}
	}
}
