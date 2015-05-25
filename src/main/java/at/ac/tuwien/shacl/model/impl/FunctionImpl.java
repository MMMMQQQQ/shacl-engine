package at.ac.tuwien.shacl.model.impl;

import at.ac.tuwien.shacl.metamodel.Function;

import com.hp.hpl.jena.rdf.model.Resource;

public class FunctionImpl extends SHACLResourceImpl implements Function {
	private Resource returnType;
	
	private boolean cachable;

	public Resource getReturnType() {
		return returnType;
	}

	public void setReturnType(Resource returnType) {
		this.returnType = returnType;
	}

	public boolean isCachable() {
		return cachable;
	}

	public void setCachable(boolean cachable) {
		this.cachable = cachable;
	}
}
