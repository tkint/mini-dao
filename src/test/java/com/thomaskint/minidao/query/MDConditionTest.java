package com.thomaskint.minidao.query;

import com.thomaskint.minidao.model.UserTest;
import org.junit.Test;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.model.UserTest.id_user;
import static org.junit.Assert.assertEquals;


/**
 * Created by tkint on 24/01/2018.
 */
public class MDConditionTest {

	@Test
	public void test_simple_condition() {
		// GIVEN
		String expectedCondition = " WHERE " + id_user + " " + EQUAL.getValue() + " '" + 1 + "'";
		MDCondition mdCondition = new MDCondition(id_user, 1);
		// WHEN
		String returnedCondition = null;
		try {
			returnedCondition = mdCondition.build(UserTest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// THEN
		assertEquals(expectedCondition, returnedCondition);
	}
}
