package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.exception.MDNotAnMDEntityException;
import com.thomaskint.minidao.exception.MDParamNotIncludedInClassException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.querybuilder.MDCondition;
import com.thomaskint.minidao.querybuilder.MDSelectBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY;
import static com.thomaskint.minidao.enumeration.MDSQLAction.SELECT;

/**
 * Class exposing methods to read entities from database
 *
 * @author Thomas Kint
 */
public class MDRead extends MDCRUDBase {

	public MDRead(MDConnectionConfig mdConnectionConfig) {
		super(mdConnectionConfig);
	}

	/**
	 * Retrieve entity based on its class and primary key
	 *
	 * @param entityClass {@link Class}
	 * @param id          {@link Object}
	 * @param <T>         T
	 * @return Entity
	 * @throws MDException when can't retrieve data
	 */
	public <T> T getEntityById(Class<T> entityClass, Object id) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);

		String fieldName = entityInfo.getIDFieldInfo().getFieldName();

		MDCondition condition = new MDCondition(fieldName, EQUAL, id);

		List<T> entities = getEntities(entityInfo, condition);

		T entity = null;

		if (entities.size() > 0) {
			entity = entities.get(0);
		}

		return entity;
	}

	/**
	 * Retrieve entities based on class
	 *
	 * @param entityClass {@link Class}
	 * @param <T>         T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	public <T> List<T> getEntities(Class<T> entityClass) throws MDException {
		return getEntities(entityClass, null);
	}

	/**
	 * Retrieve entities based on class and condition
	 *
	 * @param entityClass {@link Class}
	 * @param condition   {@link MDCondition}
	 * @param <T>         T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	public <T> List<T> getEntities(Class<T> entityClass, MDCondition condition) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return getEntities(entityInfo, condition);
	}

	/**
	 * Retrieve entities based on class and condition
	 *
	 * @param entityClass {@link Class}
	 * @param condition   {@link MDCondition}
	 * @param subRequest  boolean
	 *                    <p>true: the call is a subrequest and referenced entities must not be retrieved</p>
	 *                    <p>false: the call is a basic request and reference entities are going to be retrieved</p>
	 * @param <T>         T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	public <T> List<T> getEntities(Class<T> entityClass, MDCondition condition, boolean subRequest) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return getEntities(entityInfo, condition, subRequest);
	}

	/**
	 * Retrieve entities based on entityInfo
	 *
	 * @param entityInfo {@link MDEntityInfo}
	 * @param <T>        T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	private <T> List<T> getEntities(MDEntityInfo entityInfo) throws MDException {
		return getEntities(entityInfo, null);
	}

	/**
	 * Retrieve entities based on entityInfo and condition
	 *
	 * @param entityInfo {@link MDEntityInfo}
	 * @param condition  {@link MDCondition}
	 * @param <T>        T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	private <T> List<T> getEntities(MDEntityInfo entityInfo, MDCondition condition) throws MDException {
		return getEntities(entityInfo, condition, false);
	}

	/**
	 * Retrieve entities based on entityInfo and condition
	 *
	 * @param entityInfo {@link MDEntityInfo}
	 * @param condition  {@link MDCondition}
	 * @param subRequest boolean
	 *                   true: the call is a ubrequest and referenced entities must not be retrieved
	 *                   false: the call is a basic request and reference entities are going to be retrieved
	 * @param <T>        T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	private <T> List<T> getEntities(MDEntityInfo entityInfo, MDCondition condition, boolean subRequest) throws MDException {
		// Control if it's an MDEntity
		if (!entityInfo.isMDEntity()) {
			throw new MDNotAnMDEntityException(entityInfo.getEntityClass());
		}
		if (!entityInfo.isSQLActionAllowed(SELECT)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), SELECT);
		}

		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(entityInfo.getEntityClass());

		// Condition
		if (condition != null) {
			selectBuilder.where(condition);
		}

		// InheritLink
		MDEntityInfo parentEntityInfo = entityInfo.getParentEntityInfo();
		if (parentEntityInfo != null) {
			selectBuilder.innerJoin(parentEntityInfo.getEntityClass());
		}

		// Many To Ones entities
		if (!subRequest) {
			List<MDEntityInfo> manyToOneEntityInfos = entityInfo.getManyToOneEntityInfos(HEAVY);
			if (!manyToOneEntityInfos.isEmpty()) {
				for (MDEntityInfo manyToOneEntityInfo : manyToOneEntityInfos) {
					selectBuilder.innerJoin(manyToOneEntityInfo.getEntityClass());
				}
			}
		}

		// Build query
		String query = selectBuilder.build();

		// Execute query
		ResultSet resultSet;
		resultSet = MDConnection.executeQuery(mdConnectionConfig, query);

		List<T> entities = new ArrayList<>();
		try {
			// Map resultset to the expected objet
			while (resultSet.next()) {
				entities.add(entityInfo.mapEntity(resultSet, this, subRequest));
			}
		} catch (SQLException e) {
			throw new MDException(e);
		}

		return entities;
	}
}
