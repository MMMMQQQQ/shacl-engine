package at.ac.tuwien.shacl.sparql.queries;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.model.MinMaxCount;
import at.ac.tuwien.shacl.model.SHACLEntity;

import com.hp.hpl.jena.rdf.model.RDFNode;

public class MinMaxCountQuery extends SPARQLConstraintQuery {
	private String subject = "this";
	private String predicate = "predicate";
	private String minCount = "minCount";
	private String maxCount = "maxCount";
	
	@Override
	protected void addQueryString() {
		this.setBaseQueryString(
			"SELECT ?" + this.subject + " (?" + this.subject + " AS ?subject) ?" + this.predicate +
				"WHERE { " +
					"BIND (sh:valueCount(?" + this.subject + ", ?" + this.predicate + ") AS ?count) . " +
					"FILTER ((?count < ?" + this.minCount + ") || (bound(?" + this.maxCount + ") && (?count > ?" + this.maxCount + "))) . " +
			"}"
		);
	}
	
	@Override
	protected void addBindings(SHACLEntity shaclEntity) {
		MinMaxCount minMaxEntity = (MinMaxCount) shaclEntity;
		Map<String, RDFNode> variables = new HashMap<String, RDFNode>();

		variables.put(this.subject, minMaxEntity.getFocusNode());
		variables.put(this.predicate, minMaxEntity.getPredicate());
		variables.put(this.minCount, minMaxEntity.getMinCountNode());
		variables.put(this.maxCount, minMaxEntity.getMaxCountNode());
		
		this.createBindings(variables);
	}
}
