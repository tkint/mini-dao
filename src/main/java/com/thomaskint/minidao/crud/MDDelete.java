package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.exception.MDParamNotIncludedInClassException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.querybuilder.MDCondition;
import com.thomaskint.minidao.querybuilder.MDDeleteBuilder;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDVerb.DELETE;

/**
 * @author Thomas Kint
 */
public class MDDelete extends MDCRUDBase {

	public MDDelete(MDConnectionConfig mdConnectionConfig) {
		super(mdConnectionConfig);
	}

	public <T> boolean deleteEntity(T entity) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entity.getClass());

		if (!entityInfo.isMDEntity()) {

		}
		if (!entityInfo.includeParam(DELETE)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), DELETE);
		}

		MDDeleteBuilder deleteBuilder = new MDDeleteBuilder();
		deleteBuilder.delete(entity);

		String query = deleteBuilder.build();

		return MDConnection.executeUpdate(mdConnectionConfig, query) > 0;
	}

	public <T> boolean deleteEntityById(Class<T> entityClass, Object id) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);

		String fieldName = entityInfo.getIDFieldInfo().getFieldName();

		MDCondition condition = new MDCondition(fieldName, EQUAL, id);

		return deleteEntities(entityInfo, condition);
	}

	public <T> boolean deleteEntities(Class<T> entityClass) throws MDException {
		return deleteEntities(entityClass, null);
	}

	public <T> boolean deleteEntities(Class<T> entityClass, MDCondition condition) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return deleteEntities(entityInfo, condition);
	}

	private boolean deleteEntities(MDEntityInfo entityInfo, MDCondition condition) throws MDException {
		if (!entityInfo.isMDEntity()) {

		}
		if (!entityInfo.includeParam(DELETE)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), DELETE);
		}

		MDDeleteBuilder deleteBuilder = new MDDeleteBuilder();
		deleteBuilder.delete(entityInfo.getEntityClass());

		if (condition != null) {
			deleteBuilder.where(condition);
		}

		String query = deleteBuilder.build();

		return MDConnection.executeUpdate(mdConnectionConfig, query) > 0;
	}
}
