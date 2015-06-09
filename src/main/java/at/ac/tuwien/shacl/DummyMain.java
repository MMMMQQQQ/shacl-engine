package at.ac.tuwien.shacl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import at.ac.tuwien.shacl.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public class DummyMain {
	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		model.read("queryData.ttl");
		model.write(System.out, "Turtle");
//		List<Statement> ps = model.listStatements(null, SHACL.sparql, (RDFNode) null).toList();
//		
//		for(Statement s : ps) {
//			System.out.println(model.listStatements().toList());
//			System.out.println(s.getSubject().getNameSpace()+":"+s.getSubject().getLocalName());
//			
//		}
	}
}
