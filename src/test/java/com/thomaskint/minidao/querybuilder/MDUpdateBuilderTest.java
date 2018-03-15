package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.testonly.model.MessageTest;
import com.thomaskint.minidao.testonly.model.UserTest;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author Thomas Kint
 */
public class MDUpdateBuilderTest {

	@Test
	public void should_construct_query() {
		// GIVEN
		String expectedQuery = "UPDATE message ('id_author', 'content') VALUES ('1', 'BOB')";
		MDUpdateBuilder updateBuilder = new MDUpdateBuilder();
		updateBuilder.update(MessageTest.class);
		updateBuilder.set("id_author", 1);
		updateBuilder.set("content", "BOB");
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = updateBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_bis() {
		// GIVEN
		String expectedQuery = "UPDATE message ('id_author', 'content') VALUES ('1', 'test-content') WHERE message.id_message = '1'";
		UserTest author = new UserTest(BigDecimal.valueOf(1), "pseudo", "login", "password");
		MessageTest message = new MessageTest(BigDecimal.valueOf(1), BigDecimal.valueOf(1), author, "test-content");
		MDUpdateBuilder updateBuilder = new MDUpdateBuilder();
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = updateBuilder.update(message).build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}
}