package com.thomaskint.minidao.enumeration;

/**
 * @author Thomas Kint
 */
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

	@Override
	public String toString() {
		return value;
	}
}
