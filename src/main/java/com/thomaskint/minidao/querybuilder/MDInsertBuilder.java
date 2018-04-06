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

import static com.thomaskint.minidao.enumeration.MDSQLAction.INSERT;
import static com.thomaskint.minidao.enumeration.MDSQLWord.INTO;
import static com.thomaskint.minidao.enumeration.MDSQLWord.VALUES;
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
		queryBuilder.append(sqlAction);
	}

	@Override
	protected void buildTablePart() {
		queryBuilder.append(SPACE);
		queryBuilder.append(INTO);
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
					&& !(fieldInfo.equals(baseEntityInfo.getIDFieldInfo()) && !baseEntityInfo.getIDFieldInfo().isSQLActionAllowed(sqlAction))
					&& isFieldInfoParamValid(fieldInfo)) {
				if (keyValue.getValue() != null) {
					if (addedFields > 0) {
						keysBuilder.append(COMMA);
						keysBuilder.append(SPACE);
						valuesBuilder.append(COMMA);
						valuesBuilder.append(SPACE);
					}
					keysBuilder.append(keyValue.getKey());
					if (!(keyValue.getValue() instanceof Number)) {
						valuesBuilder.append(QUOTE);
					}
					valuesBuilder.append(keyValue.getValue());
					if (!(keyValue.getValue() instanceof Number)) {
						valuesBuilder.append(QUOTE);
					}
					addedFields++;
				}
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
		return queryBuilder.toString();
	}
}
