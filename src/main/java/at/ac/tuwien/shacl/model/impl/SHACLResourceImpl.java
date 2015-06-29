package at.ac.tuwien.shacl.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.shacl.model.SHACLResource;
import at.ac.tuwien.shacl.util.Config;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class SHACLResourceImpl extends ResourceImpl implements SHACLResource {

	private Map<String, String> messages;
	
	public SHACLResourceImpl(Node node, EnhGraph graph) {
		super(node, graph);
	}
	
	@Override
	public Map<String, String> getMessages() {
		if(messages == null) {
			this.messages = new HashMap<String, String>();
			
			List<Statement> statements = this.listProperties(SHACL.message).toList();
			
			if(statements.size() == 1) {
				String lang = statements.get(0).getLanguage() == null ? Config.DEFAULT_LANG : statements.get(0).getLanguage();
				messages.put(lang, statements.get(0).getString());
			} else if(statements.size() > 1) {
				for(Statement statement : statements) {
					messages.put(statement.getLanguage(), statement.getString());
				}
			}
		}
		
		return messages;
	}

	protected Boolean getOptionalBooleanOfProperty(Property property) {
		Statement s = this.getProperty(property);
		
		if(s != null) {
			return s.getBoolean();
		} else {
			return null;
		}
	}
	
	protected Resource getOptionalResourceOfProperty(Property property) {
		Statement s = this.getProperty(property);
		
		if(s != null) {
			return s.getResource();
		} else {
			return null;
		}
	}
	
	protected RDFNode getOptionalNodeOfProperty(Property property) {
		Statement s = this.getProperty(property);
		
		if(s != null) {
			return s.getObject();
		} else {
			return null;
		}
	}
	
	protected String getOptionalStringOfProperty(Property property) {
		Statement s = this.getProperty(property);
		
		if(s != null) {
			return s.getString();
		} else {
			return null;
		}
	}
	
	protected List<RDFNode> listObjectsOfProperty(Property property) {
		List<RDFNode> nodes = new ArrayList<RDFNode>();
		
		for(Statement stmt : this.listProperties(property).toList()) {
			nodes.add(stmt.getObject());
		}
		
		return nodes;
	}
}
