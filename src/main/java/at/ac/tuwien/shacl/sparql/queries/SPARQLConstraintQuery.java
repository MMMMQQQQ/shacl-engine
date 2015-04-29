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
	
	public void init(SHACLEntity entity) {
		this.addQueryString();
		this.addBindings(entity);
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
		System.out.println(variables);
		for(Map.Entry<String, RDFNode> v : variables.entrySet()) {
			bindings.add(v.getKey(), v.getValue());
		}
	}
	
	public QuerySolutionMap getBindings() {
		return this.bindings;
	}
	
	public boolean executeQuery(Model model) {
		SPARQLQueryExecutor executor = new SPARQLQueryExecutor();		
		boolean isValid = executor.isQueryValid(this.getBaseQuery(), this.getBindings(), model);
		System.out.println("isvalid?"+isValid);
		return isValid;
	}
	
	protected abstract void addQueryString();

	protected abstract void addBindings(SHACLEntity shaclEntity);
}
