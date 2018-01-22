package com.thomaskint.minidao.query.update;

import com.thomaskint.minidao.MDConnection;
import com.thomaskint.minidao.model.User;
import com.thomaskint.minidao.query.MDCondition;
import com.thomaskint.minidao.utils.MDEntityUtils;
import com.thomaskint.minidao.utils.MDFieldUtils;

import static com.thomaskint.minidao.enumeration.MDParam.UPDATE;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDUpdate {

	public static <T> boolean updateEntities(Class<T> entityClass, MDCondition mdCondition, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		if (!MDEntityUtils.includeParam(entityClass, UPDATE)) {
			throw new Exception("");
		}

		boolean updated;

		StringBuilder queryBuilder = new StringBuilder("UPDATE ");

		// Adding table
		queryBuilder.append(MDEntityUtils.getTableName(entityClass));

		// Adding fields
		queryBuilder.append(mdUpdateFieldList.build());

		// Adding conditions
		if (mdCondition != null) {
			queryBuilder.append(" WHERE ");
			queryBuilder.append(mdCondition.build());
		}

		// Executing query
		updated = MDConnection.getInstance().executeUpdate(queryBuilder.toString()) > 0;

		return updated;
	}

	public static <T> boolean updateEntity(Class<T> entityClass, int id, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		MDCondition mdCondition = new MDCondition(MDFieldUtils.getIdFieldName(entityClass), id);
		return updateEntities(User.class, mdCondition, mdUpdateFieldList);
	}
}
