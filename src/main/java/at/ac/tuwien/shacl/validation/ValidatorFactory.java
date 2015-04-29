package at.ac.tuwien.shacl.validation;

import com.hp.hpl.jena.rdf.model.Model;

public class ValidatorFactory {
	public SPARQLValidator createSPARQLValidator(Model model) {
		return new SPARQLValidator(model);
	}
}
