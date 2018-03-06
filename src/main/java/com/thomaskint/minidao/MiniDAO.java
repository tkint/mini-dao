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

	public static MDConnectionConfig getMdConnectionConfig() {
		return mdConnectionConfig;
	}

	public static void verifyMDConnectionConfig(MDConnectionConfig mdConnectionConfig) throws Exception {
		if (mdConnectionConfig == null) {
			throw new Exception("MiniDAO config must be not null");
		}
	}

	// CREATE

	public static <T> boolean createEntity(T entity) {
		return createEntity(mdConnectionConfig, entity);
	}

	public static <T> boolean createEntity(MDConnectionConfig mdConnectionConfig, T entity) {
		boolean created = false;
		try {
			verifyMDConnectionConfig(mdConnectionConfig);
			created = MDCreate.createEntity(mdConnectionConfig, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return created;
	}

	// READ

	public static <T> List<T> getEntities(Class<T> entityClass) {
		return getEntities(mdConnectionConfig, entityClass);
	}

	public static <T> List<T> getEntities(Class<T> entityClass, MDCondition mdCondition) {
		return getEntities(mdConnectionConfig, entityClass, mdCondition);
	}

	public static <T> T getEntityById(Class<T> entityClass, Object id) {
		return getEntityById(mdConnectionConfig, entityClass, id);
	}

	public static <T> List<T> getEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass) {
		List<T> entities = null;
		try {
			verifyMDConnectionConfig(mdConnectionConfig);
			entities = MDRead.getEntities(mdConnectionConfig, entityClass, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}

	public static <T> List<T> getEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, MDCondition mdCondition) {
		List<T> entities = null;
		try {
			verifyMDConnectionConfig(mdConnectionConfig);
			entities = MDRead.getEntities(mdConnectionConfig, entityClass, mdCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}

	public static <T> T getEntityById(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, Object id) {
		T entity = null;
		try {
			verifyMDConnectionConfig(mdConnectionConfig);
			entity = MDRead.getEntityById(mdConnectionConfig, entityClass, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	// UPDATE

	public static <T> boolean updateEntity(T entity) {
		return updateEntity(mdConnectionConfig, entity);
	}

	public static <T> boolean updateEntity(MDConnectionConfig mdConnectionConfig, T entity) {
		boolean updated = false;
		try {
			verifyMDConnectionConfig(mdConnectionConfig);
			updated = MDUpdate.updateEntity(mdConnectionConfig, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updated;
	}

	// DELETE

	public static <T> boolean deleteEntities(Class<T> entityClass) {
		return deleteEntities(mdConnectionConfig, entityClass);
	}

	public static <T> boolean deleteEntities(Class<T> entityClass, MDCondition mdCondition) {
		return deleteEntities(mdConnectionConfig, entityClass, mdCondition);
	}

	public static <T> boolean deleteEntity(Class<T> entityClass, Object id) {
		return deleteEntity(mdConnectionConfig, entityClass, id);
	}

	public static <T> boolean deleteEntity(T entity) {
		return deleteEntity(mdConnectionConfig, entity);
	}

	public static <T> boolean deleteEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass) {
		boolean deleted = false;
		try {
			verifyMDConnectionConfig(mdConnectionConfig);
			deleted = MDDelete.deleteEntities(mdConnectionConfig, entityClass, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleted;
	}

	public static <T> boolean deleteEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, MDCondition mdCondition) {
		boolean deleted = false;
		try {
			verifyMDConnectionConfig(mdConnectionConfig);
			deleted = MDDelete.deleteEntities(mdConnectionConfig, entityClass, mdCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleted;
	}

	public static <T> boolean deleteEntity(MDConnectionConfig mdConnectionConfig, T entity) {
		boolean deleted = false;
		try {
			verifyMDConnectionConfig(mdConnectionConfig);
			deleted = MDDelete.deleteEntity(mdConnectionConfig, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleted;
	}

	public static <T> boolean deleteEntity(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, Object id) {
		boolean deleted = false;
		try {
			verifyMDConnectionConfig(mdConnectionConfig);
			deleted = MDDelete.deleteEntity(mdConnectionConfig, entityClass, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleted;
	}
}
