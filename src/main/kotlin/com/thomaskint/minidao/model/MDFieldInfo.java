/*
 * MIT License
 *
 * Copyright (c) 2017 Thomas Kint
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.thomaskint.minidao.model;

import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDInheritLink;
import com.thomaskint.minidao.annotation.MDManyToOne;
import com.thomaskint.minidao.annotation.MDOneToMany;
import com.thomaskint.minidao.enumeration.MDSQLAction;
import com.thomaskint.minidao.utils.MDStringUtils;

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
		this.field.setAccessible(true);
	}

	public Field getField() {
		return field;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public MDEntityInfo getMDEntityInfo() {
		return entityInfo;
	}

	public String getTableName() {
		return entityInfo.getTableName();
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
		return getTableName() + MDStringUtils.DOT + getFieldName();
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
