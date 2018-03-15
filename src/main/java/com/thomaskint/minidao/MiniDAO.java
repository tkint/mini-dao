package com.thomaskint.minidao;

import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.connection.MDDriver;
import com.thomaskint.minidao.crud.MDCreate;
import com.thomaskint.minidao.crud.MDDelete;
import com.thomaskint.minidao.crud.MDRead;
import com.thomaskint.minidao.crud.MDUpdate;

/**
 * @author Thomas Kint
 */
public class MiniDAO {

	private final MDConnectionConfig connectionConfig;
	private final MDCreate create;
	private final MDRead read;
	private final MDUpdate update;
	private final MDDelete delete;

	public MiniDAO(MDConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
		this.create = new MDCreate(connectionConfig);
		this.read = new MDRead(connectionConfig);
		this.update = new MDUpdate(connectionConfig);
		this.delete = new MDDelete(connectionConfig);
	}

	public MiniDAO(MDDriver mdDriver, String url, String port, String login, String password, String database) {
		this(new MDConnectionConfig(mdDriver, url, port, login, password, database));
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
}
