package at.ac.tuwien.shacl.sparql.queries;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.model.AllowedValues;
import at.ac.tuwien.shacl.model.SHACLEntity;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.RDF;

public class AllowedValuesQuery extends SPARQLConstraintQuery {
	private String subject = "this";
	private String predicate = "predicate";
	private String allowedValues = "allowedValues";
	
	@Override
	protected void addQueryString() {
		this.setBaseQueryString(
			"PREFIX rdf: <" + RDF.getURI() + ">"  +
			"SELECT ?" + this.subject + " (?" + this.subject + " AS ?subject) ?" + this.predicate + " ?object " +
			"WHERE {" +
				"?" + this.subject + " ?" + this.predicate + " ?object . " +
				"FILTER NOT EXISTS {" +
					"?" + this.allowedValues + " (rdf:rest*)/rdf:first ?object" + 
				"}"+
			"}"
		);
	}

	@Override
	protected void addBindings(SHACLEntity shaclEntity) {
		AllowedValues allowedValuesEntity = (AllowedValues) shaclEntity;
		Map<String, RDFNode> variables = new HashMap<String, RDFNode>();

		variables.put(this.subject, allowedValuesEntity.getFocusNode());
		variables.put(this.predicate, allowedValuesEntity.getPredicate());
		variables.put(this.allowedValues, allowedValuesEntity.getAllowedValuesNode());

		this.createBindings(variables);
	}

}
