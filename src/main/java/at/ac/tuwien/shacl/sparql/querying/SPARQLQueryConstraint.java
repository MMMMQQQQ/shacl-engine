package at.ac.tuwien.shacl.sparql.querying;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.shacl.validation.constraint.Constraint;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class SPARQLQueryConstraint implements Constraint {
	protected String queryString;
	
	protected Set<String> variableNames = new HashSet<String>();
	
	protected QuerySolutionMap bindings;
	
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
	
	public void createBindings(Map<String, RDFNode> variables) throws MissingBindingException {
		bindings = new QuerySolutionMap();
		
		//check, if all parameters are passed, otherwise SPARQL query wouldn't work
		for(String vn : variableNames) {
			if(!variables.containsKey(vn)) {
				throw new MissingBindingException("Missing binding variable "+vn+"");
			}
		}
		
		for(Map.Entry<String, RDFNode> v : variables.entrySet()) {
			bindings.add(v.getKey(), v.getValue());
		}
	}
	
	public QuerySolutionMap getBindings() throws MissingBindingException {
		if(bindings == null) {
			throw new MissingBindingException("No binding for constraint defined.");
		} else {
			return this.bindings;
		}
	}
	
	protected void validateBinding() {
		
	}
}
