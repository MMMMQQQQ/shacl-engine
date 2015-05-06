package at.ac.tuwien.shacl.model;

import java.util.Map;

public interface SHACLResource {
	public Map<String, String> getLabels();
	
	public void addLabel(String lang, String label);

	public void setLabels(Map<String, String> labels);

	public Map<String, String> getComments();
	
	public void addComment(String lang, String comment);

	public void setComments(Map<String, String> comments);

	public String getDefaultLang();

	public void setDefaultLang(String defaultLang);
	
	public boolean isAbstract();
	
	public void setAbstract(boolean isAbstract);
}
