package com.thomaskint.minidao;

import com.thomaskint.minidao.query.*;
import com.thomaskint.minidao.query.delete.MDDelete;
import com.thomaskint.minidao.query.read.MDRead;
import com.thomaskint.minidao.query.update.MDUpdate;
import com.thomaskint.minidao.query.update.MDUpdateFieldList;

import java.util.List;

/**
 * Created by tkint on 19/01/2018.
 */
public abstract class MiniDAO {

	// CREATE


	// READ

	public static <T> List<T> getEntities(Class<T> entityClass) throws Exception {
		return MDRead.getEntities(entityClass, null);
	}

	public static <T> List<T> getEntities(Class<T> entityClass, MDCondition mdCondition) throws Exception {
		return MDRead.getEntities(entityClass, mdCondition);
	}

	public static <T> T getEntityById(Class<T> entityClass, int id) throws Exception {
		return MDRead.getEntityById(entityClass, id);
	}

	// UPDATE

	public static <T> boolean updateEntities(Class<T> entityClass, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		return MDUpdate.updateEntities(entityClass, null, mdUpdateFieldList);
	}

	public static <T> boolean updateEntities(Class<T> entityClass, MDCondition mdCondition, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		return MDUpdate.updateEntities(entityClass, mdCondition, mdUpdateFieldList);
	}

	public static <T> boolean updateEntity(Class<T> entityClass, int id, MDUpdateFieldList mdUpdateFieldList) throws Exception {
		return MDUpdate.updateEntity(entityClass, id, mdUpdateFieldList);
	}

	// DELETE

	public static <T> boolean deleteEntities(Class<T> entityClass) throws Exception {
		return MDDelete.deleteEntities(entityClass, null);
	}

	public static <T> boolean deleteEntities(Class<T> entityClass, MDCondition mdCondition) throws Exception {
		return MDDelete.deleteEntities(entityClass, mdCondition);
	}

	public static <T> boolean deleteEntity(Class<T> entityClass, int id) throws Exception {
		return MDDelete.deleteEntity(entityClass, id);
	}
}
