package at.ac.tuwien.shacl.sparql;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Execute a query. Currently only supported for constraint checking queries.
 * 
 * @author Xiashuo Lin
 *
 */
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
		//CAUTION: printing it out in formatter will skew result set and might return false values.
		//de-comment the next lines only for testing purposes
		System.out.println(ResultSetFormatter.asText(results));
		//System.out.println("result set: "+results.getRowNumber());
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
}
