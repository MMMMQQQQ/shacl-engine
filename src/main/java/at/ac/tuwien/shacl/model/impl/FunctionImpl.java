package at.ac.tuwien.shacl.model.impl;

import at.ac.tuwien.shacl.model.Function;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Resource;

public class FunctionImpl extends MacroImpl implements Function {
	
	public FunctionImpl(Node node, EnhGraph graph) {
		super(node, graph);
	}
	
	@Override
	public Resource getReturnType() {
		return this.getOptionalResourceOfProperty(SHACL.returnType);
	}

	@Override
	public Boolean isCachable() {
		return this.getOptionalBooleanOfProperty(SHACL.cachable);
	}
}
