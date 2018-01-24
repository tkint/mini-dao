package com.thomaskint.minidao;

import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.query.MDCondition;
import com.thomaskint.minidao.query.create.MDCreate;
import com.thomaskint.minidao.query.delete.MDDelete;
import com.thomaskint.minidao.query.read.MDRead;
import com.thomaskint.minidao.query.update.MDUpdate;
import java.util.List;

/**
 * Created by tkint on 19/01/2018.
 */
public class MiniDAO {

	private static MDConnectionConfig mdConnectionConfig;

	public static void config(MDConnectionConfig mdConnectionConfig) {
		MiniDAO.mdConnectionConfig = mdConnectionConfig;
	}

	public static boolean isMDConnectionConfigSet() {
		return mdConnectionConfig != null;
	}

	public static void verifyMDConnectionConfig() throws Exception {
		if (!isMDConnectionConfigSet()) {
			throw new Exception("MiniDAO config must be not null");
		}
	}

	// CREATE

	public static <T> boolean createEntity(T entity) throws Exception {
		verifyMDConnectionConfig();
		return createEntity(mdConnectionConfig, entity);
	}

	public static <T> boolean createEntity(MDConnectionConfig mdConnectionConfig, T entity) throws Exception {
		return MDCreate.createEntity(mdConnectionConfig, entity);
	}

	// READ

	public static <T> List<T> getEntities(Class<T> entityClass) throws Exception {
		verifyMDConnectionConfig();
		return getEntities(mdConnectionConfig, entityClass);
	}

	public static <T> List<T> getEntities(Class<T> entityClass, MDCondition mdCondition) throws Exception {
		verifyMDConnectionConfig();
		return getEntities(mdConnectionConfig, entityClass, mdCondition);
	}

	public static <T> T getEntityById(Class<T> entityClass, int id) throws Exception {
		verifyMDConnectionConfig();
		return getEntityById(mdConnectionConfig, entityClass, id);
	}

	public static <T> List<T> getEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass) throws Exception {
		return MDRead.getEntities(mdConnectionConfig, entityClass, null);
	}

	public static <T> List<T> getEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, MDCondition mdCondition) throws Exception {
		return MDRead.getEntities(mdConnectionConfig, entityClass, mdCondition);
	}

	public static <T> T getEntityById(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, int id) throws Exception {
		return MDRead.getEntityById(mdConnectionConfig, entityClass, id);
	}

	// UPDATE

	public static <T> boolean updateEntity(T entity) throws Exception {
		verifyMDConnectionConfig();
		return MDUpdate.updateEntity(mdConnectionConfig, entity);
	}

	public static <T> boolean updateEntity(MDConnectionConfig mdConnectionConfig, T entity) throws Exception {
		return MDUpdate.updateEntity(mdConnectionConfig, entity);
	}

	// DELETE

	public static <T> boolean deleteEntities(Class<T> entityClass) throws Exception {
		verifyMDConnectionConfig();
		return deleteEntities(mdConnectionConfig, entityClass);
	}

	public static <T> boolean deleteEntities(Class<T> entityClass, MDCondition mdCondition) throws Exception {
		verifyMDConnectionConfig();
		return deleteEntities(mdConnectionConfig, entityClass, mdCondition);
	}

	public static <T> boolean deleteEntity(Class<T> entityClass, int id) throws Exception {
		verifyMDConnectionConfig();
		return deleteEntity(mdConnectionConfig, entityClass, id);
	}

	public static <T> boolean deleteEntity(T entity) throws Exception {
		verifyMDConnectionConfig();
		return MDDelete.deleteEntity(mdConnectionConfig, entity);
	}

	public static <T> boolean deleteEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass) throws Exception {
		return MDDelete.deleteEntities(mdConnectionConfig, entityClass, null);
	}

	public static <T> boolean deleteEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, MDCondition mdCondition) throws Exception {
		return MDDelete.deleteEntities(mdConnectionConfig, entityClass, mdCondition);
	}

	public static <T> boolean deleteEntity(MDConnectionConfig mdConnectionConfig, T entity) throws Exception {
		return MDDelete.deleteEntity(mdConnectionConfig, entity);
	}

	public static <T> boolean deleteEntity(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, int id) throws Exception {
		return MDDelete.deleteEntity(mdConnectionConfig, entityClass, id);
	}
}
