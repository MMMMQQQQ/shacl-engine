package at.ac.tuwien.shacl.sparql;

import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.Resource;

//TODO Add method for building SELECT and ASK queries
public class QueryBuilder {
	private Query query;
	private QuerySolutionMap bindings;
	
	public QueryBuilder(String queryString) {
		System.out.println("sparql query:"+queryString);
		this.query = QueryFactory.create(queryString);
		this.bindings = new QuerySolutionMap();
	}
	
	public void addPrefixes() {
		//query.addNamedGraphURI(uri);
	}
	
	public void addPrefix() {
		
	}
	
	public Query getQuery() {
		return query;
	}
	
	public void addBindings(Map<String, Resource> bindings) {
		for(Map.Entry<String, Resource> b : bindings.entrySet()) {
			this.bindings.add(b.getKey(), b.getValue());
		}
	}
	
	public void addBinding(String name, Resource resource) {
		this.bindings.add(name, resource);
	}
	
	public QuerySolutionMap getBindings() {
		System.out.println("bindings: "+bindings);
		return this.bindings;
	}

	public void addThisBinding(Resource thiz) {
		bindings.add("this", thiz);
	}
	
	public Object executeSelect() {
		return null;
	}
}
