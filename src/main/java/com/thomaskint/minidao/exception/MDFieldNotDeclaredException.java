package com.thomaskint.minidao.exception;

/**
 * @author Thomas Kint
 */
public class MDFieldNotDeclaredException extends MDException {

	public MDFieldNotDeclaredException(Class entityClass, String field) {
		super(entityClass.getName() + " has no " + field + " declared as MDField");
	}
}
