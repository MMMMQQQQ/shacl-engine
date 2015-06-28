package at.ac.tuwien.shacl.util;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

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
