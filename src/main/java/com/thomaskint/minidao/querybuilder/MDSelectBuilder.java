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

import static com.thomaskint.minidao.enumeration.MDSQLAction.SELECT;
import static com.thomaskint.minidao.enumeration.MDSQLWord.*;
import static com.thomaskint.minidao.utils.MDStringUtils.*;

/**
 * @author Thomas Kint
 */
public class MDSelectBuilder extends MDQueryBuilder<MDSelectBuilder> {

	private boolean count = false;

	private String[] fieldsName = new String[0];

	public MDSelectBuilder() {
		super(SELECT);
	}

	public MDSelectBuilder select(String... fieldNames) {
		this.fieldsName = fieldNames;
		return this;
	}

	public <T> MDSelectBuilder from(Class<T> entityClass) {
		baseEntityInfo = new MDEntityInfo(entityClass);
		return this;
	}

	public <T> MDSelectBuilder count(Class<T> entityClass) {
		from(entityClass);
		count = true;
		return this;
	}

	@Override
	protected void buildVerbPart() {
		if (!count) {
			int addedFields = 0;
			StringBuilder tempBuilder = new StringBuilder();

			MDEntityInfo entityInfo;
			MDFieldInfo fieldInfo;
			for (String fieldName : fieldsName) {
				entityInfo = getEntityInfoByFieldName(fieldName);
				if (entityInfo != null) {
					fieldInfo = entityInfo.getMDFieldInfoByFieldName(fieldName);
					if (isFieldInfoParamValid(fieldInfo)) {
						if (addedFields > 0) {
							tempBuilder.append(COMMA);
							tempBuilder.append(SPACE);
						}
						tempBuilder.append(fieldInfo.getFieldFullName());
						addedFields++;
					}
				}
			}

			queryBuilder.append(sqlAction);
			queryBuilder.append(SPACE);
			if (addedFields == 0) {
				queryBuilder.append(ALL);
			} else {
				queryBuilder.append(tempBuilder);
			}
		} else {
			queryBuilder.append(sqlAction);
			queryBuilder.append(SPACE);
			queryBuilder.append(COUNT);
			queryBuilder.append(LEFT_PARENTHESIS);
			queryBuilder.append(ALL);
			queryBuilder.append(RIGHT_PARENTHESIS);
			queryBuilder.append(SPACE);
			queryBuilder.append(AS);
			queryBuilder.append(SPACE);
			queryBuilder.append(TOTAL);
		}
	}

	@Override
	protected void buildTablePart() {
		queryBuilder.append(SPACE);
		queryBuilder.append(FROM);
		queryBuilder.append(SPACE);
		queryBuilder.append(baseEntityInfo.getTableName());
	}
}
