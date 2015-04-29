package at.ac.tuwien.shacl.sparql.querying;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.shacl.model.SHACLEntity;
import at.ac.tuwien.shacl.vocabulary.SHACLType;

public class VocabularyRegistry {
	private Map<SHACLType, Class<? extends SHACLEntity>> registry;

	public VocabularyRegistry() {
		this.registry = new HashMap<SHACLType, Class<? extends SHACLEntity>>();
	}
	
	public void register(SHACLType type, Class<? extends SHACLEntity> entityClass) {
		this.registry.put(type, entityClass);
	}
	
	public void unregister(SHACLType type) {
		this.registry.remove(type);
	}
	
	public void getEmptyInstance(SHACLType type) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		Class<?> clazz = Class.forName(registry.get(type).getName());
		Constructor<?> ctor = clazz.getConstructor();
		Object object = ctor.newInstance();
		System.out.println("instance generated for object of type "+object.getClass());
	}
}
