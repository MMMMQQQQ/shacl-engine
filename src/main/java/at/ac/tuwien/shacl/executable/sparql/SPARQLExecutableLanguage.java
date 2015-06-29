package at.ac.tuwien.shacl.executable.sparql;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.shacl.executable.ExecutableLanguage;
import at.ac.tuwien.shacl.model.Function;
import at.ac.tuwien.shacl.util.Config;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.function.FunctionRegistry;

/**
 * Defines the SPARQL executable language.
 * 
 * @author xlin
 *
 */
public class SPARQLExecutableLanguage implements ExecutableLanguage {
    private final Logger log = LoggerFactory.getLogger(SPARQLExecutableLanguage.class);
	
	private SPARQLExecutor executor;
	
	public SPARQLExecutableLanguage() {		
		this.executor = new SPARQLExecutor();
	}
	
	@Override
	public Property getCommand() {
		return SHACL.sparql;
	}

	@Override
	public RDFNode executeAsSingleValue(String query, Dataset dataset,
			Map<String, RDFNode> variables) {
		query = this.buildExecutable(query, dataset.getNamedModel(Config.DEFAULT_NAMED_MODEL.getURI()));

		return this.executor.executeAsSingleValue(query, dataset, variables);
	}

	@Override
	public RDFNode executeAsSingleValue(String query, Dataset dataset) {
		query = this.buildExecutable(query, dataset.getNamedModel(Config.DEFAULT_NAMED_MODEL.getURI()));
		return this.executor.executeAsSingleValue(query, dataset);
	}
	
	public String buildExecutable(String string, Model model) {
		Map<String, String> prefixes = model.getNsPrefixMap();
		String prefs = "";
		for(Map.Entry<String,String> p : prefixes.entrySet()) {
			prefs = prefs + "\n" + "PREFIX " + p.getKey() + ": <" + p.getValue() + ">";
		}
		return prefs + "\n" + string;
	}

	@Override
	public void registerFunction(Function function) {
		String query = function.getExecutableBody(this.getCommand());
		
		if(query != null) {
			query = this.buildExecutable(query, function.getModel());
			
			if(this.executor.isAskQuery(query)) {
				FunctionRegistry.get().put(function.getURI(), new AskFunction());
				log.debug("ask sparql function with uri "+function.getURI()+" registered");
			} else if(this.executor.isSelectQuery(query)) {
				FunctionRegistry.get().put(function.getURI(), new SelectFunction());
				log.debug("select sparql function with uri "+function.getURI()+" registered");
			}
		} else if(function.getURI().equals(SHACL.hasShape.getURI())) {
			FunctionRegistry.get().put(function.getURI(), new HasShapeFunction());
			log.debug("sparql function with uri "+function.getURI()+" registered");
		} else {
			//do nothing, because the function has no purpose
		}
	}

	@Override
	public Map<String, RDFNode> executeAsMultipleValues(String query,
			Dataset dataset, Map<String, RDFNode> variables) {
		query = this.buildExecutable(query, dataset.getNamedModel(Config.DEFAULT_NAMED_MODEL.getURI()));
		return this.executor.executeAsMultipleValues(query, dataset, variables);
	}

	@Override
	public Map<String, RDFNode> executeAsMultipleValues(String query,
			Dataset dataset) {
		query = this.buildExecutable(query, dataset.getNamedModel(Config.DEFAULT_NAMED_MODEL.getURI()));
		return this.executor.executeAsMultipleValues(query, dataset);
	}
}
