/*
 * MIT License
 *
 * Copyright (c) 2017 Thomas Kint
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.thomaskint.minidao.connection;

import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.model.MDPair;

import java.sql.*;

/**
 * @author Thomas Kint
 */
public class MDConnection {

	private static MDConnection instance;

	private MDConnectionConfig connectionConfig;

	private Connection connection;

	private Statement statement;

	private MDConnection(MDConnectionConfig connectionConfig) throws MDException {
		try {
			Class.forName(connectionConfig.getDriver().getValue());

			String completeUrl = connectionConfig.getCompleteUrl();
			String login = connectionConfig.getLogin();
			String password = connectionConfig.getPassword();

			this.connection = DriverManager.getConnection(completeUrl, login, password);
			this.connectionConfig = connectionConfig;
		} catch (ClassNotFoundException | SQLException ex) {
			throw new MDException(ex);
		}
	}

	private static MDConnection getInstance(MDConnectionConfig connectionConfig) throws MDException {
		try {
			if (instance == null || !connectionConfig.equals(instance.connectionConfig) || instance.connection.isClosed()) {
				instance = new MDConnection(connectionConfig);
			}
		} catch (SQLException e) {
			throw new MDException(e);
		}
		return instance;
	}

	public static ResultSet executeQuery(MDConnectionConfig connectionConfig, String query) throws MDException {
		try {
			return getInstance(connectionConfig).prepareStatement(query, null).executeQuery();
		} catch (SQLException e) {
			throw new MDException(e);
		}
	}

	public static boolean execute(MDConnectionConfig connectionConfig, String sql) throws MDException {
		try {
			return getInstance(connectionConfig).createStatement().execute(sql);
		} catch (SQLException e) {
			throw new MDException(e);
		}
	}

	public static MDPair<Statement, Integer> executeUpdate(MDConnectionConfig connectionConfig, String query, String idFieldName) throws MDException {
		try {
			PreparedStatement statement = getInstance(connectionConfig).prepareStatement(query, idFieldName);
			Integer lines = statement.executeUpdate();
			return new MDPair<>(statement, lines);
		} catch (SQLException e) {
			throw new MDException(e);
		}
	}

	public static void close() throws MDException {
		try {
			if (instance.statement != null && !instance.statement.isClosed()) {
				if (instance.statement.getResultSet() != null && !instance.statement.getResultSet().isClosed()) {
					instance.statement.getResultSet().close();
				}
				instance.statement.close();
			}
			if (instance.connection != null && !instance.connection.isClosed()) {
				instance.connection.close();
			}
		} catch (SQLException e) {
			throw new MDException(e);
		}
	}

	private PreparedStatement prepareStatement(String query, String idFieldName) throws SQLException {
		if (idFieldName != null) {
			this.statement = this.connection.prepareStatement(query, new String[]{idFieldName});
		} else {
			this.statement = this.connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		}
//		statement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		return (PreparedStatement) statement;
	}

	private Statement createStatement() throws SQLException {
		this.statement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		return statement;
	}
}
