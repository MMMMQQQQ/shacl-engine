package at.ac.tuwien.shacl.model;

import java.util.List;

import at.ac.tuwien.shacl.model.impl.ArgumentImpl;

public interface Macro extends SHACLResource {
	public String getExecutableBody();

	public void setExecutableBody(String executableBody);
	
	public List<ArgumentImpl> getArguments();

	public void setArguments(List<ArgumentImpl> arguments);
	
	public void addArgument(ArgumentImpl argument);
	
	public void addArguments(List<ArgumentImpl> arguments);
}
