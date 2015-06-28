package at.ac.tuwien.shacl.metamodel;

import java.util.List;

import at.ac.tuwien.shacl.model.SHACLResource;

import com.hp.hpl.jena.rdf.model.Property;

public interface ResultAnnotation extends SHACLResource {
	public Property getAnnotationProperty();

	public String getAnnotationVarName();

	public void setAnnotationVarName(String annotationVarName);

	public List<Object> getAnnotationValues();

	public void setAnnotationValues(List<Object> annotationValues);

	public void addAnnotationValue(Object annotationValue);
}
