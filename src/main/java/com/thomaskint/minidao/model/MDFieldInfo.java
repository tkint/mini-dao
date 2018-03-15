package com.thomaskint.minidao.model;

import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDInheritLink;
import com.thomaskint.minidao.annotation.MDManyToOne;
import com.thomaskint.minidao.annotation.MDOneToMany;
import com.thomaskint.minidao.enumeration.MDVerb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author Thomas Kint
 */
public class MDFieldInfo {

	private Field field;

	private Annotation[] annotations;

	private Class entityClass;

	public MDFieldInfo(Field field) {
		this.field = field;
		this.annotations = field.getDeclaredAnnotations();
		this.entityClass = field.getDeclaringClass();
	}

	public Field getField() {
		return field;
	}

	public String getFieldName() {
		String fieldName = null;
		if (isMDField()) {
			fieldName = getMDField().name();
		} else {
			fieldName = getMDManyToOne().name();
		}
		return fieldName;
	}

	public String getFieldFullName() {
		return getTableName() + "." + getFieldName();
	}

	public MDField getMDField() {
		return (MDField) getAnnotation(MDField.class);
	}

	public MDInheritLink getMDInheritLink() {
		return (MDInheritLink) getAnnotation(MDInheritLink.class);
	}

	public MDOneToMany getMDOneToMany() {
		return (MDOneToMany) getAnnotation(MDOneToMany.class);
	}

	public MDManyToOne getMDManyToOne() {
		return (MDManyToOne) getAnnotation(MDManyToOne.class);
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public String getTableName() {
		return getMDEntityInfo().getTableName();
	}

	public <T extends Annotation> Annotation getAnnotation(Class<T> annotation) {
		return field.getDeclaredAnnotation(annotation);
	}

	public boolean isMDField() {
		return getMDField() != null;
	}

	public boolean isRelation() {
		return getMDOneToMany() != null || getMDManyToOne() != null;
	}

	public boolean isMDFieldOrRelation() {
		return isMDField() || isRelation();
	}

	public boolean includeParam(MDVerb verb) {
		boolean includeParam = false;

		if (verb != null) {
			int i = 0;
			while (i < getMDField().verbs().length && !includeParam) {
				if (getMDField().verbs()[i].equals(verb)) {
					includeParam = true;
				}
				i++;
			}
		} else {
			includeParam = true;
		}

		return includeParam;
	}

	public MDEntityInfo getMDEntityInfo() {
		return new MDEntityInfo(entityClass);
	}
}
