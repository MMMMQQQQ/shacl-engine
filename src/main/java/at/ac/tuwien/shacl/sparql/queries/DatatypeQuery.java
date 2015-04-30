package at.ac.tuwien.shacl.sparql.queries;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.model.Datatype;
import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.RDFNode;

public class DatatypeQuery extends SPARQLConstraintQuery {
	private String subject = "this";
	private String predicate = "predicate";
	private String datatype = "datatype";
	
	@Override
	protected void addQueryString() {
		this.setBaseQueryString(
			"SELECT ?" + this.subject + "(?" + this.subject + " AS ?subject) ?" + this.predicate + " ?object " +
					"WHERE { " +
						"?" + this.subject + " ?" + this.predicate + " ?object ." +
						"FILTER (!" + SHACL.datatype + "(?object, ?" + this.datatype + ")) ." +
					"}"
		);
	}

	@Override
	protected void addBindings(SHACLEntity shaclEntity) {
		Datatype datatypeEntity = (Datatype) shaclEntity;
		Map<String, RDFNode> variables = new HashMap<String, RDFNode>();
		System.out.println("datatype vars: "+variables);
		variables.put(this.subject, datatypeEntity.getFocusNode());
		variables.put(this.predicate, datatypeEntity.getPredicate());
		variables.put(this.datatype, datatypeEntity.getDatatypeNode());

		this.createBindings(variables);
	}

}
