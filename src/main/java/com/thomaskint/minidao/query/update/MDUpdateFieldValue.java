package com.thomaskint.minidao.query.update;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDUpdateFieldValue {

	private String field;

	private Object value;

	public MDUpdateFieldValue(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public Object getValue() {
		return value;
	}

	public String build() {
		return field + " = '" + value + "'";
	}
}
