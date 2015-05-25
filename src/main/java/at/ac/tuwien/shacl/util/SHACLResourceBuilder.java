package at.ac.tuwien.shacl.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.ac.tuwien.shacl.core.model.NativeConstraint;
import at.ac.tuwien.shacl.core.model.PropertyConstraint;
import at.ac.tuwien.shacl.metamodel.Template;
import at.ac.tuwien.shacl.model.impl.ArgumentImpl;
import at.ac.tuwien.shacl.model.impl.FunctionImpl;
import at.ac.tuwien.shacl.registry.SHACLMetaModelRegistry;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
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
				System.out.println("UNDEFINED TEMPLATE PROPERTY: "+property.getPredicate()+" VALUE: "+property.getObject()+" SUBJECT: "+property.getSubject());
			}
		}	
		return template;
	}

	private static Set<ArgumentImpl> buildArgumentsOfSuperclass(Resource resource) {
		Set<ArgumentImpl> arguments = new HashSet<ArgumentImpl>();
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
				argument.setPredicate(ResourceFactory.createProperty(prop.getObject().asResource().getURI()));
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
	
	public static NativeConstraint build(List<Statement> statements, NativeConstraint constraint) {
		for(Statement property : statements) {			
			System.out.println("Property: "+property);
			if(property.getPredicate().equals(SHACL.sparql)) {
				System.out.println("sparql object: "+property.getObject());
				constraint.setExecutableBody(property.getString());
			} else if(property.getPredicate().equals(RDFS.comment)) {
				//TODO replace hard coded language flag
				constraint.addComment(property.getString());
			} else if(property.getPredicate().equals(SHACL.sparqlEntailment)) {
				//TODO implement sparqlEntailment
			} else if(property.getPredicate().equals(SHACL.severity)) {
				//TODO implement checking whether it's really a constraintViolation type
				constraint.setSeverity(property.getObject().asResource());
			} else if(property.getPredicate().equals(SHACL.message)) {
				constraint.addMessage(property.getString());
			} else if(property.getPredicate().equals(SHACL.predicate)) {
				
				constraint.setPredicate(ResourceFactory.createProperty(property.getObject().asResource().getURI()));
			} else if(property.getPredicate().equals(SHACL.resultAnnotation)) {
				//TODO add resultAnnotation object
				//constraint.addResultAnnotation(property.getObject());
			}
			
			else {
				//System.out.println("UNDEFINED PROPERTY: "+property.getPredicate()+"VALUE: "+property.getObject()+" FROM SUBJECT: "+property.getSubject());
			}
		}	
		
		return constraint;
	}
	
	public static PropertyConstraint build(List<Statement> statements, PropertyConstraint constraint, boolean isInverse) {
		for(Statement property : statements) {			
			System.out.println("Property: "+property);
			if(property.getPredicate().equals(SHACL.predicate)) {
				constraint.setPredicate(ResourceFactory.createProperty(property.getObject().asResource().getURI()));
			} else if(property.getPredicate().equals(RDFS.label)) {
				//TODO add case of string containing language tag
				constraint.addLabel(property.getString());
			} else if(property.getPredicate().equals(RDFS.comment)) {
				constraint.addComment(property.getString());
			} else if(!isInverse && SHACLMetaModelRegistry.getRegistry().getPropertyConstraintsURIs().contains(property.getPredicate().getURI())) {
				constraint.addConstraint(property.getPredicate(), property.getObject());
			} else if(isInverse && SHACLMetaModelRegistry.getRegistry().getInversePropertyConstraintsURIs().contains(property.getPredicate().getURI())) {
				constraint.addConstraint(property.getPredicate(), property.getObject());
			}  else {
				System.out.println("UNDEFINED PROPERTY of PROPERTY CONSTRAINT: "+property.getPredicate()+"VALUE: "+property.getObject()+" FROM SUBJECT: "+property.getSubject());
				//TODO add constraintViolation for unknown property
			}
		}
		
		return constraint;
	}
}
