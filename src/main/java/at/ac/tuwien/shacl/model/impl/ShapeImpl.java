package at.ac.tuwien.shacl.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.model.Constraint;
import at.ac.tuwien.shacl.model.Shape;
import at.ac.tuwien.shacl.registry.SHACLModelRegistry;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class ShapeImpl extends SHACLResourceImpl implements Shape {
	private Map<String, Constraint> constraints;
	
	public ShapeImpl(Node node, EnhGraph graph) {
		super(node, graph);
		this.init();
	}
	
	private void init() {
		this.constraints = new HashMap<String, Constraint>();
		this.extractConstraints(SHACL.constraint);
		this.extractConstraints(SHACL.property);
		this.extractConstraints(SHACL.inverseProperty);
  	}
	
	protected void extractConstraints(Property constraintType) {
		for(Statement s : this.listProperties(constraintType).toList()) {
			Resource constraintNode = s.getResource();
			Constraint constraint = null;
			
			if(constraintNode.getURI() == null) {
				//anonymous constraint
				constraint = SHACLResourceFactory.createConstraint(constraintNode);
				this.constraints.put(constraint.getId().getLabelString(), constraint);
			} else {
				constraint = SHACLModelRegistry.get()
						.getNamedConstraint(constraintNode.getURI());
				
				if(constraint == null) {
					constraint = SHACLResourceFactory.createConstraint(constraintNode);
					SHACLModelRegistry.get().registerNamedConstraint(constraint);
				}
				
				this.constraints.put(constraint.getURI(), constraint);
			}
			
			constraint.setConstraintType(constraintType);
		}
	}

	@Override
	public Constraint getConstraint(String uri) {
		return this.constraints.get(uri);
	}

	@Override
	public Set<Constraint> getConstraints() {
		return new HashSet<Constraint>(this.constraints.values());
	}

	@Override
	public Resource getScopeClass() {
		return this.getOptionalResourceOfProperty(SHACL.scopeClass);
	}

	@Override
	public Set<Shape> getScopeShapes() {
		return null;
	}
}
