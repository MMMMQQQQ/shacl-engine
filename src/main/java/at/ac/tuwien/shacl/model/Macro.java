package at.ac.tuwien.shacl.model;

import java.util.ArrayList;
import java.util.List;

public class Macro extends SHACLResource {

	private String executableBody;
	
	private List<Argument> arguments;
	
	public String getExecutableBody() {
		return executableBody;
	}

	public void setExecutableBody(String executableBody) {
		this.executableBody = executableBody;
	}
	
	public List<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}
	
	public void addArgument(Argument argument) {
		if(this.arguments == null) {
			arguments = new ArrayList<Argument>();
		}
		arguments.add(argument);
	}
	
	public void addArguments(List<Argument> arguments) {
		if(this.arguments == null) {
			this.arguments = new ArrayList<Argument>();
		}
		this.arguments.addAll(arguments);
	}
}
