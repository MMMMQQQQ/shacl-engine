package at.ac.tuwien.shacl.registry;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class NamedModels {
	private Dataset dataset;
	
	public NamedModels() {
		this.dataset = DatasetFactory.createMem();
	}
	
	public void addNamedGraph(String uri, Model model) {
		dataset.addNamedModel(uri, model);
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
}
