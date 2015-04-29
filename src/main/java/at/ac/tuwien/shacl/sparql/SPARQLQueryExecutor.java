package at.ac.tuwien.shacl.sparql;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public class SPARQLQueryExecutor {
	
	/**
	 * 
	 * 
	 * @param query
	 * @param bindings
	 * @param model
	 * @return
	 */
	public boolean isQueryValid(final Query query, final QuerySolutionMap bindings, final Model model) {
		QueryExecution exec = QueryExecutionFactory.create(query, model, bindings);
		ResultSet results = exec.execSelect();
		
		try {
			if(results.hasNext()) {
				return false;
			} else {
				return true;
			}
		} finally {
			exec.close();
		}
	}
	
	/**
	 * Executes a select query. If the query is not valid (i.e. constraint is violated),
	 * it will return a result set that is not empty.
	 * 
	 * @param query
	 * @param bindings
	 * @param model
	 * @return
	 */
	private ResultSet executeSelectQuery(final Query query, final QuerySolutionMap bindings, final Model model) {
		QueryExecution exec = QueryExecutionFactory.create(query, model, bindings);
		
		try {
			return exec.execSelect();
		} finally {
			exec.close();
		}
	}
}
