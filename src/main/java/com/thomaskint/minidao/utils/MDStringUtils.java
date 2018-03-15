/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskint.minidao.utils;

/**
 * @author Thomas Kint
 */
public class MDStringUtils {

	public static final String EMPTY = "";

	public static final String SPACE = " ";

	public static final String COMMA = ",";

	public static final String QUOTE = "'";

	public static final String DOUBLE_QUOTE = "\"";

	public static final String LEFT_PARENTHESIS = "(";

	public static final String RIGHT_PARENTHESIS = ")";

	public static String firstLetterToUpperCase(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
}
