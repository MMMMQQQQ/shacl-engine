package at.ac.tuwien.shacl.validation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.sparql.queries.SPARQLConstraintQuery;
import at.ac.tuwien.shacl.util.ModelRegistry;
import at.ac.tuwien.shacl.util.SHACLModelLoader;
import at.ac.tuwien.shacl.vocabulary.SHACL;
import at.ac.tuwien.shacl.vocabulary.SelectionProperty;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class SPARQLValidator {
	private Model model;
	
	private ModelRegistry registry;
	
	public SPARQLValidator(Model model) {
		this.model = model;
		this.registry = SHACLModelLoader.getModelRegistry();
	}
	
	public Model validateAll() {
		Model errorModel = ModelFactory.createDefaultModel();
		
		List<Statement> shapeStatements = this.getInstantiatedShapeTriplets();
		
		for(Statement s : shapeStatements) {
			Resource resource = s.getResource();
			for(Statement property : resource.listProperties(SHACL.property).toList()) {
				errorModel = this.checkForPropertyConstraints(s.getSubject(), property.getResource());
			}
		}
		
		return errorModel;
	}
	
	/**
	 * Check for violations of property constraints.
	 * Property constraints include: 
	 * 
	 * @return
	 */
	/*private Model checkForPropertyConstraints(Resource focusNode, Resource constraint) {
		Model errorModel = ModelFactory.createDefaultModel();
		
		HasValueBuilder hasValueModelBuilder = new HasValueBuilder();
		SHACLEntity hasValue = hasValueModelBuilder.build(focusNode, constraint);
		HasValueQuery hasValueQuery = new HasValueQuery(hasValue);
		if(!hasValueQuery.executeQuery(model)) {
			errorModel = hasValueModelBuilder.buildViolationModel(hasValue);
		}
		
		return errorModel;
	}*/
	
	/**
	 * Check for all property constraints that are registered in the registry.
	 * 
	 * @param focusNode
	 * @param constraint
	 * @return
	 */
	private Model checkForPropertyConstraints(Resource focusNode, Resource constraint) {
		Model errorModel = ModelFactory.createDefaultModel();
		try {
			for(Class<? extends ModelBuilder> builderClass : registry.getAll().keySet()) {
				ModelBuilder builder = registry.getInstanceOfBuilderClass(builderClass);
				SHACLEntity entity = builder.build(focusNode, constraint);
				SPARQLConstraintQuery query = registry.getInstanceOfQueryClass(builderClass, entity);
				if(!query.executeQuery(model)) {
					errorModel.add(builder.buildViolationModel(entity));
				}
			}
		} catch(NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			e.printStackTrace();
		}

		return errorModel;
	}
	
	/**
	 *  Determine which nodes need to be evaluated against which shapes.
	 *  There are two shape selection properties currently: sh:nodeShape and rdf:type
	 *  (Section 11.1)
	 * 
	 * @return a list of statements (triplets) that will each contain:
	 * 		- the instance of the shape (i.e. the concrete data) as subject
	 * 		- the shape definition as object
	 * 		- the selection property (sh:nodeShape/rdf:type) as predicate
	 */
	private List<Statement> getInstantiatedShapeTriplets() {
		List<Statement> statements = new ArrayList<Statement>();
		
		for(SelectionProperty p : SelectionProperty.values()) {
			statements.addAll(model.listStatements(null, p.getProperty(), (RDFNode)null).toList());
		}
		
		return statements;
	}

//	private void addSuperClasses(Resource shape, Set<Resource> shapes) {
//		shapes.add(shape);
//		for(Statement s : shape.listProperties(RDFS.subClassOf).toList()) {
//			
//			if(!shapes.contains(s.getResource())) {
//				addSuperClasses(s.getResource(), shapes);
//			}
//		}
//	}
}
