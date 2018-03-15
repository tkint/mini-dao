package com.thomaskint.minidao.enumeration;

/**
 * @author Thomas Kint
 */
public enum MDConditionLink {
	OR("OR"),
	AND("AND");

	private String value;

	MDConditionLink(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
