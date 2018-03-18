package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.exception.MDParamNotIncludedInClassException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.querybuilder.MDCondition;
import com.thomaskint.minidao.querybuilder.MDSelectBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY;
import static com.thomaskint.minidao.enumeration.MDVerb.SELECT;

/**
 * @author Thomas Kint
 * Class exposing methods to read entities from database
 */
public class MDRead extends MDCRUDBase {

	public MDRead(MDConnectionConfig mdConnectionConfig) {
		super(mdConnectionConfig);
	}

	public <T> T getEntityById(Class<T> entityClass, Object id) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);

		String fieldName = entityInfo.getIDFieldInfo().getFieldName();

		MDCondition condition = new MDCondition(fieldName, EQUAL, id);

		List<T> entities = getEntities(entityInfo, condition);

		T entity = null;

		if (entities.size() > 0) {
			entity = entities.get(0);
		}

		return entity;
	}

	public <T> List<T> getEntities(Class<T> entityClass) throws MDException {
		return getEntities(entityClass, null);
	}

	public <T> List<T> getEntities(Class<T> entityClass, MDCondition condition) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return getEntities(entityInfo, condition);
	}

	public <T> List<T> getEntities(Class<T> entityClass, MDCondition condition, boolean subRequest) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return getEntities(entityInfo, condition, subRequest);
	}

	private <T> List<T> getEntities(MDEntityInfo entityInfo) throws MDException {
		return getEntities(entityInfo, null);
	}

	private <T> List<T> getEntities(MDEntityInfo entityInfo, MDCondition condition) throws MDException {
		return getEntities(entityInfo, condition, false);
	}

	private <T> List<T> getEntities(MDEntityInfo entityInfo, MDCondition condition, boolean subRequest) throws MDException {
		if (!entityInfo.isMDEntity()) {

		}
		if (!entityInfo.includeParam(SELECT)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), SELECT);
		}

		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(entityInfo.getEntityClass());

		// Condition
		if (condition != null) {
			selectBuilder.where(condition);
		}

		// InheritLink
		MDEntityInfo parentEntityInfo = entityInfo.getParentEntityInfo();
		if (parentEntityInfo != null) {
			selectBuilder.innerJoin(parentEntityInfo.getEntityClass());
		}

		// Many To Ones entities
		if (!subRequest) {
			List<MDEntityInfo> manyToOneEntityInfos = entityInfo.getManyToOneEntityInfos(HEAVY);
			if (!manyToOneEntityInfos.isEmpty()) {
				for (MDEntityInfo manyToOneEntityInfo : manyToOneEntityInfos) {
					selectBuilder.innerJoin(manyToOneEntityInfo.getEntityClass());
				}
			}
		}

		String query = selectBuilder.build();

		System.out.println(query);

		ResultSet resultSet;
		resultSet = MDConnection.executeQuery(mdConnectionConfig, query);

		List<T> entities = new ArrayList<>();
		try {
			while (resultSet.next()) {
				entities.add(entityInfo.mapEntity(resultSet, this, subRequest));
			}
		} catch (SQLException e) {
			throw new MDException(e);
		}

		return entities;
	}
}
