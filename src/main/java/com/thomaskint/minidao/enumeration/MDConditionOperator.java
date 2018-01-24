package com.thomaskint.minidao.enumeration;

public enum MDConditionOperator {
	EQUAL("="),
	DIFFERENT("!="),
	STRICTLY_SUPERIOR(">"),
	STRICTLY_INFERIOR("<"),
	LIKE("LIKE");

	private String value;

	MDConditionOperator(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
