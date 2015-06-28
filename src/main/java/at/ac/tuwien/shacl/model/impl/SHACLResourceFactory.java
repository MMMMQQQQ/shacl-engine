package at.ac.tuwien.shacl.model.impl;

import at.ac.tuwien.shacl.model.Argument;
import at.ac.tuwien.shacl.model.Constraint;
import at.ac.tuwien.shacl.model.ConstraintViolation;
import at.ac.tuwien.shacl.model.Function;
import at.ac.tuwien.shacl.model.Shape;
import at.ac.tuwien.shacl.model.Template;
import at.ac.tuwien.shacl.model.impl.constraints.NativeConstraint;
import at.ac.tuwien.shacl.model.impl.constraints.PropertyConstraint;
import at.ac.tuwien.shacl.model.impl.constraints.TemplateConstraint;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class SHACLResourceFactory {
	public static Function createFunction(RDFNode functionNode) {
		return new FunctionImpl(functionNode.asNode(), (EnhGraph) functionNode.getModel());
	}
	
	public static Constraint createConstraint(RDFNode constraintNode) {
		return new ConstraintImpl(constraintNode.asNode(), (EnhGraph) constraintNode.getModel());
	}
	
	public static Argument createArgument(RDFNode argumentNode) {
		return new ArgumentImpl(argumentNode.asNode(), (EnhGraph) argumentNode.getModel());
	}
	
	public static Template createTemplate(RDFNode templateNode) {
		return new TemplateImpl(templateNode.asNode(), (EnhGraph) templateNode.getModel());
	}
	
	public static Template createConstraintTemplate(RDFNode templateNode) {
		return new TemplateImpl(templateNode.asNode(), (EnhGraph) templateNode.getModel());
	}
	
	public static Shape createShape(RDFNode shapeNode) {
		return new ShapeImpl(shapeNode.asNode(), (EnhGraph) shapeNode.getModel());
	}
	
	public static ConstraintViolation createConstraintViolation(Resource violationType, Resource root, 
			Resource subject, Resource predicate, Resource object) {
		return new ConstraintViolationImpl(violationType, root, subject, predicate, object);
	}
	
	public static PropertyConstraint createPropertyConstraint(RDFNode constraintNode) {
		return new PropertyConstraint(constraintNode.asNode(), (EnhGraph) constraintNode.getModel());
	}
	
	public static NativeConstraint createNativeConstraint(RDFNode constraintNode) {
		return new NativeConstraint(constraintNode.asNode(), (EnhGraph) constraintNode.getModel());
	}
	
	public static TemplateConstraint createTemplateConstraint(RDFNode constraintNode) {
		return new TemplateConstraint(constraintNode.asNode(), (EnhGraph) constraintNode.getModel());
	}
}
