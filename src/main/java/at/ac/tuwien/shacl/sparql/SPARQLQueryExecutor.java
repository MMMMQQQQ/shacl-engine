package at.ac.tuwien.shacl.sparql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.resultset.RDFOutput;
import com.hp.hpl.jena.sparql.vocabulary.ResultSetGraphVocab;
import com.hp.hpl.jena.vocabulary.XSD;

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
	
	public static boolean execAsk(final String queryString, final Model model, QuerySolutionMap bindings) {
		QueryExecution exec = QueryExecutionFactory.create(queryString, model, bindings);
		//System.out.println(exec.getQuery());
		boolean result = exec.execAsk();
		return result;
	}
	
	public static RDFNode execSelect(final String queryString, final Model model, QuerySolutionMap bindings) {
		QueryExecution exec = QueryExecutionFactory.create(queryString, model, bindings);
		//System.out.println(exec.getQuery());
		ResultSet result = exec.execSelect();

		RDFNode r = result.next().get("result");
		
		return r;
	}
	
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
	
	public static Model executeQuery(String queryString, final Model model, QuerySolutionMap bindings) {

		QueryExecution exec = QueryExecutionFactory.create(queryString, model, bindings);
		System.out.println(exec.getQuery());
		ResultSet results = exec.execSelect();
		
		Model resultmodel = null;
		
		try {
			resultmodel = RDFOutput.encodeAsModel(results);
			
			resultmodel.write(System.out, "TURTLE");
			
			if(resultmodel.listStatements(null, ResultSetGraphVocab.size, (RDFNode) null)
					.toList().get(0).getInt() == 0) {
				resultmodel = null;
			}

			return resultmodel;
		} finally {
			exec.close();
		}
	}
	
	public static QuerySolution getQuerySolutionForQuery(String queryString, final Model model, QuerySolutionMap bindings) {

		QueryExecution exec = QueryExecutionFactory.create(queryString, model, bindings);
		System.out.println(exec.getQuery());
		ResultSet results = exec.execSelect();
		
		QuerySolution solution = null;

		try {
			List<QuerySolution> list = ResultSetFormatter.toList(results);
			
			if(list.size() > 0) {
				solution = list.get(0);
			}

			return solution;
		} finally {
			exec.close();
		}
	}
	
	/**
	 * Execute query and return a map containing all variables and their values.
	 * Note: If more than one row is returned, the map will only contain the values for the
	 * first row.
	 * 
	 * @param queryString
	 * @param model
	 * @param bindings
	 * @return
	 */
	public static Map<String, Object> getMapResultsForQuery(String queryString, final Model model, QuerySolutionMap bindings) {

		QueryExecution exec = QueryExecutionFactory.create(queryString, model, bindings);
		System.out.println(exec.getQuery());
		ResultSet results = exec.execSelect();
		
		try {
			List<QuerySolution> list = ResultSetFormatter.toList(results);
			HashMap<String, Object> variables = new HashMap<String, Object>();
			
			if(list.size() > 0) {
				Iterator<String> it = list.get(0).varNames();
				
				while(it.hasNext()) {
					String varName = it.next();
					RDFNode node = list.get(0).get(varName);
					
					if(node.isLiteral()) {
						variables.put(varName, list.get(0).get(varName).asLiteral().getString());
					} else {
						variables.put(varName, list.get(0).get(varName));
					}
					
					
				}
			}

			return variables;
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
			System.out.println("ARQ result:");
			System.out.println(ResultSetFormatter.asText(results));
			return isValid;
		} finally {
			exec.close();
		}
	}

	//TODO							
	public RDFNode executeQuery(Query query, QuerySolutionMap bindings, Model model) {
		return null;
	}
	
	public static boolean isAskQuery(String query) {
		Query q = QueryFactory.create(query);
		return q.isAskType();
	}
	
	public static boolean isSelectQuery(String query) {
		Query q = QueryFactory.create(query);
		return q.isSelectType();
	}
}
