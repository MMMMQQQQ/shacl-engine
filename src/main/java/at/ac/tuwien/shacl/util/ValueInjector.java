package at.ac.tuwien.shacl.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Inject values from SPARQL queries or other sources to a target and replace
 * the placeholder variables with them.
 * A placeholder variable is declared as "{?varName}".
 * 
 * @author xlin
 *
 */
public class ValueInjector {
	private static final Logger log = LoggerFactory.getLogger(ValueInjector.class);
	
	private static Pattern pattern = null;
	
	/**
	 * Initiate the regex pattern once, because the process takes awhile.
	 */
	protected static void initPattern() {
		pattern = Pattern.compile("\\{\\?([a-zA-Z0-9\\-]+)\\}");
	}
	
	/**
	 * Inject variable values to a target string with the placeholder variables.
	 * 
	 * @param string the string to inject the values in
	 * @param variables a map containing the variables and the corresponding values
	 * @return the injected string
	 */
	public static String inject(String string, Map<String, RDFNode> variables) {
		if(pattern == null) {
			initPattern();
		}
		
		if(variables != null) {
			Matcher m = pattern.matcher(string);
			
			while (m.find())
			{
				String variable = m.group(1);
				String value = null;
				
				if(variables.containsKey(variable)) {
					value = variables.get(variable).isLiteral() ? 
							variables.get(variable).asLiteral().getString() : variables.get(variable).toString();
				}

				if(value != null) {
					string = string.replace("{?"+ variable +"}", value);
				} else {
					log.warn("unknown variable " + variable);
					//TODO create constraint violation type sh:Warning that variable doesnt exist?
				}
			}
		}

		return string;
	}
}
