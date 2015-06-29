package at.ac.tuwien.shacl.util;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Class to check parsing violations.
 * A violation against the SHACL specification file will result in a 
 * SHACLParsingException (tba).
 * 
 * 
 * @author xlin
 *
 */
public class ParsingUtil {
	public static void checkCardinalityAtMostOne(Resource resource, Property property) {
		if(resource.listProperties(property).toList().size() == 1) {
			//throw exception
		}
	}
	
	public static void checkCardinalityExactlyOne(Resource resource, Property property) {
		if(resource.listProperties(property).toList().size() != 1) {
			//throw exception
		}
	}
}
