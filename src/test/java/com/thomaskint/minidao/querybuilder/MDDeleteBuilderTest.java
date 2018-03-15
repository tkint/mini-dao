package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.testonly.model.MessageTest;
import com.thomaskint.minidao.testonly.model.UserTest;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;

/**
 * @author Thomas Kint
 */
public class MDDeleteBuilderTest {

	@Test
	public void should_construct_query() {
		// GIVEN
		String expectedQuery = "DELETE FROM message";
		MDDeleteBuilder deleteBuilder = new MDDeleteBuilder();
		deleteBuilder.delete().from(MessageTest.class);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = deleteBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_without_verb() {
		// GIVEN
		String expectedQuery = "DELETE FROM message";
		MDDeleteBuilder deleteBuilder = new MDDeleteBuilder();
		deleteBuilder.from(MessageTest.class);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = deleteBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test(expected = MDException.class)
	public void should_not_construct_query_when_no_entity() throws MDException {
		// GIVEN
		MDDeleteBuilder deleteBuilder = new MDDeleteBuilder();
		// WHEN
		deleteBuilder.build();
		// THEN exception
	}

	@Test
	public void should_construct_query_with_condition() {
		// GIVEN
		String expectedQuery = "DELETE FROM message WHERE message.id_message = '1'";
		MDDeleteBuilder deleteBuilder = new MDDeleteBuilder();
		deleteBuilder.delete().from(MessageTest.class).where("id_message", EQUAL, 1);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = deleteBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_entity() {
		// GIVEN
		String expectedQuery = "DELETE FROM message WHERE message.id_message = '1'";
		UserTest author = new UserTest(BigDecimal.valueOf(1), "pseudo", "login", "password");
		MessageTest message = new MessageTest(BigDecimal.valueOf(1), BigDecimal.valueOf(1), author, "test-content");
		MDDeleteBuilder deleteBuilder = new MDDeleteBuilder();
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = deleteBuilder.delete(message).build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

}