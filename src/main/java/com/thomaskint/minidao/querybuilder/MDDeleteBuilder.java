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

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDSQLWord.FROM;
import static com.thomaskint.minidao.enumeration.MDSQLAction.DELETE;
import static com.thomaskint.minidao.utils.MDStringUtils.SPACE;

/**
 * @author Thomas Kint
 */
public class MDDeleteBuilder extends MDQueryBuilder<MDDeleteBuilder> {

	public MDDeleteBuilder() {
		super(DELETE);
	}

	public MDDeleteBuilder delete() {
		return this;
	}

	public <T> MDDeleteBuilder delete(T entity) throws MDException {
		delete(entity.getClass());
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
		return this;
	}

	public <T> MDDeleteBuilder delete(Class<T> entityClass) {
		baseEntityInfo = new MDEntityInfo(entityClass);
		return this;
	}

	public <T> MDDeleteBuilder from(Class<T> entityClass) {
		baseEntityInfo = new MDEntityInfo(entityClass);
		return this;
	}


	@Override
	protected void buildVerbPart() {
		queryBuilder.append(sqlAction);
	}

	@Override
	protected void buildTablePart() {
		queryBuilder.append(SPACE);
		queryBuilder.append(FROM);
		queryBuilder.append(SPACE);
		queryBuilder.append(baseEntityInfo.getTableName());
	}
}
