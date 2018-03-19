package com.thomaskint.minidao.exception;

/**
 * @author Thomas Kint
 */
public class MDNotAnMDEntityException extends MDException {

	public MDNotAnMDEntityException(Class entityClass) {
		super(entityClass.getName() + " is not an MDEntity");
	}
}
