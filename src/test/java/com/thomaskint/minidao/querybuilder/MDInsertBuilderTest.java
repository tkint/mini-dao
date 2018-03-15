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
public class MDInsertBuilderTest {

	@Test
	public void should_construct_query() {
		// GIVEN
		String expectedQuery = "INSERT INTO message SET id_author = '1', content = 'test-content'";
		UserTest author = new UserTest(BigDecimal.valueOf(1), "pseudo", "login", "password");
		MessageTest message = new MessageTest(BigDecimal.valueOf(1), BigDecimal.valueOf(1), author, "test-content");
		MDInsertBuilder insertBuilder = new MDInsertBuilder();
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = insertBuilder.insert(message).build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_bis() {
		// GIVEN
		String expectedQuery = "INSERT INTO message SET id_author = '1', content = 'test-content'";
		MDInsertBuilder insertBuilder = new MDInsertBuilder();
		insertBuilder.into(MessageTest.class);
		insertBuilder.set("id_author", 1);
		insertBuilder.set("content", "test-content");
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = insertBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}
}