package at.ac.tuwien.shacl.executable;

import java.util.Map;

import at.ac.tuwien.shacl.model.Function;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Interface defining an executable language.
 * To create a new executable language, implement from this interface and register an 
 * instance of that class in at.ac.tuwien.shacl.executable.Executables
 * 
 * @author xlin
 *
 */
public interface ExecutableLanguage {
	/**
	 * The Uri of the command that this language is linked with.
	 * E.g. SPARQL executables are defined as sh:sparql in SHACL.
	 * 
	 * @returns
	 */
	public Property getCommand();

	public Map<String,RDFNode> executeAsMultipleValues(final String executable, final Dataset dataset, final Map<String, RDFNode> variables);
	
	public Map<String,RDFNode> executeAsMultipleValues(final String executable, final Dataset dataset);

	public RDFNode executeAsSingleValue(final String executable, final Dataset dataset, final Map<String, RDFNode> variables);
	
	public RDFNode executeAsSingleValue(final String executable, final Dataset dataset);

	
	/**
	 * Register a function in the executable language.
	 * 
	 * @param function
	 */
	public void registerFunction(Function function);
}
