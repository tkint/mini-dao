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
		String expectedQuery = "UPDATE message SET id_author = 1, content = 'BOB'";
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
		String expectedQuery =
				"UPDATE message SET id_author = 1, content = 'test-content' WHERE message.id_message = 1";
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