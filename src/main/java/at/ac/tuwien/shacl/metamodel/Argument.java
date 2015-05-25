package at.ac.tuwien.shacl.metamodel;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public interface Argument extends SHACLResource {
	public Property getPredicate();

	public void setPredicate(Property predicate);

	public boolean isOptional();

	public void setOptional(boolean optional);

	public RDFNode getDefaultValue();

	public void setDefaultValue(RDFNode defaultValue);

	public Resource getValueType();

	public void setValueType(Resource valueType);

	public Resource getDatatype();

	public void setDatatype(Resource datatype);
	
	public void setOptionalWhenInherited(boolean optionalWhenInherited);
	
	public boolean getOptionalWhenInherited();
	
	public void setNodeKind(Resource nodeKind);
	
	public Resource getNodeKind();
}
