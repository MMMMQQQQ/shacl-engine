package at.ac.tuwien.shacl.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.model.ConstraintTemplate;
import at.ac.tuwien.shacl.model.Template;
import at.ac.tuwien.shacl.model.impl.ArgumentImpl;
import at.ac.tuwien.shacl.model.impl.ConstraintTemplateImpl;
import at.ac.tuwien.shacl.util.SHACLResourceBuilder;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Contains all known templates. Templates can either be part of the SHACL language or custom added.
 * Note: For now only constraint templates are considered.
 * 
 * @author xlin
 *
 */
public class TemplateRegistry {
	//map of all templates, with key: uri and value: template object
	private Map<String, Template> templates;
	
	//all registered global template constraints templates
	private Map<String, ConstraintTemplate> globalTemplateConstraintTs;
	
	//all registered property and inverse property constraint templates
	private Map<String, ConstraintTemplate> propertyConstraintTs;
	
	private TemplateRegistry() {
		this.templates = new HashMap<String, Template>();
		this.globalTemplateConstraintTs = new HashMap<String, ConstraintTemplate>();
		this.propertyConstraintTs = new HashMap<String, ConstraintTemplate>();
	}
	
	//register all known constraints extracted from the model
	public void register(Model model) {
		
	}
	
	public void registerPropertyConstraints(Model model) {
		this.registerPropertyConstraints(model, true);
		this.registerPropertyConstraints(model, false);
	}
	
	public void registerPropertyConstraints(Model model, boolean isInverse) {
		Resource shaclPcURI = null;
		
		if(isInverse) {
			shaclPcURI = SHACL.InversePropertyConstraint;
		} else {
			shaclPcURI = SHACL.PropertyConstraint;
		}
		
		//get all meta model nodes of property constraints
		List<Statement> pcStatements = model.listStatements(
				shaclPcURI, RDFS.subClassOf, (RDFNode) null).toList();
		
		//extract values for property constraint definition
		for(Statement s : pcStatements) {
			//properties of each constraint definition
			List<Statement> properties = s.getObject().asResource().listProperties().toList();
			
			//populate property constraint object
			ConstraintTemplateImpl propertyConstraint = (ConstraintTemplateImpl)SHACLResourceBuilder.build(properties, new ConstraintTemplateImpl());
			for(ArgumentImpl argument : propertyConstraint.getArguments()) {
				if(!argument.getPredicate().getURI().equals(SHACL.predicate.getURI())) {
					propertyConstraintTs.put(argument.getPredicate().getURI(), propertyConstraint);
				}
			}
		}
	}
	
	public Set<String> getConstraintsURIs() {
		return this.propertyConstraintTs.keySet();
	}
	
	public ConstraintTemplate getPropertyConstraintTemplate(String constraintURI) {
		return this.propertyConstraintTs.get(constraintURI);
	}
	
	
	public void registerGlobalNativeConstraint(Model model) {
		
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

	public static TemplateRegistry getInstance() {
		return new TemplateRegistry();
	}
}
