package at.ac.tuwien.shacl.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.ac.tuwien.shacl.core.model.NativeConstraint;
import at.ac.tuwien.shacl.core.model.PropertyConstraint;
import at.ac.tuwien.shacl.metamodel.ConstraintTemplate;
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
	public static ConstraintTemplate build(List<Statement> statements, ConstraintTemplate template) throws SHACLParsingException {
		for(Statement property : statements) {
			if(property.getPredicate().equals(SHACL.sparql)) {
				template.setExecutableBody(property.getString());
			} else if(property.getPredicate().equals(SHACL.message)) {
				if(property.getLanguage().length() > 0) {
					template.addMessage(property.getLanguage(), property.getString());
				} else {
					template.addMessage(Config.DEFAULT_LANG, property.getString());
				}
			} else if(property.getPredicate().equals(SHACL.argument)) {
				ArgumentImpl argument = build(property.getObject().asResource().listProperties().toList(), new ArgumentImpl());
				template.addArgument(argument);
			} else if(property.getPredicate().equals(SHACL.private_)) {
				//TODO sh:private is not mentioned in the SHACL specification!!
			} else if(property.getPredicate().equals(SHACL.abstract_)) {
				template.setAbstract(property.getBoolean());
			} else if(property.getPredicate().equals(RDFS.comment)) {
				if(property.getLanguage().length() > 0) {
					template.addComment(property.getLanguage(), property.getString());
				} else {
					template.addComment(Config.DEFAULT_LANG, property.getString());
				}
			} else if(property.getPredicate().equals(RDFS.label)) {
				if(property.getLanguage().length() > 0) {
					template.addLabel(property.getLanguage(), property.getString());
				} else {
					template.addLabel(Config.DEFAULT_LANG, property.getString());
				}
			} else if(property.getPredicate().equals(RDFS.subClassOf)) {
				template.addArguments(buildArgumentsOfSuperclass(property.getObject().asResource()));
				//TODO might need some checking whether datatype constraint or not
			} else if(property.getPredicate().equals(RDF.type)) {
				//do nothing
			} else if(property.getPredicate().equals(SHACL.labelTemplate)){
				template.setLabelTemplate(property.getString());
			} else if(property.getPredicate().equals(SHACL.property)) {
				//TODO implement sh:property constraints inside templates
			} else if(property.getPredicate().equals(SHACL.inverseProperty)) {
				//TODO implement sh:inverseProperty constraint inside templates
			} else if(property.getPredicate().equals(SHACL.constraint)) {
				//TODO implement sh:constraint constraints inside templates
			} else {
				throw new SHACLParsingException("Undefined template property " + property.getPredicate());
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
				if(prop.getLanguage().length() > 0) {
					argument.addComment(prop.getLanguage(), prop.getString());
				} else {
					argument.addComment(Config.DEFAULT_LANG, prop.getString());
				}
			} else if(prop.getPredicate().equals(RDFS.label)) {
				if(prop.getLanguage().length() > 0) {
					argument.addLabel(prop.getLanguage(), prop.getString());
				} else {
					argument.addLabel(Config.DEFAULT_LANG, prop.getString());
				}
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
			} else if(prop.getPredicate().equals(SHACL.defaultValueType)) {
				//TODO implement sh:defaultValueType
			} else {
				System.out.println("undefined argument "+prop.getPredicate());
			}
		}
		return argument;
	}
	
	public static FunctionImpl build(List<Statement> statements, FunctionImpl function) throws SHACLParsingException {
		
		for(Statement property : statements) {
			//System.out.println(property);
			if(property.getPredicate().equals(SHACL.sparql)) {
				//System.out.println("sparql object: "+property.getObject());
				function.setExecutableBody(property.getString());
			} else if(property.getPredicate().equals(SHACL.argument)) {
				ArgumentImpl argument = build(property.getObject().asResource().listProperties().toList(), new ArgumentImpl());
				function.addArgument(argument);
			} else if(property.getPredicate().equals(RDFS.comment)) {
				if(property.getLanguage().length() > 0) {
					function.addComment(property.getLanguage(), property.getString());
				} else {
					function.addComment(Config.DEFAULT_LANG, property.getString());
				}
			} else if(property.getPredicate().equals(RDFS.label)) {
				if(property.getLanguage().length() > 0) {
					function.addLabel(property.getLanguage(), property.getString());
				} else {
					function.addLabel(Config.DEFAULT_LANG, property.getString());
				}
			} else if(property.getPredicate().equals(RDFS.subClassOf)) {
				//TODO might need some checking whether datatype constraint or not
			} else if(property.getPredicate().equals(RDF.type)) {
				//do nothing
			} else if(property.getPredicate().equals(SHACL.returnType)) {
				function.setReturnType(property.getObject().asResource());
			} else if(property.getPredicate().equals(SHACL.abstract_)) {
				//TODO implement sh:abstract
			} else {
				throw new SHACLParsingException("Undefined function property " + property.getPredicate());
			}
		}	
		
		return function;
	}
	
	public static NativeConstraint build(List<Statement> statements, NativeConstraint constraint) throws SHACLParsingException {
		for(Statement property : statements) {			
			if(property.getPredicate().equals(SHACL.sparql)) {
				constraint.setExecutableBody(property.getString());
			} else if(property.getPredicate().equals(RDF.type)) {
				//TODO implement inheriting from others?
			} else if(property.getPredicate().equals(RDFS.comment)) {
				if(property.getLanguage().length() > 0) {
					constraint.addComment(property.getLanguage(), property.getString());
				} else {
					constraint.addComment(property.getString());
				}
			} else if(property.getPredicate().equals(SHACL.sparqlEntailment)) {
				//TODO implement sparqlEntailment
			} else if(property.getPredicate().equals(SHACL.severity)) {
				//TODO implement checking whether it's really a constraintViolation type
				constraint.setSeverity(property.getObject().asResource());
			} else if(property.getPredicate().equals(SHACL.message)) {
				if(property.getLanguage().length() > 0) {
					constraint.addMessage(property.getLanguage(), property.getString());
				} else {
					constraint.addMessage(property.getString());
				}
			} else if(property.getPredicate().equals(SHACL.predicate)) {
				
				constraint.setPredicate(ResourceFactory.createProperty(property.getObject().asResource().getURI()));
			} else if(property.getPredicate().equals(SHACL.resultAnnotation)) {
				//TODO add resultAnnotation object
				//constraint.addResultAnnotation(property.getObject());
			} else {
				throw new SHACLParsingException("Undefined constraint property " + property.getPredicate());
			}
		}	
		
		return constraint;
	}
	
	public static PropertyConstraint build(List<Statement> statements, PropertyConstraint constraint, boolean isInverse) throws SHACLParsingException {
		for(Statement property : statements) {			
			if(property.getPredicate().equals(SHACL.predicate)) {
				constraint.setPredicate(ResourceFactory.createProperty(property.getObject().asResource().getURI()));
			} else if(property.getPredicate().equals(RDF.type)) {
				//TODO implement inheriting from others?
			} else if(property.getPredicate().equals(RDFS.label)) {
				//TODO add case of string containing language tag
				constraint.addLabel(property.getString());
			} else if(property.getPredicate().equals(RDFS.comment)) {
				constraint.addComment(property.getString());
			} else if(!isInverse && SHACLMetaModelRegistry.getRegistry().getPropertyConstraintsURIs().contains(property.getPredicate().getURI())) {
				constraint.addConstraint(property.getPredicate(), property.getObject());
			} else if(isInverse && SHACLMetaModelRegistry.getRegistry().getInversePropertyConstraintsURIs().contains(property.getPredicate().getURI())) {
				constraint.addConstraint(property.getPredicate(), property.getObject());
			} else {
				throw new SHACLParsingException("Undefined constraint property " + property.getPredicate());
			}
		}
		
		return constraint;
	}
}
