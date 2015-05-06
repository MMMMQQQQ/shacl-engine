package at.ac.tuwien.shacl.model.impl;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.shacl.model.Macro;

public class MacroImpl extends SHACLResourceImpl implements Macro {

	private String executableBody;
	
	private List<ArgumentImpl> arguments;
	
	public String getExecutableBody() {
		return executableBody;
	}

	public void setExecutableBody(String executableBody) {
		this.executableBody = executableBody;
	}
	
	public List<ArgumentImpl> getArguments() {
		return arguments;
	}

	public void setArguments(List<ArgumentImpl> arguments) {
		this.arguments = arguments;
	}
	
	public void addArgument(ArgumentImpl argument) {
		if(this.arguments == null) {
			arguments = new ArrayList<ArgumentImpl>();
		}
		arguments.add(argument);
	}
	
	public void addArguments(List<ArgumentImpl> arguments) {
		if(this.arguments == null) {
			this.arguments = new ArrayList<ArgumentImpl>();
		}
		this.arguments.addAll(arguments);
	}
}
