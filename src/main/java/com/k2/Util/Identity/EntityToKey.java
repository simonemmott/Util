package com.k2.Util.Identity;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.EmbeddedId;
import javax.persistence.IdClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;

import com.k2.Util.classes.ClassUtil;
import com.k2.Util.exceptions.UtilityError;

public interface EntityToKey<E, K> {

	static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public Class<E> getEntityClass();
	public Class<K> getKeyClass();
	public K getKey(E entity);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <E> EntityToKey<E,?> forClass(Class<E> entityClass) {
		
		IdClass idClass = ClassUtil.getAnnotation(entityClass, IdClass.class);
		if (idClass != null)
			return new EntityIdClassToKey(entityClass, idClass.value());
		
		Field[] embeddedIdField = ClassUtil.getAllAnnotatedFields(entityClass, EmbeddedId.class);
		if (embeddedIdField.length == 1)
			return new EntityFieldToKey(entityClass, embeddedIdField[0]);
		
		Method[] embeddedIdMethod = ClassUtil.getAllAnnotatedMethods(entityClass, EmbeddedId.class);
		if (embeddedIdMethod.length == 1)
			return new EntityMethodToKey(entityClass, embeddedIdMethod[0]);

		Field[] IdField = ClassUtil.getAllAnnotatedFields(entityClass, Id.class);
		if (IdField.length == 1)
			return new EntityFieldToKey(entityClass, IdField[0]);
		
		Method[] IdMethod = ClassUtil.getAllAnnotatedMethods(entityClass, Id.class);
		if (IdMethod.length == 1)
			return new EntityMethodToKey(entityClass, IdMethod[0]);
		
		Member id = IdentityUtil.getIdMember(entityClass);
		if (id != null) {
			if (id instanceof Field) 
				return new EntityFieldToKey(entityClass, (Field) id);
			if (id instanceof Method) 
				return new EntityMethodToKey(entityClass, (Method) id);
			
		}
		throw new UtilityError("Unable to identify suitable key source for entity {}", entityClass.getName());

	}
}
