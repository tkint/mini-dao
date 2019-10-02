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

package com.thomaskint.minidao.querybuilder

import com.thomaskint.minidao.MiniDAO
import com.thomaskint.minidao.exception.MDException
import com.thomaskint.minidao.testonly.model.MessageTest
import com.thomaskint.minidao.testonly.model.MessageTypeTest
import com.thomaskint.minidao.testonly.model.UserTest
import org.junit.Assert
import org.junit.Test

import java.math.BigDecimal

/**
 * @author Thomas Kint
 */
class MDInsertBuilderTest {

    @Test
    fun should_construct_query() {
        // GIVEN
        val expectedQuery = "INSERT INTO message (id_author, content) VALUES (1, 'test-content')"
        val author = UserTest(1L, "pseudo", "login", "password")
        val message = MessageTest(1L, 1L, author, "test-content")
        val insertBuilder = MDInsertBuilder(MiniDAO.getDefaultDateFormat())
        // WHEN
        var returnedQuery: String? = null
        try {
            returnedQuery = insertBuilder.insert(message).build()
        } catch (e: MDException) {
            e.printStackTrace()
        }

        // THEN
        Assert.assertEquals(expectedQuery, returnedQuery)
    }

    @Test
    fun should_construct_query_bis() {
        // GIVEN
        val expectedQuery = "INSERT INTO message (id_author, content) VALUES (1, 'test-content')"
        val insertBuilder = MDInsertBuilder(MiniDAO.getDefaultDateFormat())
        insertBuilder.into(MessageTest::class.java)
        insertBuilder.set("id_author", 1)
        insertBuilder.set("content", "test-content")
        // WHEN
        var returnedQuery: String? = null
        try {
            returnedQuery = insertBuilder.build()
        } catch (e: MDException) {
            e.printStackTrace()
        }

        // THEN
        Assert.assertEquals(expectedQuery, returnedQuery)
    }

    @Test
    fun should_construct_query_with_primary_key_when_insertable() {
        // GIVEN
        val expectedQuery = "INSERT INTO message_type (value) VALUES ('newValue')"
        val insertBuilder = MDInsertBuilder(MiniDAO.getDefaultDateFormat())
        insertBuilder.into(MessageTypeTest::class.java)
        insertBuilder.set(MessageTypeTest.valueFieldName, "newValue")
        // WHEN
        var returnedQuery: String? = null
        try {
            returnedQuery = insertBuilder.build()
        } catch (e: MDException) {
            e.printStackTrace()
        }

        // THEN
        Assert.assertEquals(expectedQuery, returnedQuery)
    }
}