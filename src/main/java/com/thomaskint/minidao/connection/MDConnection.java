/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskint.minidao.connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author tkint
 */
public class MDConnection {

	private java.sql.Connection connection;
	private Statement statement;

	private MDConnection(MDConnectionConfig mdConnectionConfig) throws ClassNotFoundException, SQLException {
		try {
			Class.forName(mdConnectionConfig.getMdDriver().getValue());

			String jdbcUrl = mdConnectionConfig.getCompleteUrl();

			connection = DriverManager.getConnection(jdbcUrl, mdConnectionConfig.getLogin(), mdConnectionConfig.getPassword());

			statement = connection.createStatement();

		} catch (ClassNotFoundException | SQLException ex) {
			throw ex;
		}
	}

	public static ResultSet executeQuery(MDConnectionConfig mdConnectionConfig, String sql) throws SQLException, ClassNotFoundException {
		return new MDConnection(mdConnectionConfig).statement.executeQuery(sql);
	}

	public static int executeUpdate(MDConnectionConfig mdConnectionConfig, String sql) throws SQLException, ClassNotFoundException {
		return new MDConnection(mdConnectionConfig).statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
	}
}
