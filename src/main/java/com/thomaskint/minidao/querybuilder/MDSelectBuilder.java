package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.model.MDFieldInfo;

import static com.thomaskint.minidao.enumeration.MDSQLWord.ALL;
import static com.thomaskint.minidao.enumeration.MDSQLWord.FROM;
import static com.thomaskint.minidao.enumeration.MDSQLAction.SELECT;
import static com.thomaskint.minidao.utils.MDStringUtils.COMMA;
import static com.thomaskint.minidao.utils.MDStringUtils.SPACE;

/**
 * @author Thomas Kint
 */
public class MDSelectBuilder extends MDQueryBuilder<MDSelectBuilder> {

	private String[] fieldsName = new String[0];

	public MDSelectBuilder() {
		super(SELECT);
	}

	public MDSelectBuilder select(String... fieldNames) {
		this.fieldsName = fieldNames;
		return this;
	}

	public <T> MDSelectBuilder from(Class<T> entityClass) {
		baseEntityInfo = new MDEntityInfo(entityClass);
		return this;
	}

	@Override
	protected void buildVerbPart() {
		int addedFields = 0;
		StringBuilder tempBuilder = new StringBuilder();

		MDEntityInfo entityInfo;
		MDFieldInfo fieldInfo;
		for (String fieldName : fieldsName) {
			entityInfo = getEntityInfoByFieldName(fieldName);
			if (entityInfo != null) {
				fieldInfo = entityInfo.getMDFieldInfoByFieldName(fieldName);
				if (isFieldInfoParamValid(fieldInfo)) {
					if (addedFields > 0) {
						tempBuilder.append(COMMA);
						tempBuilder.append(SPACE);
					}
					tempBuilder.append(fieldInfo.getFieldFullName());
					addedFields++;
				}
			}
		}

		queryBuilder.append(verb);
		queryBuilder.append(SPACE);
		if (addedFields == 0) {
			queryBuilder.append(ALL);
		} else {
			queryBuilder.append(tempBuilder);
		}
	}

	@Override
	protected void buildTablePart() {
		queryBuilder.append(SPACE);
		queryBuilder.append(FROM);
		queryBuilder.append(SPACE);
		queryBuilder.append(baseEntityInfo.getTableName());
	}
}
