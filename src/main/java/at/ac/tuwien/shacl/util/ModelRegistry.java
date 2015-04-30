package at.ac.tuwien.shacl.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.sparql.queries.SPARQLConstraintQuery;
import at.ac.tuwien.shacl.validation.ModelBuilder;

public class ModelRegistry {
	private Map<Class<? extends ModelBuilder>, Class<? extends SPARQLConstraintQuery>> registry;
	
	public ModelRegistry() {
		this.registry = new HashMap<Class<? extends ModelBuilder>, Class<? extends SPARQLConstraintQuery>>();
	}
	
	public void register(Class<? extends ModelBuilder> builderClass, Class<? extends SPARQLConstraintQuery> queryClass) {
		this.registry.put(builderClass, queryClass);
	}
	
	public SPARQLConstraintQuery getInstanceOfQueryClass(Class<? extends ModelBuilder> builderClass, SHACLEntity entity) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<? extends SPARQLConstraintQuery> c = registry.get(builderClass);
		Constructor<? extends SPARQLConstraintQuery> cons = c.getConstructor();
		SPARQLConstraintQuery query = cons.newInstance();
		
		return query;
	}
	
	public ModelBuilder getInstanceOfBuilderClass(Class<? extends ModelBuilder> builderClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Constructor<? extends ModelBuilder> cons = builderClass.getConstructor();
		ModelBuilder query = cons.newInstance();
		
		return query;
	}
	
	public Map<Class<? extends ModelBuilder>, Class<? extends SPARQLConstraintQuery>> getAll() {
		return this.registry;
	}
}
