package at.ac.tuwien.shacl.model;

public interface Template extends Macro {
	public String getLabelTemplate();

	public void setLabelTemplate(String labelTemplate);
	/**
	 * A template must either be abstract or have an executable body.
	 * Check, if template is valid under those circumstances.
	 * 
	 * @return
	 */
	public boolean isValidTemplate();
}
