package at.ac.tuwien.shacl.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.vocabulary.ResultSetGraphVocab;

/**
 * Inject values from SPARQL queries or other sources to a target and replace
 * the placeholder variables with them.
 * A placeholder variable is declared as "{?varName}".
 * 
 * @author xlin
 *
 */
public class ValueInjector {
	private static Pattern pattern = null;
	
	private static void initPattern() {
		pattern = Pattern.compile("\\{\\?([a-zA-Z0-9\\-]+)\\}");
	}
	
	public static String inject(String string, Map<String, Object> variables) {
		if(pattern == null) {
			initPattern();
		}
		
		if(variables != null) {
			Matcher m = pattern.matcher(string);
			
			while (m.find())
			{
				String variable = m.group(1);
				String value = variables.get(variable)!=null ? 
						variables.get(variable).toString() : null;
				if(value != null) {
					string = string.replace("{?"+ variable +"}", value);
				} else {
					System.out.println("unknown variable" + variable);
					//TODO create constraint violation type sh:Warning
					//that variable doesnt exist
				}
			}
		}

		return string;
	}
}
