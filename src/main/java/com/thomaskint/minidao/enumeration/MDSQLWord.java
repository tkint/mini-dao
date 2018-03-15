package com.thomaskint.minidao.enumeration;

/**
 * @author Thomas Kint
 */
public enum MDSQLWord {
	ON("ON"),
	FROM("FROM"),
	WHERE("WHERE"),
	IS_NULL("IS NULL"),
	IS_NOT_NULL("IS NOT NULL"),
	JOKER_TAG("%"),
	VALUES("VALUES"),
	SET("SET"),
	ALL("*"),
	INTO("INTO");

	private String value;

	MDSQLWord(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
