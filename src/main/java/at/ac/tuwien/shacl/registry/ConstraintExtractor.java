package at.ac.tuwien.shacl.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.shacl.model.ConstraintTemplate;
import at.ac.tuwien.shacl.model.GlobalConstraint;
import at.ac.tuwien.shacl.model.NativeConstraint;
import at.ac.tuwien.shacl.model.impl.ArgumentImpl;
import at.ac.tuwien.shacl.model.impl.ConstraintTemplateImpl;
import at.ac.tuwien.shacl.model.impl.NativeConstraintImpl;
import at.ac.tuwien.shacl.util.GraphTraverser;
import at.ac.tuwien.shacl.util.SHACLResourceBuilder;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * Contains all known templates. Templates can either be part of the SHACL language or custom added.
 * Note: For now only constraint templates are considered.
 * 
 * @author xlin
 *
 */
public class ConstraintExtractor {
	//all registered global constraints templates
	private Map<String, GlobalConstraint> globalConstraintTs;
	
	//private Map<String>
	
	public ConstraintExtractor() {
		this.globalConstraintTs = new HashMap<String, GlobalConstraint>();
	}

	public static Map<String, NativeConstraint> getGlobalNativeConstraintDefinitions(Model model) {
		List<Statement> statements = GraphTraverser.listAllAndSubclassesOfRdfTypeAsSubject(SHACL.GlobalNativeConstraint, model);
		Map<String, NativeConstraint> constraints = new HashMap<String, NativeConstraint>();
		
		for(Statement s : statements) {
			NativeConstraint constraint = SHACLResourceBuilder.build(s.getSubject().listProperties().toList(), new NativeConstraintImpl());
			constraints.put(s.getSubject().getURI(), constraint);
		}
		
		return constraints;
	}
	
	public void registerGlobalConstraints(Model model) {
	}
	
	//Instances of sh:ConstraintTemplate that are also subclasses of sh:GlobalConstraint
	public void registerGlobalTemplateConstraint(Model model) {
	}
	
	//rdf:type of 
	public void registerConstraintTemplate() {
		
	}
	
	//sh:orConstraint
	public void registerDisjunctiveConstraint() {
		
	}
	
	//sh:constraint
	public void registerGeneralShapeConstraint() {
		
	}
	

	public static Map<String, ConstraintTemplate> getPropertyConstraints(Model model, boolean isInverse) {
		Map<String, ConstraintTemplate> templates = new HashMap<String, ConstraintTemplate>();
		Resource shaclPcURI = null;

		if(isInverse) {
			shaclPcURI = SHACL.InversePropertyConstraint;
		} else {
			shaclPcURI = SHACL.PropertyConstraint;
		}
		
		//get all meta model nodes of property constraints
		//each meta model node is defined as a superclass of sh:PropertyConstraint/sh:InversePropertyConstraint
		List<Statement> pcStatements = GraphTraverser.listDirectSuperclassesOfNodeAsObject(shaclPcURI, model);

		//extract values for property constraint definition
		for(Statement s : pcStatements) {
			//properties of each constraint definition
			List<Statement> properties = s.getObject().asResource().listProperties().toList();
			
			//populate property constraint object
			ConstraintTemplateImpl propertyConstraint = (ConstraintTemplateImpl)SHACLResourceBuilder.build(properties, new ConstraintTemplateImpl());
			for(ArgumentImpl argument : propertyConstraint.getArguments()) {
				if(!argument.getPredicate().getURI().equals(SHACL.predicate.getURI())) {
					templates.put(argument.getPredicate().getURI(), propertyConstraint);
				}
			}
		}
		return templates;
	}
}
