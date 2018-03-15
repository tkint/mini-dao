package com.thomaskint.minidao.exception;

/**
 * @author Thomas Kint
 */
public class MDException extends Exception {

	public MDException(String message) {
		super(message);
	}

	public MDException(Exception exception) {
		super(exception);
	}
}
