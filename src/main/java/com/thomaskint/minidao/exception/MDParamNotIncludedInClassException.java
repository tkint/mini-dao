package com.thomaskint.minidao.exception;

import com.thomaskint.minidao.enumeration.MDVerb;

/**
 * @author Thomas Kint
 */
public class MDParamNotIncludedInClassException extends MDException {

	public MDParamNotIncludedInClassException(Class entityClass, MDVerb param) {
		super(entityClass.getName() + " does not permit " + param);
	}
}
