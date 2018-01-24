package com.thomaskint.minidao.query.create;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.utils.MDEntityUtils;
import com.thomaskint.minidao.utils.MDFieldUtils;

import java.lang.reflect.Field;

import static com.thomaskint.minidao.enumeration.MDParam.INSERT;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDCreate {

	public static <T> boolean createEntity(MDConnectionConfig mdConnectionConfig, T entity) throws Exception {
		Class entityClass = entity.getClass();

		if (!MDEntityUtils.includeParam(entityClass, INSERT)) {
			throw new Exception("");
		}

		boolean created;

		StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");

		// Adding table
		queryBuilder.append(MDEntityUtils.getTableName(entityClass));

		// Adding fields
		MDCreateFieldList mdCreateFieldList = new MDCreateFieldList(entityClass);

		for (Field field : entityClass.getDeclaredFields()) {
			if (MDFieldUtils.isMDField(field) && MDFieldUtils.includeParam(field, INSERT)) {
				String fieldName = MDFieldUtils.getFieldName(field);
				Object value = field.get(entity);
				if (value != null) {
					mdCreateFieldList.addMDCreateFieldValue(new MDCreateFieldValue(fieldName, value));
				}
			}
		}

		queryBuilder.append(mdCreateFieldList.build());

		// Executing query
		created = MDConnection.executeUpdate(mdConnectionConfig, queryBuilder.toString()) > 0;

		return created;
	}
}
