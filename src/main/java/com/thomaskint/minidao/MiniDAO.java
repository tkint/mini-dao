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

package com.thomaskint.minidao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.connection.MDDriver;
import com.thomaskint.minidao.crud.MDCreate;
import com.thomaskint.minidao.crud.MDDelete;
import com.thomaskint.minidao.crud.MDRead;
import com.thomaskint.minidao.crud.MDUpdate;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.model.MDEntityInfo;

/**
 * @author Thomas Kint
 */
public class MiniDAO {

	private final MDConnectionConfig connectionConfig;
	private final MDCreate create;
	private final MDRead read;
	private final MDUpdate update;
	private final MDDelete delete;
	private final String dateFormat;

	private static final String defaultDateFormat = "dd/MM/yy HH:mm:ss";

	public MiniDAO(MDConnectionConfig connectionConfig) {
		this.dateFormat = defaultDateFormat;
		this.connectionConfig = connectionConfig;
		this.create = new MDCreate(connectionConfig);
		this.read = new MDRead(connectionConfig);
		this.update = new MDUpdate(connectionConfig, this.dateFormat);
		this.delete = new MDDelete(connectionConfig);
	}

	public MiniDAO(MDConnectionConfig connectionConfig, String dateFormat) {
		this.dateFormat = dateFormat;
		this.connectionConfig = connectionConfig;
		this.create = new MDCreate(connectionConfig);
		this.read = new MDRead(connectionConfig);
		this.update = new MDUpdate(connectionConfig, dateFormat);
		this.delete = new MDDelete(connectionConfig);
	}

	public MiniDAO(MDDriver driver, String url, String port, String login, String password, String database) {
		this(new MDConnectionConfig(driver, url, port, login, password, database));
	}

	public MiniDAO(String url, String port, String login, String password, String database) {
		this(new MDConnectionConfig(MDDriver.DEFAULT, url, port, login, password, database));
	}

	public MDConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}

	public MDCreate create() {
		return create;
	}

	public MDRead read() {
		return read;
	}

	public MDUpdate update() {
		return update;
	}

	public MDDelete delete() {
		return delete;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public static String getDefaultDateFormat() {
		return defaultDateFormat;
	}

	public ResultSet executeQuery(String query) throws MDException {
		return MDConnection.executeQuery(connectionConfig, query);
	}

	public int executeUpdate(String query) throws MDException {
		try {
			return MDConnection.executeUpdate(connectionConfig, query);
		} finally {
			MDConnection.close();
		}
	}

	public <T> T mapResultSetToEntity(ResultSet resultSet, Class<T> entityClass) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		T entity = null;
		try {
			if (resultSet.next()) {
				entity = entityInfo.mapEntity(resultSet, read, null);
			}
			resultSet.close();
		} catch (SQLException e) {
			throw new MDException(e);
		}
		return entity;
	}

	public <T> List<T> mapResultSetToEntities(ResultSet resultSet, Class<T> entityClass, int offset, int limit) throws MDException {
		List<T> entities = new ArrayList<>();
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		int count = 0;
		try {
			if (resultSet.absolute(offset)) {
				while (resultSet.next() && count < limit) {
					entities.add(entityInfo.mapEntity(resultSet, read, null));
					count++;
				}
			}
			resultSet.close();
		} catch (SQLException e) {
			throw new MDException(e);
		}
		return entities;
	}

	public void closeConnection() throws MDException {
		MDConnection.close();
	}
}
