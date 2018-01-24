package com.thomaskint.minidao.query.update;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.query.MDCondition;
import com.thomaskint.minidao.utils.MDEntityUtils;
import com.thomaskint.minidao.utils.MDFieldUtils;

import java.lang.reflect.Field;

import static com.thomaskint.minidao.enumeration.MDParam.UPDATE;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDUpdate {

	public static <T> boolean updateEntity(MDConnectionConfig mdConnectionConfig, T entity) throws Exception {
		Class entityClass = entity.getClass();

		if (!MDEntityUtils.includeParam(entityClass, UPDATE)) {
			throw new Exception("");
		}

		boolean updated = false;

		StringBuilder queryBuilder = new StringBuilder("UPDATE ");

		// Adding table
		queryBuilder.append(MDEntityUtils.getTableName(entityClass));

		// Adding fields
		MDUpdateFieldList mdUpdateFieldList = new MDUpdateFieldList(entityClass);

		for (Field field : entityClass.getDeclaredFields()) {
			if (MDFieldUtils.isMDField(field) && MDFieldUtils.includeParam(field, UPDATE)) {
				String fieldName = MDFieldUtils.getFieldName(field);
				Object value = field.get(entity);
				if (value != null) {
					mdUpdateFieldList.addMDUpdateFieldValue(new MDUpdateFieldValue(fieldName, value));
				}
			}
		}

		queryBuilder.append(mdUpdateFieldList.build());

		// Adding condition
		Field idField = MDFieldUtils.getIdField(entityClass);
		MDCondition mdCondition = new MDCondition(MDFieldUtils.getFieldName(idField), idField.get(entity));
		queryBuilder.append(mdCondition.build(entityClass));


		// Executing query
		updated = MDConnection.executeUpdate(mdConnectionConfig, queryBuilder.toString()) > 0;

		return updated;
	}
}
