package at.ac.tuwien.shacl.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.executable.Executables;
import at.ac.tuwien.shacl.model.Argument;
import at.ac.tuwien.shacl.model.Macro;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;

public class MacroImpl extends SHACLResourceImpl implements Macro {
	private Set<Argument> arguments;
	
	private Map<Property, String> executableBodies;
	
	public MacroImpl(Node node, EnhGraph graph) {
		super(node, graph);
		this.init();
	}
	
	private void init() {
		
	}
	
	@Override
	public Set<Argument> getArguments() {
		if(this.arguments == null) {
			this.extractArguments();
		}
		
		return arguments;
	}
	
	private void extractArguments() {
		this.arguments = new HashSet<Argument>();
		
		for(Statement statement : this.listProperties(SHACL.argument).toList()) {
			Argument argument = SHACLResourceFactory.createArgument(statement.getResource());
			this.arguments.add(argument);
		}
	}

	@Override
	public String getExecutableBody(Property property) {
		if(this.executableBodies == null) {
			this.extractExecutableBodies();
		}
		
		return executableBodies.get(property);
	}
	
	@Override
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

	@Override
	public Boolean isAbstract() {
		return this.getOptionalBooleanOfProperty(SHACL.abstract_);
	}

	@Override
	public Boolean isFinal() {
		return this.getOptionalBooleanOfProperty(SHACL.final_);
	}

	@Override
	public Boolean isPrivate() {
		return this.getOptionalBooleanOfProperty(SHACL.private_);
	}

	@Override
	public Boolean hasExecutableBody() {
		if(executableBodies == null || executableBodies.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}
