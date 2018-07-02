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
import com.thomaskint.minidao.querybuilder.MDUpdateBuilder;

import static com.thomaskint.minidao.enumeration.MDSQLAction.UPDATE;

/**
 * Class exposing methods to update entities into database
 *
 * @author Thomas Kint
 */
public class MDUpdate extends MDCRUDBase {

	private final String dateFormat;

	public MDUpdate(MDConnectionConfig connectionConfig, String dateFormat) {
		super(connectionConfig);
		this.dateFormat = dateFormat;
	}

	/**
	 * Update entity into database
	 *
	 * @param entity T
	 * @param <T>    T
	 * @return boolean updated
	 * <p>true: the entity has been updated in database</p>
	 * <p>false: the entity has not been updated in database</p>
	 * @throws MDException
	 */
	public <T> boolean updateEntity(T entity) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entity.getClass());
		try {
			if (!entityInfo.isMDEntity()) {
				throw new MDNotAnMDEntityException(entityInfo.getEntityClass());
			}
			if (!entityInfo.isSQLActionAllowed(UPDATE)) {
				throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), UPDATE);
			}

			MDUpdateBuilder updateBuilder = new MDUpdateBuilder(this.dateFormat);
			updateBuilder.update(entity);

			String query = updateBuilder.build();

			// Executing crud
			return MDConnection.executeUpdate(connectionConfig, query) > 0;
		} finally {
			MDConnection.close();
		}
	}
}
