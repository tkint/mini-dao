package com.thomaskint.minidao.connection;

/**
 * Created by tkint on 13/02/2018.
 */
public enum MDDriver {
	MYSQL("com.mysql.jdbc.Driver", "mysql"),
	ORACLE("com.oracle.jdbc.OracleDriver", "oracle");

	private String value;

	private String urlPart;

	MDDriver(String value, String urlPart) {
		this.value = value;
		this.urlPart = urlPart;
	}

	public String getValue() {
		return value;
	}

	public String getUrlPart() {
		return urlPart;
	}
}
