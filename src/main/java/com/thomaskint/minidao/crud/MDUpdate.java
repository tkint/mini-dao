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

	public MDUpdate(MDConnectionConfig mdConnectionConfig) {
		super(mdConnectionConfig);
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

		if (!entityInfo.isMDEntity()) {
			throw new MDNotAnMDEntityException(entityInfo.getEntityClass());
		}
		if (!entityInfo.isSQLActionAllowed(UPDATE)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), UPDATE);
		}

		MDUpdateBuilder updateBuilder = new MDUpdateBuilder();
		updateBuilder.update(entity);

		String query = updateBuilder.build();

		// Executing crud
		return MDConnection.executeUpdate(mdConnectionConfig, query) > 0;
	}
}
