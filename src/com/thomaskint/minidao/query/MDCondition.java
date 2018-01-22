package com.thomaskint.minidao.query;

import java.lang.reflect.Field;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDCondition {

	private String field;

	private Object value;

	private MDCondition condition1;

	private String operator;

	private MDCondition condition2;

	private boolean isolated;

	private boolean parent;

	public MDCondition(String field, Object value) {
		this.field = field;
		this.value = value;
		this.parent = false;
	}

	public MDCondition(MDCondition condition1, String operator, MDCondition condition2, boolean isolated) {
		this.condition1 = condition1;
		this.operator = operator;
		this.condition2 = condition2;
		this.isolated = isolated;
		this.parent = true;
	}

	public String build() {
		String str = "";
		if (parent) {
			if (isolated) {
				str += "(";
			}
			str += condition1.build();
			str += " " + operator + " ";
			str += condition2.build();
			if (isolated) {
				str += ")";
			}
		} else {
			str += field + " = " + value;
		}

		return str;
	}
}
