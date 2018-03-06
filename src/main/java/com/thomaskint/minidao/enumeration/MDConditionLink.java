package com.thomaskint.minidao.enumeration;

/**
 * Created by tkint on 19/01/2018.
 */
public enum MDConditionLink {
	OR("OR"), AND("AND");

	private String value;

	MDConditionLink(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
