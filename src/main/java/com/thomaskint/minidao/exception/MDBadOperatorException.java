package com.thomaskint.minidao.exception;

import com.thomaskint.minidao.enumeration.MDConditionOperator;

/**
 * @author Thomas Kint
 */
public class MDBadOperatorException extends MDException {

	public MDBadOperatorException(MDConditionOperator mdConditionOperator) {
		super(mdConditionOperator + " is not a valid operator in this context");
	}
}
