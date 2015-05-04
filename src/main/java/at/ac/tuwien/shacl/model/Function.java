package at.ac.tuwien.shacl.model;

import com.hp.hpl.jena.rdf.model.Resource;

public class Function extends Macro {
	private Resource returnType;

	public Resource getReturnType() {
		return returnType;
	}

	public void setReturnType(Resource returnType) {
		this.returnType = returnType;
	}
	
}
