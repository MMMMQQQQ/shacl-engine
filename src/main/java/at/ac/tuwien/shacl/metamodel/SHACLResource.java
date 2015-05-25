package at.ac.tuwien.shacl.metamodel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.model.impl.ArgumentImpl;

public interface SHACLResource {
	public Map<String, String> getLabels();
	
	public void addLabel(String lang, String label);

	public void setLabels(Map<String, String> labels);

	public Map<String, String> getComments();
	
	public void addComment(String lang, String comment);

	public void setComments(Map<String, String> comments);
	
	public boolean isAbstract();
	
	public void setAbstract(boolean isAbstract);
	
	public String getExecutableBody();

	public void setExecutableBody(String executableBody);
	
	public Set<ArgumentImpl> getArguments();

	public void setArguments(Set<ArgumentImpl> arguments);
	
	public void addArgument(ArgumentImpl argument);
	
	public void addArguments(Set<ArgumentImpl> arguments);
}
