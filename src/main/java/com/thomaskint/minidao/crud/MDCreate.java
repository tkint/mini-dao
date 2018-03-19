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

	public MDCreate(MDConnectionConfig connectionConfig) {
		super(connectionConfig);
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

		if (!entityInfo.isMDEntity()) {
			throw new MDNotAnMDEntityException(entityInfo.getEntityClass());
		}
		if (!entityInfo.isSQLActionAllowed(INSERT)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), INSERT);
		}

		MDInsertBuilder insertBuilder = new MDInsertBuilder();
		insertBuilder.insert(entity);

		String query = insertBuilder.build();

		return MDConnection.executeUpdate(mdConnectionConfig, query) > 0;
	}
}
