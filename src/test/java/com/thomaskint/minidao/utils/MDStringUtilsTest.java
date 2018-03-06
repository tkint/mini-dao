package com.thomaskint.minidao.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MDStringUtilsTest {

	@Test public void firstLetterToUpperCase() throws Exception {
		// GIVEN
		String string = "hello world";
		String expectedString = "Hello world";
		// WHEN
		String returnedString = MDStringUtils.firstLetterToUpperCase(string);
		// THEN
		assertEquals(returnedString, expectedString);
	}
}
