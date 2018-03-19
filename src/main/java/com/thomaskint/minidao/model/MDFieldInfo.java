package com.thomaskint.minidao.model;

import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDInheritLink;
import com.thomaskint.minidao.annotation.MDManyToOne;
import com.thomaskint.minidao.annotation.MDOneToMany;
import com.thomaskint.minidao.enumeration.MDSQLAction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Class wrapper used to interact with fields parameters
 *
 * @author Thomas Kint
 */
public class MDFieldInfo {

	private Field field;

	private Class entityClass;

	private MDEntityInfo entityInfo;

	public MDFieldInfo(Field field, MDEntityInfo entityInfo) {
		this.field = field;
		this.entityClass = field.getDeclaringClass();
		this.entityInfo = entityInfo;
	}

	public Field getField() {
		return field;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public MDEntityInfo getMDEntityInfo() {
		return new MDEntityInfo(entityClass);
	}

	public String getTableName() {
		return getMDEntityInfo().getTableName();
	}

	public String getFieldName() {
		String fieldName = null;
		if (isMDField()) {
			fieldName = getMDField().fieldName();
		} else if (isManyToOne()) {
			fieldName = getMDManyToOne().fieldName();
		} else if (isOneToMany()) {
			fieldName = getMDOneToMany().fieldName();
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

	public <T extends Annotation> Annotation getAnnotation(Class<T> annotation) {
		return field.getDeclaredAnnotation(annotation);
	}

	public boolean isMDField() {
		return getMDField() != null;
	}

	public boolean isManyToOne() {
		return getMDManyToOne() != null;
	}

	public boolean isOneToMany() {
		return getMDOneToMany() != null;
	}

	/**
	 * Verify if the SQL action is allowed on this field
	 *
	 * @param sqlAction {@link MDSQLAction}
	 * @return allowed boolean
	 */
	public boolean isSQLActionAllowed(MDSQLAction sqlAction) {
		boolean allowed = false;

		if (sqlAction != null) {
			int i = 0;
			while (i < getMDField().allowedSQLActions().length && !allowed) {
				if (getMDField().allowedSQLActions()[i].equals(sqlAction)) {
					allowed = true;
				}
				i++;
			}
		} else {
			allowed = true;
		}

		return allowed;
	}
}
