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

import java.sql.*;

/**
 * @author Thomas Kint
 */
public class MDConnection {

	private Statement statement;

	MDConnection(MDConnectionConfig connectionConfig) throws MDException {
		try {
			Class.forName(connectionConfig.getDriver().getValue());

			String completeUrl = connectionConfig.getCompleteUrl();
			String login = connectionConfig.getLogin();
			String password = connectionConfig.getPassword();

			Connection connection = DriverManager.getConnection(completeUrl, login, password);

			statement = connection.createStatement();

		} catch (ClassNotFoundException | SQLException ex) {
			throw new MDException(ex);
		}
	}

	public static ResultSet executeQuery(MDConnectionConfig connectionConfig, String sql) throws MDException {
		try {
			return new MDConnection(connectionConfig).statement.executeQuery(sql);
		} catch (SQLException e) {
			throw new MDException(e);
		}
	}

	public static int executeUpdate(MDConnectionConfig connectionConfig, String sql) throws MDException {
		try {
			return new MDConnection(connectionConfig).statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			throw new MDException(e);
		}
	}
}
