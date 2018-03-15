package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.exception.MDParamNotIncludedInClassException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.querybuilder.MDUpdateBuilder;

import static com.thomaskint.minidao.enumeration.MDVerb.UPDATE;

/**
 * @author Thomas Kint
 */
public class MDUpdate extends MDCRUDBase {

	public MDUpdate(MDConnectionConfig mdConnectionConfig) {
		super(mdConnectionConfig);
	}

	public <T> boolean updateEntity(T entity) throws MDException {
		MDEntityInfo mdEntityInfo = new MDEntityInfo(entity.getClass());

		if (!mdEntityInfo.isMDEntity()) {

		}
		if (!mdEntityInfo.includeParam(UPDATE)) {
			throw new MDParamNotIncludedInClassException(mdEntityInfo.getEntityClass(), UPDATE);
		}

		MDUpdateBuilder updateBuilder = new MDUpdateBuilder();
		updateBuilder.update(entity);

		String query = updateBuilder.build();

		// Executing crud
		return MDConnection.executeUpdate(mdConnectionConfig, query) > 0;
	}
}
