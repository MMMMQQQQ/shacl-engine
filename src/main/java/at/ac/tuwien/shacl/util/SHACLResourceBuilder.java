package at.ac.tuwien.shacl.util;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.shacl.model.NativeConstraint;
import at.ac.tuwien.shacl.model.Template;
import at.ac.tuwien.shacl.model.impl.ArgumentImpl;
import at.ac.tuwien.shacl.model.impl.FunctionImpl;
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
				ArgumentImpl argument = build(property.getObject().asResource().listProperties().toList(), new ArgumentImpl());
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
				System.out.println("UNDEFINED PROPERTY: "+property.getPredicate()+" VALUE: "+property.getObject()+" SUBJECT: "+property.getSubject());
			}
		}	
		return template;
	}
	
	public static NativeConstraint build(List<Statement> statements, NativeConstraint constraint) {
		for(Statement property : statements) {
			if(property.getPredicate().equals(SHACL.sparql)) {
				constraint.setExecutableBody(property.getString());
			} else if(property.getPredicate().equals(SHACL.message)) {
				//TODO implement sh:message
			} else if(property.getPredicate().equals(SHACL.abstract_)) {
				constraint.setAbstract(property.getBoolean());
			} else if(property.getPredicate().equals(RDFS.comment)) {
				//TODO replace hard coded language flag
				constraint.addComment("default", property.getString());
			} else if(property.getPredicate().equals(RDFS.label)) {
				//TODO replace hard coded language flag
				constraint.addLabel("default", property.getString());
			} else if(property.getPredicate().equals(RDF.type)) {
				//do nothing
			} else if(property.getPredicate().equals(SHACL.severity)) {
				constraint.setSeverity(property.getResource());
			} else if(property.getPredicate().equals(SHACL.resultAnnotation)) {
				//TODO build result annotation
			} else {
				System.out.println("UNDEFINED PROPERTY: "+property.getPredicate()+" VALUE: "+property.getObject()+" SUBJECT: "+property.getSubject());
			}
		}	
		
		return constraint;
	}

	private static List<ArgumentImpl> buildArgumentsOfSuperclass(Resource resource) {
		List<ArgumentImpl> arguments = new ArrayList<ArgumentImpl>();
		for(Statement s : resource.listProperties().toList()) {
			if(s.getPredicate().equals(SHACL.argument)) {
				ArgumentImpl argument = build(s.getObject().asResource().listProperties().toList(), new ArgumentImpl());
				arguments.add(argument);
			} else if(s.getPredicate().equals(RDFS.subClassOf)) {
				arguments.addAll(buildArgumentsOfSuperclass(s.getObject().asResource()));
			}
		}
		return arguments;
	}
		
	public static ArgumentImpl build(List<Statement> argumentProperties, ArgumentImpl argument) {
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
			} else if(prop.getPredicate().equals(SHACL.optionalWhenInherited)){
				argument.setOptionalWhenInherited(prop.getBoolean());
			} else if(prop.getPredicate().equals(SHACL.defaultValue)){
				argument.setDefaultValue(prop.getObject());
			} else if(prop.getPredicate().equals(SHACL.nodeKind)){
				argument.setDefaultValue(prop.getObject());
			} else {
				System.out.println("undefined argument "+prop.getPredicate());
			}
		}
		return argument;
	}
	
	public static FunctionImpl build(List<Statement> statements, FunctionImpl function) {
		
		for(Statement property : statements) {
			//System.out.println(property);
			if(property.getPredicate().equals(SHACL.sparql)) {
				//System.out.println("sparql object: "+property.getObject());
				function.setExecutableBody(property.getString());
			} else if(property.getPredicate().equals(SHACL.argument)) {
				ArgumentImpl argument = build(property.getObject().asResource().listProperties().toList(), new ArgumentImpl());
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
