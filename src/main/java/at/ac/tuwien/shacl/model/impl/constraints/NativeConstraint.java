package at.ac.tuwien.shacl.model.impl.constraints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.shacl.executable.Executables;
import at.ac.tuwien.shacl.model.impl.ConstraintImpl;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class NativeConstraint extends ConstraintImpl {	

	private List<ResultAnnotation> resultAnnotations;
	
	private Map<Property, String> executableBodies;

	public NativeConstraint(Node node, EnhGraph graph) {
		super(node, graph);
	}

	public Resource getSeverity() {
		return this.getOptionalResourceOfProperty(SHACL.severity);
	}

	public Resource getPredicate() {
		return this.getOptionalResourceOfProperty(SHACL.predicate);
	}

	public List<ResultAnnotation> getResultAnnotations() {
		return resultAnnotations;
	}

	public String getExecutableBody(Property property) {
		if(this.executableBodies == null) {
			this.extractExecutableBodies();
		}
		
		return executableBodies.get(property);
	}

	public Map<Property, String> getExecutableBodies() {
		if(this.executableBodies == null) {
			this.extractExecutableBodies();
		}
		
		return executableBodies;
	}
	
	private void extractExecutableBodies() {
		this.executableBodies = new HashMap<Property, String>();
		
		for(Property execProperty : Executables.getCommands()) {
			String executable = this.getOptionalStringOfProperty(execProperty);
			executableBodies.put(execProperty, executable);
		}
	}
}
