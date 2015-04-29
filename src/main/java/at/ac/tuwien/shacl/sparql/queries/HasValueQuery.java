package at.ac.tuwien.shacl.sparql.queries;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.model.HasValue;
import at.ac.tuwien.shacl.model.SHACLEntity;

import com.hp.hpl.jena.rdf.model.RDFNode;


public class HasValueQuery extends SPARQLConstraintQuery {
	private String thiz = "this";
	private String predicate = "predicate";
	private String hasValue = "hasValue";

	public HasValueQuery(SHACLEntity hasValueEntity) {
		this.init(hasValueEntity);
	}

	@Override
	protected void addQueryString() {
		this.setBaseQueryString(
			"SELECT ?" + this.thiz + " (?" + this.thiz + " AS ?subject) ?" + this.predicate+ " ?" + this.hasValue + " " +
				"WHERE {" +
				"FILTER NOT EXISTS {" +
					"?" + this.thiz + " ?" + this.predicate + " ?" + this.hasValue +
				"}" +
			  "}");
	}
	
	@Override
	protected void addBindings(SHACLEntity entity) {
		HasValue hasValueEntity = (HasValue) entity;
		Map<String, RDFNode> variables = new HashMap<String, RDFNode>();

		variables.put(this.thiz, hasValueEntity.getFocusNode());
		variables.put(this.predicate, hasValueEntity.getPredicate());
		variables.put(this.hasValue, hasValueEntity.getHasValue());

		this.createBindings(variables);
	}
}
