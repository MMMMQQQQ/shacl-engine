package at.ac.tuwien.shacl;

import at.ac.tuwien.shacl.executable.Executables;
import at.ac.tuwien.shacl.executable.sparql.SPARQLExecutableLanguage;
import at.ac.tuwien.shacl.validation.SHACLValidator;
import at.ac.tuwien.shacl.vocabulary.SHACL;

public class CoreValidation {
	private Executables execs;
	
	public CoreValidation() {
		Executables execs = new Executables();
		this.initCore();
	}
	
	private void initCore() {
		//add sparql executable
		Executables.addExecutable(new SPARQLExecutableLanguage());
		
		//add sparql functions
	}

	public SHACLValidator createDefaultValidator() {
		this.initCore();
		return null;
	}
}
