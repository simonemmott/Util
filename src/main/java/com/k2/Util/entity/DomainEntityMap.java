package com.k2.Util.entity;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.classes.ClassUtil;
import com.k2.Util.exceptions.UtilityError;

public class DomainEntityMap<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public DomainEntityMap(Class<T> cls) {
		logger.trace("Initialising DomainEntityMap for class {}", cls.getName());
		this.cls = cls;
		if (EntityUtil.isRootEntity(cls)) {
			this.rootEntityType = cls;
			return;
		}
		for (Field f : ClassUtil.getAllFields(cls)) {
			if (f.isAnnotationPresent(RootEntity.class)) {
				this.rootEntityField = f;
				this.rootEntityType = rootEntityField.getType();
			}
			if (f.isAnnotationPresent(ParentEntity.class)) {
				this.parentEntityField = f;
				this.parentEntityType = parentEntityField.getType();
			}
		}
		if (rootEntityType == null) {
			if (parentEntityType != null) {
				logger.trace("Parent entity type of class {} is {}", cls.getName(), parentEntityType.getName());
				rootEntityType = EntityUtil.getRootEntityType(parentEntityType);
			}
		}
		
	}
	private final Class<T> cls;
	public Class<T> getMapForType() { return cls; }
	
	private Class<?> rootEntityType;
	public Class<?> getRootEntityType() { return rootEntityType; }
	
	private Class<?> parentEntityType;
	public Class<?> getParentEntityType() { return parentEntityType; }
	
	private Field rootEntityField;
	public Object getRootEntity(Object obj) {
		if (EntityUtil.isRootEntity(obj.getClass()))
			return obj;
		try {
			if (rootEntityField != null) 
				return rootEntityField.get(obj);
			else if (parentEntityField != null)
				return EntityUtil.getRootEntity(parentEntityField.get(obj));
		} catch ( IllegalArgumentException | IllegalAccessException e) {
			throw new UtilityError("Unable to identify root entity for object of type {}", e, obj.getClass().getName());
		}
		return null;
	}
	
	private Field parentEntityField;
	public Object getParentEntity(Object obj) {
		try {
			if (parentEntityField != null) 
				return parentEntityField.get(obj);
			else if (rootEntityField != null)
				return rootEntityField.get(obj);
		} catch ( IllegalArgumentException | IllegalAccessException e) {
			throw new UtilityError("Unable to identify parent entity for object of type {}", e, obj.getClass().getName());
		}
		return null;
	}
	
	

}
