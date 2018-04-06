/*
 * MIT License
 *
 * Copyright (c) 2017 Thomas Kint
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.testonly.model.MessageTest;
import com.thomaskint.minidao.testonly.model.MessageTypeTest;
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
		String expectedQuery = "INSERT INTO message (id_author, content) VALUES (1, 'test-content')";
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
		String expectedQuery = "INSERT INTO message (id_author, content) VALUES (1, 'test-content')";
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

	@Test
	public void should_construct_query_with_primary_key_when_insertable() {
		// GIVEN
		String expectedQuery = "INSERT INTO message_type (value) VALUES ('newValue')";
		MDInsertBuilder insertBuilder = new MDInsertBuilder();
		insertBuilder.into(MessageTypeTest.class);
		insertBuilder.set(MessageTypeTest.valueFieldName, "newValue");
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