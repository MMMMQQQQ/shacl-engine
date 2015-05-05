package at.ac.tuwien.shacl.sparql;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Execute a query. Currently only supported for constraint checking queries.
 * 
 * @author Xiashuo Lin
 *
 */
public class SPARQLQueryExecutor {
	
	//TODO transform to other query methods
//	public static boolean isQueryValid(final Query query, final Model model) {
//		QueryExecution exec = QueryExecutionFactory.create(query, model);
//		ResultSet results = exec.execSelect();
//
//		boolean isValid = true;
//		try {
//			if(results.hasNext()) {
//				isValid = false;
//			}
//			System.out.println(ResultSetFormatter.asText(results));
//			return isValid;
//		} finally {
//			exec.close();
//		}
//	}
//	
	
	public static boolean isQueryValid(final String queryString, final Model model, QuerySolutionMap bindings) {
		QueryExecution exec = QueryExecutionFactory.create(queryString, model, bindings);
		System.out.println(exec.getQuery());
		ResultSet results = exec.execSelect();
		boolean isValid = true;
		try {
			if(results.hasNext()) {
				isValid = false;
			}
			
			System.out.println(ResultSetFormatter.asText(results));
			return isValid;
		} finally {
			exec.close();
		}
	}
	
	public static boolean isQueryValid(final String queryString) {
		QueryExecution exec = QueryExecutionFactory.create(queryString);
		
		ResultSet results = exec.execSelect();
		boolean isValid = true;
		try {
			if(results.hasNext()) {
				isValid = false;
			}
			
			System.out.println(ResultSetFormatter.asText(results));
			return isValid;
		} finally {
			exec.close();
		}
	}
	
	//TODO
	public RDFNode executeQuery(Query query, Model model) {
		//how to map sparql query result with graph
//		while (results.hasNext()) {
//	        QuerySolution result=results.next();
//	        RDFNode s=result.get("this");
//	        RDFNode p=result.get("predicate");
//	        RDFNode o=result.get("xxxxx");
//	        System.out.println(" { " + s + " "+ p+ " "+ o+ " . }");
//	      }
		return null;
	}
	
	//TODO							
	public RDFNode executeQuery(Query query, QuerySolutionMap bindings, Model model) {
		return null;
	}
}
