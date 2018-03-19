package com.thomaskint.minidao.exception;

import com.thomaskint.minidao.enumeration.MDSQLAction;

/**
 * @author Thomas Kint
 */
public class MDParamNotIncludedInClassException extends MDException {

	public MDParamNotIncludedInClassException(Class entityClass, MDSQLAction param) {
		super(entityClass.getName() + " does not permit " + param);
	}
}
