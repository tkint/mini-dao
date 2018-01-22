package com.thomaskint.minidao.query.create;

import com.thomaskint.minidao.MDConnection;
import com.thomaskint.minidao.utils.MDEntityUtils;

import static com.thomaskint.minidao.enumeration.MDParam.INSERT;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDCreate {

	public static <T> boolean createEntity(Class<T> entityClass, MDCreateFieldList mdCreateFieldList) throws Exception {
		if (!MDEntityUtils.includeParam(entityClass, INSERT)) {
			throw new Exception("");
		}

		boolean created;

		StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");

		// Adding table
		queryBuilder.append(MDEntityUtils.getTableName(entityClass));

		// Adding fields
		queryBuilder.append(mdCreateFieldList.build());

		// Executing query
		created = MDConnection.getInstance().executeUpdate(queryBuilder.toString()) > 0;

		return created;
	}

}
