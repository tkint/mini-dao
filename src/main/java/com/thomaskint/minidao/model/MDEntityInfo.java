package com.thomaskint.minidao.model;

import com.thomaskint.minidao.annotation.MDEntity;
import com.thomaskint.minidao.annotation.MDId;
import com.thomaskint.minidao.annotation.MDInheritLink;
import com.thomaskint.minidao.annotation.MDManyToOne;
import com.thomaskint.minidao.enumeration.MDLoadPolicy;
import com.thomaskint.minidao.enumeration.MDVerb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY;

/**
 * @author Thomas Kint
 */
public class MDEntityInfo {

	private Class entityClass;

	private Annotation[] annotations;

	private List<MDFieldInfo> fieldInfos = new ArrayList<>();

	public <T> MDEntityInfo(Class<T> entityClass) {
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
		return getMDEntity().name();
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
				entityInfo = new MDEntityInfo(fieldInfo.getMDManyToOne().entity());
				entityInfos.add(entityInfo);
			}
		}
		return entityInfos;
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

	public <T> T mapEntity(ResultSet resultSet) {
		T instance = null;

		try {
			instance = (T) entityClass.newInstance();

			Object object;
			MDFieldInfo idFieldInfo = getIDFieldInfo();
			if (idFieldInfo != null) {
				object = resultSet.getObject(idFieldInfo.getFieldName());
				idFieldInfo.getField().set(instance, object);
			}

			for (MDFieldInfo fieldInfo : fieldInfos) {
				object = resultSet.getObject(fieldInfo.getFieldName());
				fieldInfo.getField().set(instance, object);
			}

			Object foreignObject;
			List<MDFieldInfo> manyToOneFieldInfos = getManyToOnes();
			MDEntityInfo entityInfo;
			for (MDFieldInfo fieldInfo : manyToOneFieldInfos) {
				if (fieldInfo.getMDManyToOne().loadPolicy().equals(HEAVY)) {
					entityInfo = new MDEntityInfo(fieldInfo.getMDManyToOne().entity());
					foreignObject = entityInfo.mapEntity(resultSet);
					fieldInfo.getField().set(instance, foreignObject);
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
			if (fieldInfos.get(i).getMDManyToOne().entity().equals(entityClass)) {
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
