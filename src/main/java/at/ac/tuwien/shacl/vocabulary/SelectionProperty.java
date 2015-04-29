package at.ac.tuwien.shacl.vocabulary;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.RDF;

public enum SelectionProperty {
	rdfType(RDF.type),
	shNodeShape(SHACL.nodeShape);
	
	private Property property;
	
	private SelectionProperty(Property property) {
		this.property = property;
	}
	
	public Property getProperty() {
		return this.property;
	}
}
