package com.thomaskint.minidao.query;

import com.thomaskint.minidao.model.UserTest;
import org.junit.Before;
import org.junit.Test;

import static com.thomaskint.minidao.enumeration.MDConditionLink.AND;
import static com.thomaskint.minidao.enumeration.MDConditionOperator.DIFFERENT;
import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDConditionOperator.STRICTLY_SUPERIOR;
import static com.thomaskint.minidao.model.UserTest.loginField;
import static com.thomaskint.minidao.model.UserTest.pseudoField;
import static org.junit.Assert.assertEquals;

/**
 * Created by tkint on 24/01/2018.
 */
public class MDConditionTest {

	private static final int ID_USER = 1;

	private static final String PSEUDO = "pseudo_test";

	private static final String LOGIN = "login_test";

	private MDCondition mdCondition1 = new MDCondition(pseudoField, PSEUDO);

	private MDCondition mdCondition2 = new MDCondition(loginField, DIFFERENT, LOGIN);

	private String mdCondition1PartString = pseudoField + " " + EQUAL.getValue() + " '" + PSEUDO + "'";

	private String mdCondition2PartString = loginField + " " + DIFFERENT.getValue() + " '" + LOGIN + "'";

	@Before public void init() {

	}

	@Test public void test_simple_condition_build() {
		// GIVEN
		String expectedConditionString = " WHERE " + mdCondition1PartString;
		// WHEN
		String returnedConditionString = null;
		try {
			returnedConditionString = mdCondition1.build(UserTest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// THEN
		assertEquals(expectedConditionString, returnedConditionString);
	}

	@Test public void test_simple_null_condition_build() {
		// GIVEN
		String expectedConditionString = " WHERE " + pseudoField + " IS NULL";
		MDCondition mdCondition = new MDCondition(pseudoField, EQUAL, null);
		// WHEN
		String returnedConditionString = null;
		try {
			returnedConditionString = mdCondition.build(UserTest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// THEN
		assertEquals(expectedConditionString, returnedConditionString);
	}

	@Test public void test_simple_not_null_condition_build() {
		// GIVEN
		String expectedConditionString = " WHERE " + pseudoField + " IS NOT NULL";
		MDCondition mdCondition = new MDCondition(pseudoField, DIFFERENT, null);
		// WHEN
		String returnedConditionString = null;
		try {
			returnedConditionString = mdCondition.build(UserTest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// THEN
		assertEquals(expectedConditionString, returnedConditionString);
	}

	@Test(expected = Exception.class) public void test_simple_null_condition_build_fail() throws Exception {
		// GIVEN
		MDCondition mdCondition = new MDCondition(pseudoField, STRICTLY_SUPERIOR, null);
		// WHEN
		mdCondition.build(UserTest.class);
		// THEN
	}

	@Test public void test_nested_condition_build() {
		// GIVEN
		String expectedCondition =
				" WHERE " + mdCondition1PartString + " " + AND.getValue() + " " + mdCondition2PartString;
		MDCondition mdCondition = new MDCondition(mdCondition1, AND, mdCondition2);
		// WHEN
		String returnedConditionString = null;
		try {
			returnedConditionString = mdCondition.build(UserTest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// THEN
		assertEquals(expectedCondition, returnedConditionString);
	}

	@Test public void test_nested_isolated_condition_build() {
		// GIVEN
		String expectedCondition =
				" WHERE (" + mdCondition1PartString + " " + AND.getValue() + " " + mdCondition2PartString + ")";
		MDCondition mdCondition = new MDCondition(mdCondition1, AND, mdCondition2, true);
		// WHEN
		String returnedConditionString = null;
		try {
			returnedConditionString = mdCondition.build(UserTest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// THEN
		assertEquals(expectedCondition, returnedConditionString);
	}
}
