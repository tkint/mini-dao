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

package com.thomaskint.minidao.crud

import com.thomaskint.minidao.enumeration.MDConditionOperator
import com.thomaskint.minidao.exception.MDException
import com.thomaskint.minidao.querybuilder.MDCondition
import com.thomaskint.minidao.testonly.Config
import com.thomaskint.minidao.testonly.model.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Thomas Kint
 */
class MDReadIT {

    private var read: MDRead = MDRead(Config.connectionConfig)

    @Before
    @Throws(Exception::class)
    fun init() {
        Config.initDB()
    }

    @Test
    fun should_return_list_of_users() {
        // GIVEN
        var users: List<UserTest>? = null
        try {
            users = read.getEntities(UserTest::class.java)
            for (user in users) {
                println(user)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail()
        }

        Assert.assertNotNull(users)
    }

    @Test
    fun should_return_one_user() {
        // GIVEN
        var user: UserTest? = null
        try {
            user = read.getEntityById(UserTest::class.java, 1)
            println(user)
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail()
        }

        Assert.assertNotNull(user)
    }

    @Test
    fun should_return_list_of_roles_with_users() {
        // GIVEN
        var roles: List<RoleTest>? = null
        try {
            roles = read.getEntities(RoleTest::class.java)
            for (role in roles) {
                println(role)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail()
        }

        Assert.assertNotNull(roles)
    }

    @Test
    fun should_return_list_of_players() {
        // GIVEN
        var players: List<PlayerTest>? = null
        try {
            players = read.getEntities(PlayerTest::class.java)
            for (player in players) {
                println(player)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Assert.fail()
        }

        Assert.assertNotNull(players)
    }

    @Test
    fun should_return_list_of_messages_with_author() {
        // GIVEN
        var messages: List<MessageTest>? = null
        try {
            messages = read.getEntities(MessageTest::class.java)
            for (message in messages) {
                println(message)
            }
        } catch (e: MDException) {
            e.printStackTrace()
            Assert.fail()
        }

        Assert.assertNotNull(messages)
    }

    @Test
    fun should_return_list_of_messagetypes_with_messages_and_authors() {
        // GIVEN
        var messageTypes: List<MessageTypeTest>? = null
        try {
            messageTypes = read.getEntities(MessageTypeTest::class.java)
            for (messageType in messageTypes) {
                println(messageType)
            }
        } catch (e: MDException) {
            e.printStackTrace()
            Assert.fail()
        }

        Assert.assertNotNull(messageTypes)
    }

    @Test
    fun should_return_list_of_messages_based_on_author_id() {
        // GIVEN
        val condition = MDCondition(MessageTest.idAuthorFieldName, MDConditionOperator.EQUAL, 10)
        var messages: List<MessageTest>? = null
        try {
            messages = read.getEntities(MessageTest::class.java, condition)
            for (message in messages) {
                println(message)
            }
        } catch (e: MDException) {
            e.printStackTrace()
            Assert.fail()
        }

        Assert.assertNotNull(messages)
    }
}
