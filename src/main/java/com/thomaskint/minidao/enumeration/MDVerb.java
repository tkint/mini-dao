package com.thomaskint.minidao.enumeration;

/**
 * @author Thomas Kint
 */
public enum MDVerb {
	SELECT("SELECT"),
	INSERT("INSERT"),
	UPDATE("UPDATE"),
	DELETE("DELETE");

	private String value;

	MDVerb(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
