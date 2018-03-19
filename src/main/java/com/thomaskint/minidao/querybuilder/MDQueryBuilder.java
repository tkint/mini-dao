package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.enumeration.MDConditionLink;
import com.thomaskint.minidao.enumeration.MDConditionOperator;
import com.thomaskint.minidao.enumeration.MDJoinType;
import com.thomaskint.minidao.enumeration.MDSQLAction;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.model.MDFieldInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thomaskint.minidao.enumeration.MDConditionLink.AND;
import static com.thomaskint.minidao.enumeration.MDConditionLink.OR;
import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDJoinType.*;
import static com.thomaskint.minidao.enumeration.MDSQLWord.ON;
import static com.thomaskint.minidao.enumeration.MDSQLWord.WHERE;
import static com.thomaskint.minidao.utils.MDStringUtils.LEFT_PARENTHESIS;
import static com.thomaskint.minidao.utils.MDStringUtils.RIGHT_PARENTHESIS;
import static com.thomaskint.minidao.utils.MDStringUtils.SPACE;

/**
 * @author Thomas Kint
 */
public abstract class MDQueryBuilder<U extends MDQueryBuilder<U>> {

	MDSQLAction verb;
	MDEntityInfo baseEntityInfo;
	Map<MDJoinType, MDEntityInfo> joinedEntitiesInfo;
	Map<MDConditionLink, MDCondition> conditions;
	StringBuilder queryBuilder;

	MDQueryBuilder(MDSQLAction verb) {
		this.verb = verb;
		this.conditions = new HashMap<>();
		this.joinedEntitiesInfo = new HashMap<>();
		this.queryBuilder = new StringBuilder();
	}

	public final <T> U innerJoin(Class<T> entityClass) {
		return join(INNER_JOIN, entityClass);
	}

	public final <T> U leftOuterJoin(Class<T> entityClass) {
		joinedEntitiesInfo.put(LEFT_OUTER_JOIN, new MDEntityInfo(entityClass));
		return (U) this;
	}

	public final <T> U rightOuterJoin(Class<T> entityClass) {
		joinedEntitiesInfo.put(RIGHT_OUTER_JOIN, new MDEntityInfo(entityClass));
		return (U) this;
	}

	public final <T> U join(MDJoinType joinType, Class<T> entityClass) {
		joinedEntitiesInfo.put(joinType, new MDEntityInfo(entityClass));
		return (U) this;
	}

	public final U where(String fieldName, MDConditionOperator operator, Object value) {
		conditions.clear();
		conditions.put(null, new MDCondition(fieldName, operator, value));
		return (U) this;
	}

	public final U where(MDCondition condition) {
		conditions.clear();
		conditions.put(null, condition);
		return (U) this;
	}

	public final U and(String fieldName, MDConditionOperator operator, Object value) {
		conditions.put(AND, new MDCondition(fieldName, operator, value));
		return (U) this;
	}

	public final U and(MDCondition condition) {
		conditions.put(AND, condition);
		return (U) this;
	}

	public final U or(String fieldName, MDConditionOperator operator, Object value) {
		conditions.put(OR, new MDCondition(fieldName, operator, value));
		return (U) this;
	}

	public final U or(MDCondition condition) {
		conditions.put(OR, condition);
		return (U) this;
	}

	protected abstract void buildTablePart();

	protected abstract void buildVerbPart();

	protected void buildJoinPart() {
		MDFieldInfo linkFieldInfo;
		for (Map.Entry<MDJoinType, MDEntityInfo> joinedEntityInfo : joinedEntitiesInfo.entrySet()) {
			linkFieldInfo = baseEntityInfo.getFieldInfoLinkedTo(joinedEntityInfo.getValue().getEntityClass());
			if (linkFieldInfo != null) {
				queryBuilder.append(SPACE);
				queryBuilder.append(joinedEntityInfo.getKey());
				queryBuilder.append(SPACE);
				queryBuilder.append(joinedEntityInfo.getValue().getTableName());
				queryBuilder.append(SPACE);
				queryBuilder.append(ON);
				queryBuilder.append(SPACE);
				queryBuilder.append(linkFieldInfo.getFieldFullName());
				queryBuilder.append(SPACE);
				queryBuilder.append(EQUAL);
				queryBuilder.append(SPACE);
				queryBuilder.append(joinedEntityInfo.getValue().getIDFieldInfo().getFieldFullName());
			}
		}
	}

	protected void buildWherePart() {
		int addedConditions = 0;
		StringBuilder tempBuilder = new StringBuilder();

		MDFieldInfo fieldInfo;
		MDEntityInfo conditionEntityInfo;
		for (Map.Entry<MDConditionLink, MDCondition> condition : conditions.entrySet()) {
			conditionEntityInfo = null;
			if (baseEntityInfo.getMDFieldInfoByFieldName(condition.getValue().getFieldName()) != null) {
				conditionEntityInfo = baseEntityInfo;
			} else {
				List<MDEntityInfo> joinedEntitiesInfos = new ArrayList<>(joinedEntitiesInfo.values());
				int i = 0;
				while (i < joinedEntitiesInfos.size() && conditionEntityInfo == null) {
					if (joinedEntitiesInfos.get(i).getMDFieldInfoByFieldName(condition.getValue().getFieldName()) != null) {
						conditionEntityInfo = joinedEntitiesInfos.get(i);
					}
					i++;
				}
			}

			if (conditionEntityInfo != null) {
				fieldInfo = conditionEntityInfo.getMDFieldInfoByFieldName(condition.getValue().getFieldName());
				if (fieldInfo != null) {
					if (addedConditions > 0) {
						tempBuilder.append(SPACE);
						tempBuilder.append(condition.getKey());
					}
					tempBuilder.append(SPACE);
					if (condition.getValue().hasSubCondition()) {
						tempBuilder.append(LEFT_PARENTHESIS);
					}
					tempBuilder.append(condition.getValue().build(conditionEntityInfo));
					if (condition.getValue().hasSubCondition()) {
						tempBuilder.append(RIGHT_PARENTHESIS);
					}
					addedConditions++;
				}
			}
		}
		if (addedConditions > 0) {
			queryBuilder.append(SPACE);
			queryBuilder.append(WHERE);
			queryBuilder.append(tempBuilder);
		}
	}

	public String build() throws MDException {
		if (baseEntityInfo == null) {
			throw new MDException("No target!");
		}
		buildVerbPart();
		buildTablePart();
		buildJoinPart();
		buildWherePart();
		return queryBuilder.toString();
	}

	protected MDEntityInfo getEntityInfoByFieldName(String fieldName) {
		MDEntityInfo entityInfo = null;
		if (this.baseEntityInfo != null && this.baseEntityInfo.getMDFieldInfoByFieldName(fieldName) != null) {
			entityInfo = this.baseEntityInfo;
		} else {
			List<MDEntityInfo> entitiesInfo = new ArrayList<>(joinedEntitiesInfo.values());
			int i = 0;
			while (i < entitiesInfo.size() && entityInfo == null) {
				if (entitiesInfo.get(i).getMDFieldInfoByFieldName(fieldName) != null) {
					entityInfo = entitiesInfo.get(i);
				}
				i++;
			}
		}
		return entityInfo;
	}

	protected boolean isFieldInfoParamValid(MDFieldInfo fieldInfo) {
		return baseEntityInfo.isSQLActionAllowed(verb) && fieldInfo.isSQLActionAllowed(verb);
	}
}
