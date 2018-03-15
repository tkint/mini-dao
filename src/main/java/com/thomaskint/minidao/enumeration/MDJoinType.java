package com.thomaskint.minidao.enumeration;

/**
 * @author Thomas Kint
 */
public enum MDJoinType {
	INNER_JOIN("INNER JOIN"),
	LEFT_OUTER_JOIN("LEFT OUTER JOIN"),
	RIGHT_OUTER_JOIN("RIGHT OUTER JOIN");

	private String value;

	MDJoinType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
