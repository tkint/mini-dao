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
import com.thomaskint.minidao.querybuilder.MDInsertBuilder;

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

			return MDConnection.executeUpdate(connectionConfig, query) > 0;
		} finally {
			MDConnection.close();
		}
	}
}
