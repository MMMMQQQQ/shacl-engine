package at.ac.tuwien.shacl.sparql.queries;

import java.util.Map;

import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.sparql.SPARQLQueryExecutor;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

public abstract class SPARQLConstraintQuery {
	protected String queryString;

	protected QuerySolutionMap bindings;
	
	protected SPARQLConstraintQuery() {
	}
	
	/**
	 * The parametrized query without the actual values.
	 * 
	 * @return
	 */
	public Query getBaseQuery() {
		if(queryString == null) {
			
		}
		Query query = QueryFactory.create(this.queryString);

		return query;
	};
	
	public String getBaseQueryString() {
		return this.queryString;
	}
	
	public void setBaseQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	protected void createBindings(Map<String, RDFNode> variables) {
		bindings = new QuerySolutionMap();
		
		//FIXME binding values get changed in allowedValues. issue with list/array?
		System.out.println("in create bindings: "+variables);
		for(Map.Entry<String, RDFNode> v : variables.entrySet()) {
			System.out.println("v.value: "+v.getValue());
			bindings.add(v.getKey(), v.getValue());
			System.out.println("now: "+bindings);
		}
	}
	
	public QuerySolutionMap getBindings() {
		return this.bindings;
	}
	
	public boolean validateQuery(Model model, SHACLEntity shaclEntity) {
		if(bindings == null) {
			this.addBindings(shaclEntity);
		}
		
		if(queryString == null) {
			this.addQueryString();
		}
		
		SPARQLQueryExecutor executor = new SPARQLQueryExecutor();	
		System.out.println("bindings: "+this.getBindings());
		boolean isValid = executor.isQueryValid(this.getBaseQuery(), this.getBindings(), model);

		return isValid;
	}
	
	protected abstract void addQueryString();

	protected abstract void addBindings(SHACLEntity shaclEntity);
}
