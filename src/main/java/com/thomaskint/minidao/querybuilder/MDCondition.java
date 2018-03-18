package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.enumeration.MDConditionLink;
import com.thomaskint.minidao.enumeration.MDConditionOperator;
import com.thomaskint.minidao.model.MDEntityInfo;

import static com.thomaskint.minidao.utils.MDStringUtils.QUOTE;
import static com.thomaskint.minidao.utils.MDStringUtils.SPACE;

public class MDCondition {

	private String fieldName;

	private MDConditionOperator operator;

	private Object value;

	private MDConditionLink conditionLink = null;

	private MDCondition subCondition = null;

	public MDCondition(String fieldName, MDConditionOperator operator, Object value, MDConditionLink conditionLink, MDCondition subCondition) {
		this.fieldName = fieldName;
		this.operator = operator;
		this.value = value;
		this.conditionLink = conditionLink;
		this.subCondition = subCondition;
	}

	public MDCondition(String fieldName, MDConditionOperator operator, Object value) {
		this.fieldName = fieldName;
		this.operator = operator;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public MDConditionOperator getOperator() {
		return operator;
	}

	public Object getValue() {
		return value;
	}

	public String build(MDEntityInfo entityInfo) {
		StringBuilder conditionBuilder = new StringBuilder();
		conditionBuilder.append(entityInfo.getTableName());
		conditionBuilder.append(".");
		conditionBuilder.append(fieldName);
		conditionBuilder.append(SPACE);
		conditionBuilder.append(operator);
		conditionBuilder.append(SPACE);
		conditionBuilder.append(QUOTE);
		conditionBuilder.append(value);
		conditionBuilder.append(QUOTE);
		if (hasSubCondition()) {
			conditionBuilder.append(SPACE);
			conditionBuilder.append(conditionLink);
			conditionBuilder.append(SPACE);
			conditionBuilder.append(subCondition.build(entityInfo));
		}
		return conditionBuilder.toString();
	}

	public boolean hasSubCondition() {
		return subCondition != null && conditionLink != null;
	}
}
