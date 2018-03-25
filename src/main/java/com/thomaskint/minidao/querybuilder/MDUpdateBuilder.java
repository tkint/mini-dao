/*
 * MIT License
 *
 * Copyright (c) 2017 Thomas Kint
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.model.MDFieldInfo;

import java.util.HashMap;
import java.util.Map;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDSQLWord.VALUES;
import static com.thomaskint.minidao.enumeration.MDSQLAction.UPDATE;
import static com.thomaskint.minidao.utils.MDStringUtils.*;

/**
 * @author Thomas Kint
 */
public class MDUpdateBuilder extends MDQueryBuilder<MDUpdateBuilder> {

	private Map<String, Object> newValues = new HashMap<>();

	public MDUpdateBuilder() {
		super(UPDATE);
	}

	public <T> MDUpdateBuilder update(Class<T> entityClass) {
		baseEntityInfo = new MDEntityInfo(entityClass);
		return this;
	}

	public <T> MDUpdateBuilder update(T entity) throws MDException {
		baseEntityInfo = new MDEntityInfo(entity.getClass());
		if (baseEntityInfo.getIDFieldInfo() != null) {
			String idFieldName = baseEntityInfo.getIDFieldInfo().getFieldName();
			if (baseEntityInfo.getIDFieldInfo().getField() != null) {
				try {
					Object value = baseEntityInfo.getIDFieldInfo().getField().get(entity);
					where(idFieldName, EQUAL, value);
				} catch (IllegalAccessException e) {
					throw new MDException("Can't access this field " + idFieldName);
				}
			}
		}
		String key;
		Object value;
		for (MDFieldInfo fieldInfo : baseEntityInfo.getMDFieldInfos()) {
			if (!fieldInfo.equals(baseEntityInfo.getIDFieldInfo())) {
				key = fieldInfo.getFieldName();
				try {
					value = fieldInfo.getField().get(entity);
					set(key, value);
				} catch (IllegalAccessException e) {
					throw new MDException("Can't access this field " + key);
				}
			}
		}
		return this;
	}

	public MDUpdateBuilder set(String key, Object value) {
		newValues.put(key, value);
		return this;
	}

	@Override
	protected void buildVerbPart() {
		queryBuilder.append(sqlAction);
	}

	@Override
	protected void buildTablePart() {
		queryBuilder.append(SPACE);
		queryBuilder.append(baseEntityInfo.getTableName());
	}

	private void buildKeysValuesPart() {
		StringBuilder keysBuilder = new StringBuilder();
		StringBuilder valuesBuilder = new StringBuilder();

		MDFieldInfo fieldInfo;
		int addedFields = 0;
		for (Map.Entry<String, Object> keyValue : newValues.entrySet()) {
			fieldInfo = baseEntityInfo.getMDFieldInfoByFieldName(keyValue.getKey());
			if (fieldInfo != null
					&& !fieldInfo.equals(baseEntityInfo.getIDFieldInfo())
					&& isFieldInfoParamValid(fieldInfo)) {
				if (addedFields > 0) {
					keysBuilder.append(COMMA);
					keysBuilder.append(SPACE);
					valuesBuilder.append(COMMA);
					valuesBuilder.append(SPACE);
				}
				keysBuilder.append(QUOTE);
				keysBuilder.append(keyValue.getKey());
				keysBuilder.append(QUOTE);
				valuesBuilder.append(QUOTE);
				valuesBuilder.append(keyValue.getValue());
				valuesBuilder.append(QUOTE);
				addedFields++;
			}
		}

		if (addedFields > 0) {
			queryBuilder.append(SPACE);
			queryBuilder.append(LEFT_PARENTHESIS);
			queryBuilder.append(keysBuilder);
			queryBuilder.append(RIGHT_PARENTHESIS);
			queryBuilder.append(SPACE);
			queryBuilder.append(VALUES);
			queryBuilder.append(SPACE);
			queryBuilder.append(LEFT_PARENTHESIS);
			queryBuilder.append(valuesBuilder);
			queryBuilder.append(RIGHT_PARENTHESIS);
		}
	}

	@Override
	public String build() throws MDException {
		if (baseEntityInfo == null) {
			throw new MDException("No target!");
		}
		buildVerbPart();
		buildTablePart();
		buildKeysValuesPart();
		buildWherePart();
		return queryBuilder.toString();
	}
}
