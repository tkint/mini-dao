package com.thomaskint.minidao.query.create;

/**
 * Created by tkint on 22/01/2018.
 */
public class MDCreateFieldValue {

	private String field;

	private Object value;

	public MDCreateFieldValue(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public Object getValue() {
		return value;
	}
}
