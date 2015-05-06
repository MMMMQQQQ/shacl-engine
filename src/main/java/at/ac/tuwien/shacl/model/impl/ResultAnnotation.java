package at.ac.tuwien.shacl.model.impl;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Property;

public class ResultAnnotation {
	private Property annotationProperty;
	
	private String annotationVarName;
	
	private List<Object> annotationValues;
	
	public ResultAnnotation(Property annotationProperty) {
		this.annotationProperty = annotationProperty;
	}

	public Property getAnnotationProperty() {
		return annotationProperty;
	}

	public void setAnnotationProperty(Property annotationProperty) {
		this.annotationProperty = annotationProperty;
	}

	public String getAnnotationVarName() {
		return annotationVarName;
	}

	public void setAnnotationVarName(String annotationVarName) {
		this.annotationVarName = annotationVarName;
	}

	public List<Object> getAnnotationValues() {
		return annotationValues;
	}

	public void setAnnotationValues(List<Object> annotationValues) {
		this.annotationValues = annotationValues;
	}

	public void addAnnotationValue(Object annotationValue) {
		if(this.annotationValues == null) {
			this.annotationValues = new ArrayList<Object>();
		}
		this.annotationValues.add(annotationValue);
	}
}
