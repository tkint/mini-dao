package com.thomaskint.minidao;

import com.thomaskint.minidao.config.MDConnectionConfig;
import com.thomaskint.minidao.query.*;
import com.thomaskint.minidao.query.delete.MDDelete;
import com.thomaskint.minidao.query.read.MDRead;
import com.thomaskint.minidao.query.update.MDUpdate;
import com.thomaskint.minidao.query.update.MDUpdateFieldList;

import java.util.List;

/**
 * Created by tkint on 19/01/2018.
 */
public class MiniDAO {

	private static MDConnectionConfig mdConnectionConfig;

	public static void init(MDConnectionConfig mdConnectionConfig) {
		MiniDAO.mdConnectionConfig = mdConnectionConfig;
	}

	public static boolean isMDConnectionConfigSet() {
		return mdConnectionConfig != null;
	}

	public static void verifyMDConnectionConfig() throws Exception {
		if (!isMDConnectionConfigSet()) {
			throw new Exception("MiniDAO must be setted");
		}
	}

	// CREATE


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

	public static <T> boolean updateEntities(Class<T> entityClass, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		verifyMDConnectionConfig();
		return updateEntities(mdConnectionConfig, entityClass, mdUpdateFieldList);
	}

	public static <T> boolean updateEntities(Class<T> entityClass, MDCondition mdCondition, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		verifyMDConnectionConfig();
		return updateEntities(mdConnectionConfig, entityClass, mdCondition, mdUpdateFieldList);
	}

	public static <T> boolean updateEntity(Class<T> entityClass, int id, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		verifyMDConnectionConfig();
		return updateEntity(mdConnectionConfig, entityClass, id, mdUpdateFieldList);
	}

	public static <T> boolean updateEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		return MDUpdate.updateEntities(mdConnectionConfig, entityClass, null, mdUpdateFieldList);
	}

	public static <T> boolean updateEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, MDCondition mdCondition, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		return MDUpdate.updateEntities(mdConnectionConfig, entityClass, mdCondition, mdUpdateFieldList);
	}

	public static <T> boolean updateEntity(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, int id, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		return MDUpdate.updateEntity(mdConnectionConfig, entityClass, id, mdUpdateFieldList);
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

	public static <T> boolean deleteEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass) throws Exception {
		return MDDelete.deleteEntities(mdConnectionConfig, entityClass, null);
	}

	public static <T> boolean deleteEntities(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, MDCondition mdCondition) throws Exception {
		return MDDelete.deleteEntities(mdConnectionConfig, entityClass, mdCondition);
	}

	public static <T> boolean deleteEntity(MDConnectionConfig mdConnectionConfig, Class<T> entityClass, int id) throws Exception {
		return MDDelete.deleteEntity(mdConnectionConfig, entityClass, id);
	}
}
