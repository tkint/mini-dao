package com.thomaskint.minidao.testonly;

import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.connection.MDDriver;

import java.io.File;

/**
 * @author Thomas Kint
 */
public class Config {

	private static final File file = new File("src/test/resources/database");

	private static final String databaseName = "h2test";

	public static final MDConnectionConfig mdConnectionConfig = new MDConnectionConfig(MDDriver.H2, file.getAbsolutePath(), null, "root", "password", databaseName);
}
