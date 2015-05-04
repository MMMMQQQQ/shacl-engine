package at.ac.tuwien.shacl.util;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.shacl.model.Argument;
import at.ac.tuwien.shacl.model.Function;
import at.ac.tuwien.shacl.model.Template;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class SHACLResourceBuilder {
	public static Template build(List<Statement> statements, Template template) {
		for(Statement property : statements) {
			if(property.getPredicate().equals(SHACL.sparql)) {
				template.setExecutableBody(property.getString());
			} else if(property.getPredicate().equals(SHACL.message)) {
				//TODO implement sh:message
			} else if(property.getPredicate().equals(SHACL.argument)) {
				Argument argument = build(property.getObject().asResource().listProperties().toList(), new Argument());
				template.addArgument(argument);
			} else if(property.getPredicate().equals(SHACL.private_)) {
				//TODO sh:private is not mentioned in the SHACL specification!!
			} else if(property.getPredicate().equals(SHACL.abstract_)) {
				template.setAbstract(property.getBoolean());
			} else if(property.getPredicate().equals(RDFS.comment)) {
				//TODO replace hard coded language flag
				template.addComment("default", property.getString());
			} else if(property.getPredicate().equals(RDFS.label)) {
				//TODO replace hard coded language flag
				template.addLabel("default", property.getString());
			} else if(property.getPredicate().equals(RDFS.subClassOf)) {
				template.addArguments(buildArgumentsOfSuperclass(property.getObject().asResource()));
				//TODO might need some checking whether datatype constraint or not
			} else if(property.getPredicate().equals(RDF.type)) {
				//do nothing
			} else if(property.getPredicate().equals(SHACL.labelTemplate)){
				template.setLabelTemplate(property.getString());
			} else {
				//System.out.println("UNDEFINED PROPERTY: "+property.getPredicate()+"VALUE: "+property.getObject());
			}
		}	
		return template;
	}
	
	private static List<Argument> buildArgumentsOfSuperclass(Resource resource) {
		List<Argument> arguments = new ArrayList<Argument>();
		for(Statement s : resource.listProperties().toList()) {
			if(s.getPredicate().equals(SHACL.argument)) {
				Argument argument = build(s.getObject().asResource().listProperties().toList(), new Argument());
				arguments.add(argument);
			} else if(s.getPredicate().equals(RDFS.subClassOf)) {
				arguments.addAll(buildArgumentsOfSuperclass(s.getObject().asResource()));
			}
		}
		return arguments;
	}
		
	public static Argument build(List<Statement> argumentProperties, Argument argument) {
		for(Statement prop : argumentProperties) {
			if(prop.getPredicate().equals(RDFS.comment)) {
				argument.addComment("default", prop.getString());
			} else if(prop.getPredicate().equals(RDFS.label)) {
				argument.addLabel("default", prop.getString());
			} else if(prop.getPredicate().equals(SHACL.optional)) {
				argument.setOptional(prop.getBoolean());
			} else if(prop.getPredicate().equals(SHACL.predicate)) {
				argument.setPredicate(prop.getObject().asResource());
			} else if(prop.getPredicate().equals(SHACL.valueType)) {
				argument.setValueType(prop.getObject().asResource());
			} else if(prop.getPredicate().equals(SHACL.datatype)) {
				argument.setDatatype(prop.getObject().asResource());
			} else {
				//System.out.println("undefined argument "+prop.getPredicate());
			}
		}
		return argument;
	}
	
	public static Function build(List<Statement> statements, Function function) {
		
		for(Statement property : statements) {
			if(property.getPredicate().equals(SHACL.sparql)) {
				function.setExecutableBody(property.getString());
			} else if(property.getPredicate().equals(SHACL.argument)) {
				Argument argument = build(property.getObject().asResource().listProperties().toList(), new Argument());
				function.addArgument(argument);
			} else if(property.getPredicate().equals(RDFS.comment)) {
				//TODO replace hard coded language flag
				function.addComment("default", property.getString());
			} else if(property.getPredicate().equals(RDFS.label)) {
				//TODO replace hard coded language flag
				function.addLabel("default", property.getString());
			} else if(property.getPredicate().equals(RDFS.subClassOf)) {
				//TODO might need some checking whether datatype constraint or not
			} else if(property.getPredicate().equals(RDF.type)) {
				//do nothing
			} else if(property.getPredicate().equals(SHACL.returnType)) {
				function.setReturnType(property.getObject().asResource());
			} else {
				//System.out.println("UNDEFINED PROPERTY: "+property.getPredicate()+"VALUE: "+property.getObject()+" FROM SUBJECT: "+property.getSubject());
			}
		}	
		
		return function;
	}
}
