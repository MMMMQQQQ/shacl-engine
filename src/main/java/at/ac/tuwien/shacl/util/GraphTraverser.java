package at.ac.tuwien.shacl.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
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
	public static List<Statement> listAllSubclassesOfNode(RDFNode node, Model model) {
		List<Statement> subclasses = listDirectSubclassesOfNode(node, model);
		List<Statement> resultList = new ArrayList<Statement>();

		for(Statement s : subclasses) {
			resultList.addAll(listAllSubclassesOfNode(s.getSubject(), model));
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
	public static List<Statement> listDirectSubclassesOfNode(RDFNode node, Model model) {
		return model.listStatements(null, RDFS.subClassOf, node).toList();
	}
	
	/**
	 * Get all direct superclasses of a node
	 * 
	 * @param node
	 * @param model
	 * @return
	 */
	public static List<Statement> listDirectSuperclassesOfNode(Resource node, Model model) {
		return model.listStatements(node, RDFS.subClassOf, (RDFNode) null).toList();
	}
}
