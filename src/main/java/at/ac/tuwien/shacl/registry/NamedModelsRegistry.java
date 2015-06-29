package at.ac.tuwien.shacl.registry;

import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * A registry for all named models.
 * 
 * @author xlin
 *
 */
public class NamedModelsRegistry {
	private Dataset dataset;
	
	private Map<String, Model> errorModels;
	
	public NamedModelsRegistry() {
		this.dataset = DatasetFactory.createMem();
		this.errorModels = new HashMap<String, Model>();
	}
	
	public void addNamedGraph(String uri, Model model) {
		this.dataset.addNamedModel(uri, model);
		this.errorModels.put(uri, ModelFactory.createDefaultModel());
	}
	
	public Dataset getDataset() {
		return dataset;
	}
	
	public Model getNamedModel(String uri) {
		return dataset.getNamedModel(uri);
	}
	
	public void removeNamedGraph(String uri) {
		dataset.removeNamedModel(uri);
	}
	
	public void setDefaultModel(Model model) {
		dataset.setDefaultModel(model);
	}
	
	public Model getErrorModel(String uri) {
		Model errorModel = this.errorModels.get(uri);
		
		if(errorModel == null) {
			errorModel = ModelFactory.createDefaultModel();
			this.errorModels.put(uri, errorModel);
		}
		
		return errorModel;
	}
	
	
	private static NamedModelsRegistry registry;
	
	public static NamedModelsRegistry get() {
		if(registry == null) {
			registry = new NamedModelsRegistry();
		}
		
		return registry;
	}
}
