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

	private static MDConnection instance;
	private java.sql.Connection connection = null;
	private Statement statement = null;

	private MDConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			MDConnectionConfig connexion = MDConnectionConfig.getInstance();

			String url = connexion.getUrl();
			String port = connexion.getPort();
			String login = connexion.getLogin();
			String password = connexion.getPassword();
			String database = connexion.getDatabase();

			String jdbcUrl = "jdbc:mysql://" + url + ":" + port + "/" + database;

			connection = DriverManager.getConnection(jdbcUrl, login, password);

			statement = connection.createStatement();

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (NamingException ex) {
			Logger.getLogger(MDConnection.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public java.sql.Connection getConnection() {
		return connection;
	}

	public void setConnection(java.sql.Connection connection) {
		this.connection = connection;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return statement.executeQuery(sql);
	}

	public int executeUpdate(String sql) throws SQLException {
		return statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
	}

	public static MDConnection getInstance() {
		if (instance == null) {
			instance = new MDConnection();
		}
		return instance;
	}
}
