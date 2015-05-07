package at.ac.tuwien.shacl.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Easing the traversing through a RDF graph
 * 
 * @author xlin
 *
 */
public class GraphTraverser {

	/**
	 * Get all subclasses of a node. Subclasses can be 
	 * subclasses of other subclasses of other subclasses ... of the node.
	 * 
	 * @param node
	 * @param model
	 * @return
	 */
	public static List<Statement> listAllSubclassesOfNodeAsSubject(RDFNode node, Model model) {
		List<Statement> subclasses = listDirectSubclassesOfNodeAsSubject(node, model);
		List<Statement> resultList = new ArrayList<Statement>();

		for(Statement s : subclasses) {
			resultList.addAll(listAllSubclassesOfNodeAsSubject(s.getSubject(), model));
		}
		subclasses.addAll(resultList);
		return subclasses;
	}
	
	/**
	 * Get all direct subclasses of a node
	 * 
	 * @param node
	 * @param model
	 * @return
	 */
	public static List<Statement> listDirectSubclassesOfNodeAsSubject(RDFNode node, Model model) {
		return model.listStatements(null, RDFS.subClassOf, node).toList();
	}
	
	/**
	 * Get all direct superclasses of a node
	 * 
	 * @param node
	 * @param model
	 * @return
	 */
	public static List<Statement> listDirectSuperclassesOfNodeAsObject(Resource node, Model model) {
		return model.listStatements(node, RDFS.subClassOf, (RDFNode) null).toList();
	}
	
	/**
	 * Get all statements where the object node is of rdf:type
	 * 
	 * @param hasType
	 * @param model
	 * @return
	 */
	public static List<Statement> listAllOfRdfTypeAsSubject(Resource hasType, Model model) {
		return model.listStatements(null, RDF.type, hasType).toList();
	}
	
	/**
	 * Get all statements where the object node and its subclasses is connected through rdf:type
	 * 
	 * @param hasType
	 * @param model
	 * @return
	 */
	public static List<Statement> listAllAndSubclassesOfRdfTypeAsSubject(Resource hasType, Model model) {
		List<Statement> statements = listAllOfRdfTypeAsSubject(hasType, model);
		List<Statement> subclasses = listAllSubclassesOfNodeAsSubject(hasType, model);

		for(Statement s : subclasses) {
			statements.addAll(listAllOfRdfTypeAsSubject(s.getSubject(), model));
		}
		
		return removeDuplicates(statements);
	}
	
	private static List<Statement> removeDuplicates(List<Statement> statements) {
		Set<String> known = new HashSet<String>();
		List<Statement> cleanedStatements = new ArrayList<Statement>();
		
		for(Statement s : statements) {
			if(!known.contains(s.getSubject().getURI())) {
				known.add(s.getSubject().getURI());
				cleanedStatements.add(s);
			}
		}
		
		return cleanedStatements;
	}
}
