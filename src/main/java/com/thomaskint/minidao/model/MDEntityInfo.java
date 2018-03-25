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

import com.thomaskint.minidao.annotation.*;
import com.thomaskint.minidao.crud.MDRead;
import com.thomaskint.minidao.enumeration.MDLoadPolicy;
import com.thomaskint.minidao.enumeration.MDSQLAction;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.querybuilder.MDCondition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY;

/**
 * Class wrapper used to interact with entities parameters
 *
 * @author Thomas Kint
 */
public class MDEntityInfo {

	private Class entityClass;

	private Annotation[] annotations;

	private List<MDFieldInfo> fieldInfos = new ArrayList<>();

	public MDEntityInfo(Class entityClass) {
		this.entityClass = entityClass;
		this.annotations = entityClass.getDeclaredAnnotations();
		MDFieldInfo fieldInfo;
		for (Field field : entityClass.getFields()) {
			if ((fieldInfo = new MDFieldInfo(field, this)).isMDField()) {
				fieldInfos.add(fieldInfo);
			}
		}
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public List<MDFieldInfo> getMDFieldInfos() {
		return fieldInfos;
	}

	public MDEntity getMDEntity() {
		return (MDEntity) getAnnotation(MDEntity.class);
	}

	public String getTableName() {
		return getMDEntity().tableName();
	}

	public <T extends Annotation> Annotation getAnnotation(Class<T> annotation) {
		return entityClass.getDeclaredAnnotation(annotation);
	}

	public boolean isMDEntity() {
		return getMDEntity() != null;
	}

	/**
	 * Verify if the SQL action is allowed on this entity
	 *
	 * @param sqlAction {@link MDSQLAction}
	 * @return allowed boolean
	 */
	public boolean isSQLActionAllowed(MDSQLAction sqlAction) {
		boolean allowed = false;

		if (sqlAction != null) {
			int i = 0;
			while (i < getMDEntity().allowedSQLActions().length && !allowed) {
				if (getMDEntity().allowedSQLActions()[i].equals(sqlAction)) {
					allowed = true;
				}
				i++;
			}
		} else {
			allowed = true;
		}

		return allowed;
	}

	/**
	 * Get superClass as MDEntityInfo
	 *
	 * @return entityInfo {@link MDEntityInfo}
	 */
	public MDEntityInfo getParentEntityInfo() {
		MDEntityInfo parent;
		if (!(parent = new MDEntityInfo(entityClass.getSuperclass())).isMDEntity()) {
			parent = null;
		}
		return parent;
	}

	/**
	 * Get every class as {@link MDEntityInfo} linked by a {@link MDManyToOne} annotation
	 *
	 * @param loadPolicy {@link MDLoadPolicy}
	 * @return entityInfos {@link List}
	 */
	public List<MDEntityInfo> getManyToOneEntityInfos(MDLoadPolicy loadPolicy) {
		List<MDEntityInfo> entityInfos = new ArrayList<>();
		MDEntityInfo entityInfo;
		for (MDFieldInfo fieldInfo : getManyToOnes()) {
			if (fieldInfo.getMDManyToOne().loadPolicy().equals(loadPolicy)) {
				entityInfo = new MDEntityInfo(fieldInfo.getMDManyToOne().target());
				entityInfos.add(entityInfo);
			}
		}
		return entityInfos;
	}

	/**
	 * Get every fields as {@link MDFieldInfo} linked by a {@link MDOneToMany} annotation
	 *
	 * @param loadPolicy {@link MDLoadPolicy}
	 * @return entityInfos {@link List}
	 */
	public List<MDFieldInfo> getOneToManyFieldInfos(MDLoadPolicy loadPolicy) {
		List<MDFieldInfo> fieldInfos = new ArrayList<>();
		for (MDFieldInfo fieldInfo : getOneToManys()) {
			if (fieldInfo.getMDOneToMany().loadPolicy().equals(loadPolicy)) {
				fieldInfos.add(fieldInfo);
			}
		}
		return fieldInfos;
	}

	/**
	 * Get {@link MDId} annotation
	 *
	 * @return id {@link MDId}
	 */
	public MDFieldInfo getIDFieldInfo() {
		return getFieldInfoByAnnotation(MDId.class);
	}

	/**
	 * Get {@link MDInheritLink} annotation
	 *
	 * @return inheritLink {@link MDInheritLink}
	 */
	public MDFieldInfo getInheritLink() {
		return getFieldInfoByAnnotation(MDInheritLink.class);
	}

	/**
	 * Get {@link MDManyToOne} annotated fields
	 *
	 * @return fieldInfos {@link List}
	 */
	public List<MDFieldInfo> getManyToOnes() {
		return getFieldsByAnnotation(MDManyToOne.class);
	}

	/**
	 * Get {@link MDOneToMany} annotated fields
	 *
	 * @return fieldInfos {@link List}
	 */
	public List<MDFieldInfo> getOneToManys() {
		return getFieldsByAnnotation(MDOneToMany.class);
	}

	/**
	 * Get list of fields annotated by given annotation as a {@link MDFieldInfo} list
	 *
	 * @param annotation {@link Class}
	 * @param <U>        U extends {@link Annotation}
	 * @return fieldInfos {@link List}
	 */
	private <U extends Annotation> List<MDFieldInfo> getFieldsByAnnotation(Class<U> annotation) {
		List<MDFieldInfo> fieldInfos = new ArrayList<>();
		for (Field field : entityClass.getFields()) {
			MDFieldInfo fieldInfo = new MDFieldInfo(field, this);
			if (fieldInfo.getAnnotation(annotation) != null) {
				fieldInfos.add(fieldInfo);
			}
		}
		return fieldInfos;
	}

	/**
	 * Get first field annotated by given annotation as an {@link MDFieldInfo}
	 *
	 * @param annotation {@link Class}
	 * @param <U>        U extends {@link Annotation}
	 * @return fieldInfo {@link MDFieldInfo}
	 */
	private <U extends Annotation> MDFieldInfo getFieldInfoByAnnotation(Class<U> annotation) {
		MDFieldInfo fieldInfo = null;
		int i = 0;
		while (i < fieldInfos.size() && fieldInfo == null) {
			if (fieldInfos.get(i).getAnnotation(annotation) != null) {
				fieldInfo = fieldInfos.get(i);
			}
			i++;
		}
		return fieldInfo;
	}

	/**
	 * Instantiate an object and build it based on the given {@link ResultSet}
	 *
	 * @param <T>                     T
	 * @param resultSet               {@link ResultSet}
	 * @param read                    {@link MDRead}
	 * @param alreadyRetrievedClasses {@link Set}
	 * @return instance T
	 * @throws MDException Something went wrong
	 */
	public <T> T mapEntity(ResultSet resultSet, MDRead read, Set<Class> alreadyRetrievedClasses) throws MDException {
		T instance = null;

		try {
			instance = (T) entityClass.newInstance();

			// ID Field
			Object id = null;
			MDFieldInfo idFieldInfo = getIDFieldInfo();
			if (idFieldInfo != null) {
				id = resultSet.getObject(idFieldInfo.getFieldName());
				idFieldInfo.getField().set(instance, id);
			}

			// Fields
			Object object;
			for (MDFieldInfo fieldInfo : fieldInfos) {
				object = resultSet.getObject(fieldInfo.getFieldName());
				fieldInfo.getField().set(instance, object);
			}

			// Many To Ones
			List<MDFieldInfo> manyToOneFieldInfos = getManyToOnes();
			MDEntityInfo entityInfo;
			for (MDFieldInfo fieldInfo : manyToOneFieldInfos) {
				if (fieldInfo.getMDManyToOne().loadPolicy().equals(HEAVY)) {
					entityInfo = new MDEntityInfo(fieldInfo.getMDManyToOne().target());
					if (!alreadyRetrievedClasses.contains(entityInfo.getEntityClass())) {
						alreadyRetrievedClasses.add(entityInfo.getEntityClass());
						object = entityInfo.mapEntity(resultSet, read, alreadyRetrievedClasses);
						fieldInfo.getField().set(instance, object);
					}
				}
			}

			// One To Manys
			List entities;
			MDOneToMany oneToMany;
			MDCondition subCondition;
			List<MDFieldInfo> oneToManyFieldInfos = getOneToManyFieldInfos(HEAVY);
			if (id != null) {
				for (MDFieldInfo oneToManyFieldInfo : oneToManyFieldInfos) {
					oneToMany = oneToManyFieldInfo.getMDOneToMany();
					if (!alreadyRetrievedClasses.contains(oneToMany.target())) {
						alreadyRetrievedClasses.add(oneToMany.target());
						subCondition = new MDCondition(oneToMany.targetFieldName(), EQUAL, id);
						entities = read.getEntities(oneToMany.target(), subCondition, alreadyRetrievedClasses);
						oneToManyFieldInfo.getField().set(instance, entities);
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}

		return instance;
	}

	/**
	 * Get first {@link MDFieldInfo} matching fieldName
	 *
	 * @param fieldName {@link String}
	 * @return fieldInfo {@link MDFieldInfo}
	 */
	public MDFieldInfo getMDFieldInfoByFieldName(String fieldName) {
		MDFieldInfo fieldInfo = null;
		int i = 0;
		while (i < fieldInfos.size() && fieldInfo == null) {
			if (fieldInfos.get(i).getFieldName().equals(fieldName)) {
				fieldInfo = fieldInfos.get(i);
			}
			i++;
		}
		return fieldInfo;
	}

	/**
	 * Get first {@link MDFieldInfo} pointing given {@link Class}
	 * It may be either an {@link MDInheritLink} or an {@link MDManyToOne}
	 *
	 * @param entityClass {@link Class}
	 * @param <T>         T
	 * @return fieldInfo {@link MDFieldInfo}
	 */
	public <T> MDFieldInfo getFieldInfoLinkedTo(Class<T> entityClass) {
		MDFieldInfo fieldInfo = null;
		List<MDFieldInfo> fieldInfos = getFieldsByAnnotation(MDManyToOne.class);
		int i = 0;
		while (i < fieldInfos.size() && fieldInfo == null) {
			if (fieldInfos.get(i).getMDManyToOne().target().equals(entityClass)) {
				fieldInfo = fieldInfos.get(i);
			}
			i++;
		}
		if (fieldInfo == null) {
			MDEntityInfo parentEntityInfo = getParentEntityInfo();
			if (parentEntityInfo != null && parentEntityInfo.getEntityClass().equals(entityClass)) {
				fieldInfo = getInheritLink();
			}
		}
		return fieldInfo;
	}
}
