package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.model.MDFieldInfo;

import java.util.HashMap;
import java.util.Map;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDSQLWord.INTO;
import static com.thomaskint.minidao.enumeration.MDSQLWord.SET;
import static com.thomaskint.minidao.enumeration.MDVerb.INSERT;
import static com.thomaskint.minidao.utils.MDStringUtils.*;

/**
 * @author Thomas Kint
 */
public class MDInsertBuilder extends MDQueryBuilder<MDInsertBuilder> {

	private Map<String, Object> newValues = new HashMap<>();

	public MDInsertBuilder() {
		super(INSERT);
	}

	public MDInsertBuilder insert() {
		return this;
	}

	public <T> MDInsertBuilder insert(T entity) throws MDException {
		baseEntityInfo = new MDEntityInfo(entity.getClass());
		String key;
		Object value;
		for (MDFieldInfo fieldInfo : baseEntityInfo.getMDFieldInfos()) {
			key = fieldInfo.getFieldName();
			try {
				value = fieldInfo.getField().get(entity);
				set(key, value);
			} catch (IllegalAccessException e) {
				throw new MDException("Can't access this field " + key);
			}
		}
		return this;
	}

	public <T> MDInsertBuilder into(Class<T> entityClass) {
		baseEntityInfo = new MDEntityInfo(entityClass);
		return this;
	}

	public MDInsertBuilder set(String key, Object value) {
		newValues.put(key, value);
		return this;
	}

	@Override
	protected void buildVerbPart() {
		queryBuilder.append(verb);
	}

	@Override
	protected void buildTablePart() {
		queryBuilder.append(SPACE);
		queryBuilder.append(INTO);
		queryBuilder.append(SPACE);
		queryBuilder.append(baseEntityInfo.getTableName());
	}

	private void buildSetPart() {
		StringBuilder setBuilder = new StringBuilder();

		MDFieldInfo fieldInfo;
		int addedFields = 0;
		for (Map.Entry<String, Object> keyValue : newValues.entrySet()) {
			fieldInfo = baseEntityInfo.getMDFieldInfoByFieldName(keyValue.getKey());
			if (fieldInfo != null && isFieldInfoParamValid(fieldInfo)) {
				if (addedFields > 0) {
					setBuilder.append(COMMA);
					setBuilder.append(SPACE);
				}
				setBuilder.append(fieldInfo.getFieldName());
				setBuilder.append(SPACE);
				setBuilder.append(EQUAL);
				setBuilder.append(SPACE);
				setBuilder.append(QUOTE);
				setBuilder.append(keyValue.getValue());
				setBuilder.append(QUOTE);
				addedFields++;
			}
		}

		if (addedFields > 0) {
			queryBuilder.append(SPACE);
			queryBuilder.append(SET);
			queryBuilder.append(SPACE);
			queryBuilder.append(setBuilder);
		}
	}

	@Override
	public String build() throws MDException {
		if (baseEntityInfo == null) {
			throw new MDException("No target!");
		}
		buildVerbPart();
		buildTablePart();
		buildSetPart();
		return queryBuilder.toString();
	}
}
