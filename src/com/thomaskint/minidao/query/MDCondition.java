package com.thomaskint.minidao.query;

import com.thomaskint.minidao.enumeration.MDConditionLink;
import com.thomaskint.minidao.enumeration.MDConditionOperator;
import com.thomaskint.minidao.utils.MDFieldUtils;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDCondition {

	private String field;

	private Object value;

	private MDConditionOperator operator;

	private boolean joker;

	private MDCondition condition1;

	private MDConditionLink conditionsLink;

	private MDCondition condition2;

	private boolean isolated;

	private boolean parent;

	public MDCondition(String field, Object value) {
		this(field, MDConditionOperator.EQUAL, value);
	}

	public MDCondition(String field, MDConditionOperator operator, Object value) {
		this(field, operator, value, false);
	}

	public MDCondition(String field, MDConditionOperator operator, Object value, boolean joker) {
		this.field = field;
		this.operator = operator;
		this.value = value;
		this.joker = joker;
		this.parent = false;
	}

	public MDCondition(MDCondition condition1, MDConditionLink conditionsLink, MDCondition condition2) {
		this.condition1 = condition1;
		this.conditionsLink = conditionsLink;
		this.condition2 = condition2;
		this.isolated = false;
		this.parent = true;
	}

	public MDCondition(MDCondition condition1, MDConditionLink conditionsLink, MDCondition condition2, boolean isolated) {
		this.condition1 = condition1;
		this.conditionsLink = conditionsLink;
		this.condition2 = condition2;
		this.isolated = isolated;
		this.parent = true;
	}

	public String build(Class entityClass) throws Exception {
		return build(entityClass, true);
	}

	private String build(Class entityClass, boolean first) throws Exception {
		String str = "";
		if (first) {
			str += " WHERE ";
		}
		if (parent) {
			str += buildParent(entityClass);
		} else {
			str += buildChild(entityClass);
		}
		return str;
	}

	private String buildParent(Class entityClass) throws Exception {
		String str = "";
		if (isolated) {
			str += "(";
		}
		str += condition1.build(entityClass, false);
		str += " " + conditionsLink + " ";
		str += condition2.build(entityClass, false);
		if (isolated) {
			str += ")";
		}
		return str;
	}

	private String buildChild(Class entityClass) throws Exception {
		verifyCondition(entityClass);
		String str = "";
		str += field + " " + operator.getValue() + " '";
		if (joker) {
			str += "%";
		}
		str += value;
		if (joker) {
			str += "%";
		}
		str += "'";
		return str;
	}

	private void verifyCondition(Class entityClass) throws Exception {
		if (MDFieldUtils.getFieldByName(entityClass, field) == null) {
			throw new Exception("Database field " + field + " is not declared " + entityClass.getName());
		}
	}
}
