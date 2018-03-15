package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.exception.MDParamNotIncludedInClassException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.querybuilder.MDInsertBuilder;

import static com.thomaskint.minidao.enumeration.MDVerb.INSERT;

/**
 * @author Thomas Kint
 */
public class MDCreate extends MDCRUDBase {

	public MDCreate(MDConnectionConfig connectionConfig) {
		super(connectionConfig);
	}

	public <T> boolean createEntity(T entity) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entity.getClass());

		if (!entityInfo.isMDEntity()) {

		}
		if (!entityInfo.includeParam(INSERT)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), INSERT);
		}

		MDInsertBuilder insertBuilder = new MDInsertBuilder();
		insertBuilder.insert(entity);

		String query = insertBuilder.build();

		return MDConnection.executeUpdate(mdConnectionConfig, query) > 0;
	}
}
