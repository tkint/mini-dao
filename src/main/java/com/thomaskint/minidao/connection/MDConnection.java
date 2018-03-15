/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskint.minidao.connection;

import com.thomaskint.minidao.exception.MDException;

import java.sql.*;

/**
 * @author Thomas Kint
 */
public class MDConnection {

	private Statement statement;

	private MDConnection(MDConnectionConfig mdConnectionConfig) throws MDException {
		try {
			Class.forName(mdConnectionConfig.getMdDriver().getValue());

			String completeUrl = mdConnectionConfig.getCompleteUrl();
			String login = mdConnectionConfig.getLogin();
			String password = mdConnectionConfig.getPassword();

			Connection connection = DriverManager.getConnection(completeUrl, login, password);

			statement = connection.createStatement();

		} catch (ClassNotFoundException | SQLException ex) {
			throw new MDException(ex);
		}
	}

	public static ResultSet executeQuery(MDConnectionConfig mdConnectionConfig, String sql) throws MDException {
		try {
			return new MDConnection(mdConnectionConfig).statement.executeQuery(sql);
		} catch (SQLException e) {
			throw new MDException(e);
		}
	}

	public static int executeUpdate(MDConnectionConfig mdConnectionConfig, String sql) throws MDException {
		try {
			return new MDConnection(mdConnectionConfig).statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			throw new MDException(e);
		}
	}
}
