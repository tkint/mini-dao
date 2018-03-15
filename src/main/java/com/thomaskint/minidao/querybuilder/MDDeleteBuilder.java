package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.model.MDEntityInfo;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDSQLWord.FROM;
import static com.thomaskint.minidao.enumeration.MDVerb.DELETE;
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
		MDDeleteBuilder deleteBuilder = this;
		baseEntityInfo = new MDEntityInfo(entity.getClass());
		if (baseEntityInfo.getIDFieldInfo() != null) {
			String idFieldName = baseEntityInfo.getIDFieldInfo().getFieldName();
			if (baseEntityInfo.getIDFieldInfo().getField() != null) {
				try {
					Object value = baseEntityInfo.getIDFieldInfo().getField().get(entity);
					deleteBuilder = where(idFieldName, EQUAL, value);
				} catch (IllegalAccessException e) {
					throw new MDException("Can't access this field " + idFieldName);
				}
			}
		}
		return deleteBuilder;
	}

	public <T> MDDeleteBuilder from(Class<T> entityClass) {
		baseEntityInfo = new MDEntityInfo(entityClass);
		return this;
	}


	@Override
	protected void buildVerbPart() {
		queryBuilder.append(verb);
	}

	@Override
	protected void buildTablePart() {
		queryBuilder.append(SPACE);
		queryBuilder.append(FROM);
		queryBuilder.append(SPACE);
		queryBuilder.append(baseEntityInfo.getTableName());
	}
}
