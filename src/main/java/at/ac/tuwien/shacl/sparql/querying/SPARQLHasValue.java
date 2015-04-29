package at.ac.tuwien.shacl.sparql.querying;

import at.ac.tuwien.shacl.model.constraints.HasValue;


public class SPARQLHasValue extends SPARQLQueryConstraint {
	private String thiz = "this";
	private String predicate = "predicate";
	private String hasValue = "hasValue";
	
	public SPARQLHasValue() {
		super();
		String thiz = "this";
		String predicate = "predicate";
		String hasValue = "hasValue";
		
		queryString = "SELECT ?" + this.thiz + " (?" + this.thiz + " AS ?subject) ?" + this.predicate+ " ?" + this.hasValue + " " +
				"WHERE {" +
				"FILTER NOT EXISTS {" +
					"?" + this.thiz + " ?" + this.predicate + " ?" + this.hasValue +
				"}" +
			  "}";
	}
}
