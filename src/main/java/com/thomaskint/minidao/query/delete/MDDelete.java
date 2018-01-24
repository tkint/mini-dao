package com.thomaskint.minidao.query.delete;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.query.MDCondition;
import com.thomaskint.minidao.utils.MDEntityUtils;
import com.thomaskint.minidao.utils.MDFieldUtils;

import java.lang.reflect.Field;

import static com.thomaskint.minidao.enumeration.MDParam.DELETE;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDDelete {

	public static <T> boolean deleteEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, MDCondition mdCondition) throws Exception {
		if (!MDEntityUtils.includeParam(entityClass, DELETE)) {
			throw new Exception("");
		}

		boolean deleted;

		StringBuilder queryBuilder = new StringBuilder("DELETE FROM ");

		// Adding table
		queryBuilder.append(MDEntityUtils.getTableName(entityClass));

		// Adding conditions
		if (mdCondition != null) {
			queryBuilder.append(mdCondition.build(entityClass));
		}

		deleted = MDConnection.executeUpdate(mdConnectionConfig, queryBuilder.toString()) > 0;

		return deleted;
	}

	public static <T> boolean deleteEntity(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, int id) throws Exception {
		MDCondition mdCondition = new MDCondition(MDFieldUtils.getIdFieldName(entityClass), id);
		return deleteEntities(mdConnectionConfig, entityClass, mdCondition);
	}

	public static <T> boolean deleteEntity(MDConnectionConfig mdConnectionConfig, T entity) throws Exception {
		Field idField = MDFieldUtils.getIdField(entity.getClass());
		return deleteEntity(mdConnectionConfig, entity.getClass(), idField.getInt(entity));
	}
}
