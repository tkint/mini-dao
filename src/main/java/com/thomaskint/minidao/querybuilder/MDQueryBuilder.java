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

package com.thomaskint.minidao.querybuilder;

import java.util.ArrayList;
import java.util.List;

import com.thomaskint.minidao.enumeration.MDConditionLink;
import com.thomaskint.minidao.enumeration.MDConditionOperator;
import com.thomaskint.minidao.enumeration.MDJoinType;
import com.thomaskint.minidao.enumeration.MDSQLAction;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.model.MDFieldInfo;
import com.thomaskint.minidao.model.MDPair;

import static com.thomaskint.minidao.enumeration.MDConditionLink.AND;
import static com.thomaskint.minidao.enumeration.MDConditionLink.OR;
import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDJoinType.INNER_JOIN;
import static com.thomaskint.minidao.enumeration.MDJoinType.LEFT_OUTER_JOIN;
import static com.thomaskint.minidao.enumeration.MDJoinType.RIGHT_OUTER_JOIN;
import static com.thomaskint.minidao.enumeration.MDSQLWord.ON;
import static com.thomaskint.minidao.enumeration.MDSQLWord.WHERE;
import static com.thomaskint.minidao.utils.MDStringUtils.LEFT_PARENTHESIS;
import static com.thomaskint.minidao.utils.MDStringUtils.RIGHT_PARENTHESIS;
import static com.thomaskint.minidao.utils.MDStringUtils.SPACE;

/**
 * @author Thomas Kint
 */
public abstract class MDQueryBuilder<U extends MDQueryBuilder<U>> {

	MDSQLAction sqlAction;
	MDEntityInfo baseEntityInfo;

	List<MDPair<MDJoinType, MDEntityInfo>> joinedEntitiesInfo;

	List<MDPair<MDConditionLink, MDCondition>> conditions;
	StringBuilder queryBuilder;

	int limit = -1;

	int offset = -1;

	MDQueryBuilder(MDSQLAction sqlAction) {
		this.sqlAction = sqlAction;
		this.conditions = new ArrayList<>();
		this.joinedEntitiesInfo = new ArrayList<>();
		this.queryBuilder = new StringBuilder();
	}

	public final <T> U innerJoin(Class<T> entityClass) {
		return join(INNER_JOIN, entityClass);
	}

	public final <T> U leftOuterJoin(Class<T> entityClass) {
		joinedEntitiesInfo.add(new MDPair<>(LEFT_OUTER_JOIN, new MDEntityInfo(entityClass)));
		return (U) this;
	}

	public final <T> U rightOuterJoin(Class<T> entityClass) {
		joinedEntitiesInfo.add(new MDPair<>(RIGHT_OUTER_JOIN, new MDEntityInfo(entityClass)));
		return (U) this;
	}

	public final <T> U join(MDJoinType joinType, Class<T> entityClass) {
		joinedEntitiesInfo.add(new MDPair<>(joinType, new MDEntityInfo(entityClass)));
		return (U) this;
	}

	public final U where(String fieldName, MDConditionOperator operator, Object value) {
		conditions.clear();
		conditions.add(new MDPair<>(null, new MDCondition(fieldName, operator, value)));
		return (U) this;
	}

	public final U where(MDCondition condition) {
		conditions.clear();
		conditions.add(new MDPair<>(null, condition));
		return (U) this;
	}

	public final U and(String fieldName, MDConditionOperator operator, Object value) {
		conditions.add(new MDPair<>(AND, new MDCondition(fieldName, operator, value)));
		return (U) this;
	}

	public final U and(MDCondition condition) {
		conditions.add(new MDPair<>(AND, condition));
		return (U) this;
	}

	public final U or(String fieldName, MDConditionOperator operator, Object value) {
		conditions.add(new MDPair<>(OR, new MDCondition(fieldName, operator, value)));
		return (U) this;
	}

	public final U or(MDCondition condition) {
		conditions.add(new MDPair<>(OR, condition));
		return (U) this;
	}

	public final U limit(int limit) {
		this.limit = limit;
		return (U) this;
	}

	public final U offset(int offset) {
		this.offset = offset;
		return (U) this;
	}

	protected abstract void buildTablePart();

	protected abstract void buildVerbPart();

	protected void buildJoinPart() {
		MDFieldInfo linkFieldInfo;
		for (MDPair<MDJoinType, MDEntityInfo> joinedEntityInfo : joinedEntitiesInfo) {
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
		for (MDPair<MDConditionLink, MDCondition> condition : conditions) {
			conditionEntityInfo = null;
			if (baseEntityInfo.getMDFieldInfoByFieldName(condition.getValue().getFieldName()) != null) {
				conditionEntityInfo = baseEntityInfo;
			} else {
				int i = 0;
				while (i < joinedEntitiesInfo.size() && conditionEntityInfo == null) {
					if (joinedEntitiesInfo.get(i) != null
							&& joinedEntitiesInfo.get(i).getValue() != null
							&& joinedEntitiesInfo.get(i).getValue().getMDFieldInfoByFieldName(condition.getValue().getFieldName()) != null) {
						conditionEntityInfo = joinedEntitiesInfo.get(i).getValue();
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
			int i = 0;
			while (i < joinedEntitiesInfo.size() && entityInfo == null) {
				if (joinedEntitiesInfo.get(i) != null
						&& joinedEntitiesInfo.get(i).getValue() != null
						&& joinedEntitiesInfo.get(i).getValue().getMDFieldInfoByFieldName(fieldName) != null) {
					entityInfo = joinedEntitiesInfo.get(i).getValue();
				}
				i++;
			}
		}
		return entityInfo;
	}

	protected boolean isFieldInfoParamValid(MDFieldInfo fieldInfo) {
		return baseEntityInfo.isSQLActionAllowed(sqlAction) && fieldInfo.isSQLActionAllowed(sqlAction);
	}
}
