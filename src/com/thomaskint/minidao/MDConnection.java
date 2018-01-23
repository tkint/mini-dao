/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskint.minidao;

import com.thomaskint.minidao.config.MDConnectionConfig;

import javax.naming.NamingException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tkint
 */
public class MDConnection {

	private java.sql.Connection connection;
	private Statement statement;

	private MDConnection(MDConnectionConfig mdConnectionConfig) throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String jdbcUrl = "jdbc:mysql://" +
					mdConnectionConfig.getUrl() +
					":" +
					mdConnectionConfig.getPort() +
					"/" +
					mdConnectionConfig.getDatabase() +
					"?zeroDateTimeBehavior=convertToNull";

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
