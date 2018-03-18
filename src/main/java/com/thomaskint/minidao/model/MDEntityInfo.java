package com.thomaskint.minidao.model;

import com.thomaskint.minidao.annotation.*;
import com.thomaskint.minidao.crud.MDRead;
import com.thomaskint.minidao.enumeration.MDLoadPolicy;
import com.thomaskint.minidao.enumeration.MDVerb;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.querybuilder.MDCondition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY;

/**
 * @author Thomas Kint
 */
public class MDEntityInfo {

	private Class entityClass;

	private Annotation[] annotations;

	private List<MDFieldInfo> fieldInfos = new ArrayList<>();

	public MDEntityInfo(Class entityClass) {
		this.entityClass = entityClass;
		this.annotations = entityClass.getDeclaredAnnotations();
		for (Field field : entityClass.getFields()) {
			MDFieldInfo mdFieldInfo;
			if ((mdFieldInfo = new MDFieldInfo(field)).isMDField()) {
				fieldInfos.add(mdFieldInfo);
			}
		}
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

	public Class getEntityClass() {
		return entityClass;
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

	public boolean includeParam(MDVerb mdVerb) {
		boolean includeParam = false;

		if (mdVerb != null) {
			int i = 0;
			while (i < getMDEntity().params().length && !includeParam) {
				if (getMDEntity().params()[i].equals(mdVerb)) {
					includeParam = true;
				}
				i++;
			}
		} else {
			includeParam = true;
		}

		return includeParam;
	}

	public boolean isChild() {
		return getParentEntityInfo() != null;
	}

	public MDEntityInfo getParentEntityInfo() {
		MDEntityInfo parent;
		if (!(parent = new MDEntityInfo(entityClass.getSuperclass())).isMDEntity()) {
			parent = null;
		}
		return parent;
	}

	public MDFieldInfo getParentIDFieldInfo() {
		return getParentEntityInfo().getIDFieldInfo();
	}

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

	public List<MDFieldInfo> getOneToManyFieldInfos(MDLoadPolicy loadPolicy) {
		List<MDFieldInfo> fieldInfos = new ArrayList<>();
		for (MDFieldInfo fieldInfo : getOneToManys()) {
			if (fieldInfo.getMDOneToMany().loadPolicy().equals(loadPolicy)) {
				fieldInfos.add(fieldInfo);
			}
		}
		return fieldInfos;
	}

	public MDFieldInfo getIDFieldInfo() {
		return getFieldInfoByAnnotation(MDId.class);
	}

	public MDFieldInfo getInheritLink() {
		return getFieldInfoByAnnotation(MDInheritLink.class);
	}

	public List<MDFieldInfo> getManyToOnes() {
		return getFieldsByAnnotation(MDManyToOne.class);
	}

	public List<MDFieldInfo> getOneToManys() {
		return getFieldsByAnnotation(MDOneToMany.class);
	}

	private <U extends Annotation> List<MDFieldInfo> getFieldsByAnnotation(Class<U> annotation) {
		List<MDFieldInfo> fieldInfos = new ArrayList<>();
		for (Field field : entityClass.getFields()) {
			MDFieldInfo fieldInfo = new MDFieldInfo(field);
			if (fieldInfo.getAnnotation(annotation) != null) {
				fieldInfos.add(fieldInfo);
			}
		}
		return fieldInfos;
	}

	private <U extends Annotation> MDFieldInfo getFieldInfoByAnnotation(Class<U> annotation) {
		MDFieldInfo mdFieldInfo = null;
		int i = 0;
		while (i < fieldInfos.size() && mdFieldInfo == null) {
			if (fieldInfos.get(i).getAnnotation(annotation) != null) {
				mdFieldInfo = fieldInfos.get(i);
			}
			i++;
		}
		return mdFieldInfo;
	}

	public <T> T mapEntity(ResultSet resultSet, MDRead read, boolean subRequest) throws MDException {
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
			if (!subRequest) {
				List<MDFieldInfo> manyToOneFieldInfos = getManyToOnes();
				MDEntityInfo entityInfo;
				for (MDFieldInfo fieldInfo : manyToOneFieldInfos) {
					if (fieldInfo.getMDManyToOne().loadPolicy().equals(HEAVY)) {
						entityInfo = new MDEntityInfo(fieldInfo.getMDManyToOne().target());
						object = entityInfo.mapEntity(resultSet, read, true);
						fieldInfo.getField().set(instance, object);
					}
				}
			}

			// One To Manys
			if (!subRequest) {
				List entities;
				MDOneToMany oneToMany;
				MDCondition subCondition;
				List<MDFieldInfo> oneToManyFieldInfos = getOneToManyFieldInfos(HEAVY);
				if (id != null) {
					for (MDFieldInfo oneToManyFieldInfo : oneToManyFieldInfos) {
						oneToMany = oneToManyFieldInfo.getMDOneToMany();
						subCondition = new MDCondition(oneToMany.targetFieldName(), EQUAL, id);
						entities = read.getEntities(oneToMany.target(), subCondition, true);
						oneToManyFieldInfo.getField().set(instance, entities);
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}

		return instance;
	}

	public MDFieldInfo getMDFieldInfoByFieldName(String fieldName) {
		MDFieldInfo mdFieldInfo = null;
		int i = 0;
		while (i < fieldInfos.size() && mdFieldInfo == null) {
			if (fieldInfos.get(i).getFieldName().equals(fieldName)) {
				mdFieldInfo = fieldInfos.get(i);
			}
			i++;
		}
		return mdFieldInfo;
	}

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
