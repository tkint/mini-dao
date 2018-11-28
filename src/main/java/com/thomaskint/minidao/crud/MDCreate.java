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

package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.exception.MDNotAnMDEntityException;
import com.thomaskint.minidao.exception.MDParamNotIncludedInClassException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.model.MDPair;
import com.thomaskint.minidao.querybuilder.MDInsertBuilder;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.thomaskint.minidao.enumeration.MDSQLAction.INSERT;

/**
 * Class exposing methods to create entities into database
 *
 * @author Thomas Kint
 */
public class MDCreate extends MDCRUDBase {

	private final String dateFormat;

	public MDCreate(MDConnectionConfig connectionConfig, String dateFormat) {
		super(connectionConfig);
		this.dateFormat = dateFormat;
	}

	/**
	 * Create entity into database
	 *
	 * @param entity T
	 * @param <T>    T
	 * @return boolean
	 * @throws MDException when can't create entity
	 */
	public <T> boolean createEntity(T entity) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entity.getClass());
		try {
			if (!entityInfo.isMDEntity()) {
				throw new MDNotAnMDEntityException(entityInfo.getEntityClass());
			}
			if (!entityInfo.isSQLActionAllowed(INSERT)) {
				throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), INSERT);
			}

			MDInsertBuilder insertBuilder = new MDInsertBuilder(this.dateFormat);
			insertBuilder.insert(entity);

			String query = insertBuilder.build();

			String idFieldName = entityInfo.getIDFieldInfo().getFieldName();

			MDPair<Statement, Integer> result = MDConnection.executeUpdate(connectionConfig, query, idFieldName);

			ResultSet generatedKeys = result.getKey().getGeneratedKeys();
			setPrimaryKey(entity, entityInfo, generatedKeys);

			return result.getValue() > 0;
		} catch (Exception e) {
			throw new MDException(e);
		} finally {
			MDConnection.close();
		}
	}

	private <T> void setPrimaryKey(T entity, MDEntityInfo entityInfo, ResultSet generatedKeys) throws SQLException, IllegalAccessException {
		if (generatedKeys != null && generatedKeys.next()) {
			Field idField = entityInfo.getIDFieldInfo().getField();
			Object idValue = generatedKeys.getObject(1);
			if (idValue != null) {
				if (idField.getType().equals(idValue.getClass())) {
					idField.set(entity, idValue);
				} else if (idField.getType().equals(Integer.class)) {
					if (idValue instanceof BigInteger) {
						idField.set(entity, ((BigInteger) idValue).intValue());
					}
				}
			}
		}
	}
}
