package at.ac.tuwien.shacl.executable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class Executables {
	private static Map<String, ExecutableLanguage> execs = new HashMap<String, ExecutableLanguage>();

	private static Set<Property> commands = new HashSet<Property>(); 
	
	public static void addExecutable(ExecutableLanguage execLang) {
		if(!execs.containsKey(execLang.getCommand())) {
			execs.put(execLang.getCommand().getURI(), execLang);
			commands.add(execLang.getCommand());
			System.out.println("adding language "+execLang.getCommand());
		} else {
			//TODO throw either error or fatal error, because uri is ambigious
		}
	}
	
	public static Set<Property> getCommands() {
		return commands;
	}
	
	public static void removeExecutable(String uri) {
		execs.remove(uri);
		commands.remove(ResourceFactory.createProperty(uri));
	}
	
	public static void removeExecutable(Property property) {
		execs.remove(property.getURI());
		commands.remove(property);
	}
	
	public static ExecutableLanguage getExecutable(String uri) {
		return execs.get(uri);
	}
	
	public static ExecutableLanguage getExecutable(Property property) {
		return execs.get(property.getURI());
	}
}
