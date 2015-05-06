package at.ac.tuwien.shacl.model;

import com.hp.hpl.jena.rdf.model.Resource;

public interface Function extends Macro {
	public Resource getReturnType();
	
	public void setReturnType(Resource returnType);

	public boolean isCachable();

	public void setCachable(boolean cachable);
}
