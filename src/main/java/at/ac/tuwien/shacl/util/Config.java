package at.ac.tuwien.shacl.util;

import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * Configuration class defining several properties.
 * 
 * @author xlin
 *
 */
public class Config {
	public static final String BASE_RES_DIR = "src/main/resources/";
	
	public static final String DEFAULT_LANG = "eng";
	
	public static final String SHACL_METAMODEL_FILE_LOCATION = "shacl.shacl.ttl";
	
	public static final Resource DEFAULT_SEVERITY = SHACL.Error;
	
	public static final Resource DEFAULT_NAMED_MODEL = ResourceFactory.createResource("sh:defNamedModel");
}
