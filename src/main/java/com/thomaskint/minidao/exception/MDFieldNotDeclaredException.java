package com.thomaskint.minidao.exception;

public class MDFieldNotDeclaredException extends MDException {

	public MDFieldNotDeclaredException(Class entityClass, String field) {
		super(entityClass.getName() + " has no " + field + " declared as MDField");
	}
}
