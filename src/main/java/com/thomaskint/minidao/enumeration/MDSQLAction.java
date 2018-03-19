package com.thomaskint.minidao.enumeration;

/**
 * @author Thomas Kint
 */
public enum MDSQLAction {
	SELECT("SELECT"),
	INSERT("INSERT"),
	UPDATE("UPDATE"),
	DELETE("DELETE");

	private String value;

	MDSQLAction(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
