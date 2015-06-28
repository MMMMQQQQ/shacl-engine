package at.ac.tuwien.shacl.model.impl.constraints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.model.Argument;
import at.ac.tuwien.shacl.model.Template;
import at.ac.tuwien.shacl.model.impl.ConstraintImpl;
import at.ac.tuwien.shacl.model.impl.TemplateInstance;
import at.ac.tuwien.shacl.registry.SHACLModelRegistry;
import at.ac.tuwien.shacl.registry.SHACLPropertyRegistry;
import at.ac.tuwien.shacl.util.Config;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

//implement a general shape constraint
public class TemplateConstraint extends ConstraintImpl {

	public TemplateConstraint(Node node, EnhGraph graph) {
		super(node, graph);
		this.init();
	}
	
	private Map<String, String> labels;
	
	private Map<String, String> messages;

	private Set<TemplateInstance> templates;
	
	private void init() {
		
		Map<Template, TemplateInstance> registeredTmpls = new HashMap<Template, TemplateInstance>();
		Template template = SHACLModelRegistry.get().getTemplate(this.getProperty(RDF.type).getResource().getURI());
		
		for(Statement stmt : this.listProperties().toList()) {
			
			System.out.println("***templates: "+template);
			TemplateInstance tmpInstance = null;
			
			if(template != null) {
				if(registeredTmpls.containsKey(template)) {
					tmpInstance = registeredTmpls.get(template);
				} else {
					tmpInstance = new TemplateInstance(template);
					registeredTmpls.put(template, tmpInstance);
				}
				
				for(Argument arg : template.getArguments()) {
					if(arg.getPredicate().getURI().equals(stmt.getPredicate().getURI())) {
						tmpInstance.addArgumentValue(arg, stmt.getObject());
					}
				}
			}
		}
		
		templates = new HashSet<TemplateInstance>(registeredTmpls.values());
	}

	public Map<String, String> getLabels() {
		if(labels == null) {
			for(RDFNode labelNode : this.listObjectsOfProperty(SHACL.label)) {
				String lang = labelNode.asLiteral().getLanguage() == null 
						? Config.DEFAULT_LANG : labelNode.asLiteral().getLanguage();
				String label = labelNode.asLiteral().getString();
				
				labels.put(lang, label);
			}
		}
		
		return labels;
	}
	
	public Map<String, String> getComments() {
		return null;
	}
	
	public Property getPredicate() {
		Statement stmt = this.getProperty(SHACL.predicate);
		
		if(stmt != null) {
			return ResourceFactory.createProperty(stmt.getResource().getURI());
		} else {
			return null;
		}
		
	}
	
	public Map<String, String> getMessages() {
		if(messages == null) {
			for(RDFNode msgNode : this.listObjectsOfProperty(SHACL.message)) {
				String lang = msgNode.asLiteral().getLanguage() == null 
						? Config.DEFAULT_LANG : msgNode.asLiteral().getLanguage();
				String msg = msgNode.asLiteral().getString();
				
				messages.put(lang, msg);
			}
		}
		
		return this.messages;
	}
	
	public Map<Template, Map<String, RDFNode>> getTemplateInstances() {
		Map<Template, Map<String, RDFNode>> instances = new HashMap<Template, Map<String, RDFNode>>();
		
		for(TemplateInstance tmpInstance : templates) {
			Map<String, RDFNode> args = new HashMap<String, RDFNode>();
			
			for(Map.Entry<Argument,RDFNode> argVar : tmpInstance.getArgumentValues().entrySet()) {
				if(argVar.getValue() != null) {
					args.put(argVar.getKey().getPredicate().getLocalName(), argVar.getValue());
				}
			}
			
			if(this.getPredicate() != null) {
				//add sh:predicate
				args.put(SHACL.predicate.getLocalName(), this.getPredicate());
			}
			
			
			instances.put(tmpInstance.getTemplate(), args);
		}
		
		return instances;
	}
}
