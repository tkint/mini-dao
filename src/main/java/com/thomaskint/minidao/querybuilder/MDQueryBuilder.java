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

import com.thomaskint.minidao.enumeration.MDConditionLink;
import com.thomaskint.minidao.enumeration.MDConditionOperator;
import com.thomaskint.minidao.enumeration.MDJoinType;
import com.thomaskint.minidao.enumeration.MDSQLAction;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.model.MDFieldInfo;
import javafx.util.Pair;

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
	List<Pair<MDJoinType, MDEntityInfo>> joinedEntitiesInfo;
	List<Pair<MDConditionLink, MDCondition>> conditions;
	StringBuilder queryBuilder;

	MDQueryBuilder(MDSQLAction verb) {
		this.verb = verb;
		this.conditions = new ArrayList<>();
		this.joinedEntitiesInfo = new ArrayList<>();
		this.queryBuilder = new StringBuilder();
	}

	public final <T> U innerJoin(Class<T> entityClass) {
		return join(INNER_JOIN, entityClass);
	}

	public final <T> U leftOuterJoin(Class<T> entityClass) {
		joinedEntitiesInfo.add(new Pair<>(LEFT_OUTER_JOIN, new MDEntityInfo(entityClass)));
		return (U) this;
	}

	public final <T> U rightOuterJoin(Class<T> entityClass) {
		joinedEntitiesInfo.add(new Pair<>(RIGHT_OUTER_JOIN, new MDEntityInfo(entityClass)));
		return (U) this;
	}

	public final <T> U join(MDJoinType joinType, Class<T> entityClass) {
		joinedEntitiesInfo.add(new Pair<>(joinType, new MDEntityInfo(entityClass)));
		return (U) this;
	}

	public final U where(String fieldName, MDConditionOperator operator, Object value) {
		conditions.clear();
		conditions.add(new Pair<>(null, new MDCondition(fieldName, operator, value)));
		return (U) this;
	}

	public final U where(MDCondition condition) {
		conditions.clear();
		conditions.add(new Pair<>(null, condition));
		return (U) this;
	}

	public final U and(String fieldName, MDConditionOperator operator, Object value) {
		conditions.add(new Pair<>(AND, new MDCondition(fieldName, operator, value)));
		return (U) this;
	}

	public final U and(MDCondition condition) {
		conditions.add(new Pair<>(AND, condition));
		return (U) this;
	}

	public final U or(String fieldName, MDConditionOperator operator, Object value) {
		conditions.add(new Pair<>(OR, new MDCondition(fieldName, operator, value)));
		return (U) this;
	}

	public final U or(MDCondition condition) {
		conditions.add(new Pair<>(OR, condition));
		return (U) this;
	}

	protected abstract void buildTablePart();

	protected abstract void buildVerbPart();

	protected void buildJoinPart() {
		MDFieldInfo linkFieldInfo;
		for (Pair<MDJoinType, MDEntityInfo> joinedEntityInfo : joinedEntitiesInfo) {
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
		for (Pair<MDConditionLink, MDCondition> condition : conditions) {
			conditionEntityInfo = null;
			if (baseEntityInfo.getMDFieldInfoByFieldName(condition.getValue().getFieldName()) != null) {
				conditionEntityInfo = baseEntityInfo;
			} else {
				List<MDEntityInfo> entityInfos = new ArrayList<>();
				for (Pair<MDJoinType, MDEntityInfo> joinedEntityInfo : joinedEntitiesInfo) {
					entityInfos.add(joinedEntityInfo.getValue());
				}
				int i = 0;
				while (i < entityInfos.size() && conditionEntityInfo == null) {
					if (entityInfos.get(i).getMDFieldInfoByFieldName(condition.getValue().getFieldName()) != null) {
						conditionEntityInfo = entityInfos.get(i);
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
			List<MDEntityInfo> entityInfos = new ArrayList<>();
			for (Pair<MDJoinType, MDEntityInfo> joinedEntityInfo : joinedEntitiesInfo) {
				entityInfos.add(joinedEntityInfo.getValue());
			}
			int i = 0;
			while (i < entityInfos.size() && entityInfo == null) {
				if (entityInfos.get(i).getMDFieldInfoByFieldName(fieldName) != null) {
					entityInfo = entityInfos.get(i);
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
