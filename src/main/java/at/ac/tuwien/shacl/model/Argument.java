package at.ac.tuwien.shacl.model;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public interface Argument extends SHACLResource {
	public Resource getPredicate();

	public void setPredicate(Resource predicate);

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
