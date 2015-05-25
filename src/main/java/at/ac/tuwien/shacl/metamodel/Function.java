package at.ac.tuwien.shacl.metamodel;

import com.hp.hpl.jena.rdf.model.Resource;

public interface Function extends SHACLResource {
	public Resource getReturnType();
	
	public void setReturnType(Resource returnType);

	public boolean isCachable();

	public void setCachable(boolean cachable);
}
