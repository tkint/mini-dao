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

import com.thomaskint.minidao.exception.MDException
import com.thomaskint.minidao.testonly.model.MessageTest
import com.thomaskint.minidao.testonly.model.UserTest
import org.junit.Assert
import org.junit.Test

import java.math.BigDecimal

import com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL

/**
 * @author Thomas Kint
 */
class MDDeleteBuilderTest {

    @Test
    fun should_construct_query() {
        // GIVEN
        val expectedQuery = "DELETE FROM message"
        val deleteBuilder = MDDeleteBuilder()
        deleteBuilder.delete().from(MessageTest::class.java)
        // WHEN
        var returnedQuery: String? = null
        try {
            returnedQuery = deleteBuilder.build()
        } catch (e: MDException) {
            e.printStackTrace()
        }

        // THEN
        Assert.assertEquals(expectedQuery, returnedQuery)
    }

    @Test
    fun should_construct_query_without_verb() {
        // GIVEN
        val expectedQuery = "DELETE FROM message"
        val deleteBuilder = MDDeleteBuilder()
        deleteBuilder.from(MessageTest::class.java)
        // WHEN
        var returnedQuery: String? = null
        try {
            returnedQuery = deleteBuilder.build()
        } catch (e: MDException) {
            e.printStackTrace()
        }

        // THEN
        Assert.assertEquals(expectedQuery, returnedQuery)
    }

    @Test(expected = MDException::class)
    @Throws(MDException::class)
    fun should_not_construct_query_when_no_entity() {
        // GIVEN
        val deleteBuilder = MDDeleteBuilder()
        // WHEN
        deleteBuilder.build()
        // THEN exception
    }

    @Test
    fun should_construct_query_with_condition() {
        // GIVEN
        val expectedQuery = "DELETE FROM message WHERE message.id_message = 1"
        val deleteBuilder = MDDeleteBuilder()
        deleteBuilder.delete().from(MessageTest::class.java).where("id_message", EQUAL, 1)
        // WHEN
        var returnedQuery: String? = null
        try {
            returnedQuery = deleteBuilder.build()
        } catch (e: MDException) {
            e.printStackTrace()
        }

        // THEN
        Assert.assertEquals(expectedQuery, returnedQuery)
    }

    @Test
    fun should_construct_query_with_entity() {
        // GIVEN
        val expectedQuery = "DELETE FROM message WHERE message.id_message = 1"
        val author = UserTest(1L, "pseudo", "login", "password")
        val message = MessageTest(1L, 1L, author, "test-content")
        val deleteBuilder = MDDeleteBuilder()
        // WHEN
        var returnedQuery: String? = null
        try {
            returnedQuery = deleteBuilder.delete(message).build()
        } catch (e: MDException) {
            e.printStackTrace()
        }

        // THEN
        Assert.assertEquals(expectedQuery, returnedQuery)
    }

}