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
import com.thomaskint.minidao.querybuilder.MDCondition;
import com.thomaskint.minidao.querybuilder.MDDeleteBuilder;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDSQLAction.DELETE;

/**
 * Class exposing methods to delete entities from database
 *
 * @author Thomas Kint
 */
public class MDDelete extends MDCRUDBase {

	public MDDelete(MDConnectionConfig mdConnectionConfig) {
		super(mdConnectionConfig);
	}

	/**
	 * Delete entity in database
	 *
	 * @param entity T
	 * @param <T>    T
	 * @return boolean deleted
	 * <p>true: the entity has been deleted in database</p>
	 * <p>false: the entity has not been deleted in database</p>
	 * @throws MDException Deletion encountered an exception
	 */
	public <T> boolean deleteEntity(T entity) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entity.getClass());

		if (!entityInfo.isMDEntity()) {

		}
		if (!entityInfo.isSQLActionAllowed(DELETE)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), DELETE);
		}

		MDDeleteBuilder deleteBuilder = new MDDeleteBuilder();
		deleteBuilder.delete(entity);

		String query = deleteBuilder.build();

		return MDConnection.executeUpdate(connectionConfig, query) > 0;
	}

	/**
	 * Delete entity in database based on its primary key
	 *
	 * @param entityClass {@link Class}
	 * @param id          {@link Object}
	 * @param <T>         T
	 * @return boolean deleted
	 * <p>true: the entity has been deleted in database</p>
	 * <p>false: the entity has not been deleted in database</p>
	 * @throws MDException Deletion encountered an exception
	 */
	public <T> boolean deleteEntityById(Class<T> entityClass, Object id) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);

		String fieldName = entityInfo.getIDFieldInfo().getFieldName();

		MDCondition condition = new MDCondition(fieldName, EQUAL, id);

		return deleteEntities(entityInfo, condition);
	}

	/**
	 * Delete entities in database based on their class
	 *
	 * @param entityClass {@link Class}
	 * @param <T>         T
	 * @return boolean deleted
	 * <p>true: the entities have been deleted in database</p>
	 * <p>false: the entities have not been deleted in database</p>
	 * @throws MDException Deletion encountered an exception
	 */
	public <T> boolean deleteEntities(Class<T> entityClass) throws MDException {
		return deleteEntities(entityClass, null);
	}

	/**
	 * Delete entities in database based on their class and condition
	 *
	 * @param entityClass {@link Class}
	 * @param condition   {@link MDCondition}
	 * @param <T>         T
	 * @return boolean deleted
	 * <p>true: the entities have been deleted in database</p>
	 * <p>false: the entities have not been deleted in database</p>
	 * @throws MDException Deletion encountered an exception
	 */
	public <T> boolean deleteEntities(Class<T> entityClass, MDCondition condition) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return deleteEntities(entityInfo, condition);
	}

	/**
	 * Delete entities in database based on entityInfo and condition
	 *
	 * @param entityInfo {@link MDEntityInfo}
	 * @param condition  {@link MDCondition}
	 * @return boolean deleted
	 * <p>true: the entities have been deleted in database</p>
	 * <p>false: the entities have not been deleted in database</p>
	 * @throws MDException Deletion encountered an exception
	 */
	private boolean deleteEntities(MDEntityInfo entityInfo, MDCondition condition) throws MDException {
		if (!entityInfo.isMDEntity()) {
			throw new MDNotAnMDEntityException(entityInfo.getEntityClass());
		}
		if (!entityInfo.isSQLActionAllowed(DELETE)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), DELETE);
		}

		MDDeleteBuilder deleteBuilder = new MDDeleteBuilder();
		deleteBuilder.delete(entityInfo.getEntityClass());

		if (condition != null) {
			deleteBuilder.where(condition);
		}

		String query = deleteBuilder.build();

		return MDConnection.executeUpdate(connectionConfig, query) > 0;
	}
}
