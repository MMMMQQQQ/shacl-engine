package at.ac.tuwien.shacl.model.impl;

import at.ac.tuwien.shacl.model.Argument;
import at.ac.tuwien.shacl.util.ParsingUtil;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.RDFNode;
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
public class ArgumentImpl extends SHACLResourceImpl implements Argument {

	public ArgumentImpl(Node node, EnhGraph graph) {
		super(node, graph);
	}

	//Each sh:Argument must have exactly one value for sh:predicate
	//The values of sh:predicate must be IRIs
	//The local names of the values of sh:predicate must match the conditions:
	//	- local name must be a valid SPARQL VARNAME
	//	- no other declared sh:Argument for the same template that has a sh:predicate with the same local name
	@Override
	public Resource getPredicate() {
		ParsingUtil.checkCardinalityExactlyOne(this, SHACL.predicate);
		
		return this.getPropertyResourceValue(SHACL.predicate);
	}

	//sh:Argument with property sh:optional of true indicates that the argument is not mandatory
	@Override
	public Boolean isOptional() {
		return this.getOptionalBooleanOfProperty(SHACL.optional);
	}

	//sh:Argument with sh:defaultValue can be used to declare default values for template instances
	@Override
	public RDFNode getDefaultValue() {
		return this.getOptionalNodeOfProperty(SHACL.defaultValue);
	}

	//sh:Argument with property sh:valueType can be used to prevent execution of a template with invalid arguments
	@Override
	public Resource getValueClass() {
		return this.getOptionalResourceOfProperty(SHACL.valueClass);
	}

	//Each sh:Argument may declare one value for sh:datatype
	@Override
	public Resource getDatatype() {
		return this.getOptionalResourceOfProperty(SHACL.datatype);
	}

	//True to indicate that the property does not require a value when used by a subclass template. 
	//If set to true, then instances of subclasses do not need to fill in all required arguments - 
	//incomplete templates will simply not be executed. By default, the value is required.
	@Override
	public Boolean getOptionalWhenInherited() {
		return this.getOptionalBooleanOfProperty(SHACL.optionalWhenInherited);
	}

	@Override
	public Resource getNodeKind() {
		return this.getOptionalResourceOfProperty(SHACL.nodeKind);
	}
}
