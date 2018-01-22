package com.thomaskint.minidao.utils;

import com.thomaskint.minidao.annotations.MDField;
import com.thomaskint.minidao.annotations.MDId;
import com.thomaskint.minidao.enumeration.MDParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDFieldUtils {

	public static List<Field> getFields(Class entityClass) {
		return getFields(entityClass, null);
	}

	/**
	 * Return every @{@link MDField} of given entity
	 *
	 * @param entityClass
	 * @param param
	 * @return
	 */
	public static List<Field> getFields(Class entityClass, MDParam param) {
		List<Field> fields = new ArrayList<>();

		for (Field field : entityClass.getFields()) {
			if (isMDField(field) && includeParam(field, param)) {
				fields.add(field);
			}
		}

		return fields;
	}

	public static Field getIdField(Class entityClass) {
		Field idField = null;
		List<Field> fields = getFields(entityClass);

		int i = 0;
		while (i < fields.size() && idField == null) {
			if (fields.get(i).getDeclaredAnnotation(MDId.class) != null) {
				idField = fields.get(i);
			}
			i++;
		}

		return idField;
	}

	public static String getIdFieldName(Class entityClass) {
		return getFieldName(getIdField(entityClass));
	}

	/**
	 * Return field name based on its @{@link MDField} name
	 *
	 * @param field
	 * @return
	 */
	public static String getFieldName(Field field) {
		String fieldName = null;

		if (isMDField(field)) {
			MDField mdField = field.getDeclaredAnnotation(MDField.class);
			fieldName = mdField.name();
		}

		return fieldName;
	}

	public static Field getFieldByName(Class entityClass, String name) {
		Field field = null;
		List<Field> fields = getFields(entityClass);

		int i = 0;
		while (i < fields.size() && field == null) {
			if (getFieldName(fields.get(i)).equals(name)) {
				field = fields.get(i);
			}
			i++;
		}

		return field;
	}

	/**
	 * Verify if given field is annotated by @{@link MDField}
	 *
	 * @param field
	 * @return
	 */
	public static boolean isMDField(Field field) {
		boolean isMDField = false;
		Annotation[] annotations = field.getDeclaredAnnotations();

		int i = 0;
		while (i < annotations.length && !isMDField) {
			if (annotations[i] instanceof MDField) {
				isMDField = true;
			}
			i++;
		}

		return isMDField;
	}

	/**
	 * Verify if given field is annotated by @{@link MDField}
	 *
	 * @param field
	 * @return
	 */
	public static boolean includeParam(Field field, MDParam param) {
		boolean includeParams = false;

		if (isMDField(field)) {
			if (param != null) {
				MDField mdField = field.getDeclaredAnnotation(MDField.class);
				int i = 0;
				while (i < mdField.params().length && !includeParams) {
					if (mdField.params()[i].equals(param)) {
						includeParams = true;
					}
					i++;
				}
			} else {
				includeParams = true;
			}
		}

		return includeParams;
	}
}
