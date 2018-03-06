/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskint.minidao.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thomaskint.minidao.annotations.MDEntity;
import com.thomaskint.minidao.annotations.MDField;
import com.thomaskint.minidao.enumeration.MDParam;

/**
 * @author tkint
 */
public class MDEntityUtils {

	/**
	 * Return table name based on @{@link MDEntity} name annotation
	 *
	 * @param entityClass {@link Class}
	 * @return {@link String}
	 */
	public static String getTableName(Class entityClass) {
		String tableName = null;

		if (isMDEntity(entityClass)) {
			MDEntity mdEntity = (MDEntity) entityClass.getDeclaredAnnotation(MDEntity.class);
			tableName = mdEntity.name();
		}

		return tableName;
	}

	/**
	 * Verify if given entity is annotated by @{@link MDField}
	 *
	 * @param entityClass {@link Class}
	 * @return boolean
	 */
	public static boolean isMDEntity(Class entityClass) {
		boolean isMDEntity = false;
		Annotation[] annotations = entityClass.getDeclaredAnnotations();

		int i = 0;
		while (i < annotations.length && !isMDEntity) {
			if (annotations[i] instanceof MDEntity) {
				isMDEntity = true;
			}
			i++;
		}

		return isMDEntity;
	}


	/**
	 * Verify if given entity include given param
	 *
	 * @param entityClass {@link Class}
	 * @param param {@link MDParam}
	 * @return boolean
	 */
	public static boolean includeParam(Class entityClass, MDParam param) {
		boolean includeParam = false;

		if (isMDEntity(entityClass)) {
			if (param != null) {
				MDEntity mdEntity = (MDEntity) entityClass.getDeclaredAnnotation(MDEntity.class);
				int i = 0;
				while (i < mdEntity.params().length && !includeParam) {
					if (mdEntity.params()[i].equals(param)) {
						includeParam = true;
					}
					i++;
				}
			} else {
				includeParam = true;
			}
		}

		return includeParam;
	}

	/**
	 * Return instance of entity based on its entity class and given resultset
	 *
	 * @param entityClass {@link Class}
	 * @param resultSet {@link ResultSet}
	 * @param <T> T
	 * @return T
	 */
	public static <T> T mapEntity(Class<T> entityClass, ResultSet resultSet) {
		T instance = null;

		try {
			instance = entityClass.newInstance();

			Object o;
			Field idField = MDFieldUtils.getIdField(entityClass);
			if (idField != null) {
				o = resultSet.getObject(MDFieldUtils.getFieldName(idField));
				idField.set(instance, o);
			}

			for (Field field : MDFieldUtils.getFields(entityClass)) {
				o = resultSet.getObject(MDFieldUtils.getFieldName(field));
				field.set(instance, o);
			}

		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}

		return instance;
	}
}
