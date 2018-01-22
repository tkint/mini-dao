package com.thomaskint.minidao.query.read;

import com.thomaskint.minidao.MDConnection;
import com.thomaskint.minidao.query.MDCondition;
import com.thomaskint.minidao.utils.MDEntityUtils;
import com.thomaskint.minidao.utils.MDFieldUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.thomaskint.minidao.enumeration.MDParam.SELECT;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDRead {

	public static <T> List<T> getEntities(Class<T> entityClass, MDCondition mdCondition) throws Exception {
		if (!MDEntityUtils.includeParam(entityClass, SELECT)) {
			throw new Exception("");
		}

		List<T> entities = new ArrayList<>();

		try {
			StringBuilder queryBuilder = new StringBuilder("SELECT ");

			// Adding fields
			List<Field> fields = MDFieldUtils.getFields(entityClass, SELECT);
			for (int i = 0; i < fields.size(); i++) {
				queryBuilder.append(MDFieldUtils.getFieldName(fields.get(i)));
				if (i < fields.size() - 1) {
					queryBuilder.append(", ");
				}
			}

			// Adding table
			queryBuilder.append(" FROM ");
			queryBuilder.append(MDEntityUtils.getTableName(entityClass));

			// Adding conditions
			if (mdCondition != null) {
				queryBuilder.append(" WHERE ");
				queryBuilder.append(mdCondition.build());
			}

			// Executing query
			ResultSet resultSet = MDConnection.getInstance().executeQuery(queryBuilder.toString());

			while (resultSet.next()) {
				entities.add(MDEntityUtils.mapEntity(entityClass, resultSet));
			}

		} catch (Exception ex) {
			throw ex;
		}

		return entities;
	}

	public static <T> T getEntityById(Class<T> entityClass, int id) throws Exception {
		MDCondition mdCondition = new MDCondition(MDFieldUtils.getIdFieldName(entityClass), id);
		List<T> entities = getEntities(entityClass, mdCondition);

		return entities.get(0);
	}
}
