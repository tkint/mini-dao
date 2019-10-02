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

package com.thomaskint.minidao

import com.thomaskint.minidao.connection.MDConnection
import com.thomaskint.minidao.connection.MDConnectionConfig
import com.thomaskint.minidao.connection.MDDriver
import com.thomaskint.minidao.exception.MDException
import com.thomaskint.minidao.model.MDPair
import com.thomaskint.minidao.testonly.model.UserTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

import java.sql.ResultSet

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.samePropertyValuesAs
import org.junit.Assert.assertEquals
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito.*
import java.sql.Statement


@RunWith(PowerMockRunner::class)
@PowerMockIgnore("org.mockito.*")
@PrepareForTest(MDConnection::class)
class MiniDAOTest {

    private val driver = MDDriver.H2
    private val url = "url"
    private val port = "3306"
    private val login = "login"
    private val password = "password"
    private val database = "database"

    private val query = "query"

    private var userTest: UserTest? = null

    @Before
    fun init() {
        userTest = mock(UserTest::class.java)
    }

    @Test
    fun should_build_with_connection_config() {
        // GIVEN
        val expectedConfig = MDConnectionConfig(driver, url, port, login, password, database)

        // WHEN
        val miniDAO = MiniDAO(driver, url, port, login, password, database)

        // THEN
        assertThat(miniDAO.connectionConfig, samePropertyValuesAs(expectedConfig))
    }

    @Test
    fun should_build_with_connection_config_without_driver() {
        // GIVEN
        val driver = MDDriver.DEFAULT
        val expectedConfig = MDConnectionConfig(driver, url, port, login, password, database)

        // WHEN
        val miniDAO = MiniDAO(url, port, login, password, database)

        // THEN
        assertThat(miniDAO.connectionConfig, samePropertyValuesAs(expectedConfig))
    }

    @Test
    @Throws(MDException::class)
    fun should_call_execute_query() {
        // GIVEN
        val miniDAO = MiniDAO(url, port, login, password, database)
        val expectedResultSet = mock(ResultSet::class.java)

        mockStatic(MDConnection::class.java)

        // WHEN
        `when`(MDConnection.executeQuery(miniDAO.connectionConfig, query)).thenReturn(expectedResultSet)
        val resultSet = miniDAO.executeQuery(query)

        // THEN
        assertEquals(resultSet, expectedResultSet)
    }

    @Test
    @Throws(MDException::class)
    fun should_call_execute_update() {
        // GIVEN
        val miniDAO = MiniDAO(url, port, login, password, database)

        val statement = mock(Statement::class.java)
        val expectedMDPair = MDPair(statement, 0)

        mockStatic(MDConnection::class.java)

        // WHEN
        `when`(MDConnection.executeUpdate(miniDAO.connectionConfig, query, null)).thenReturn(expectedMDPair)
        val value = miniDAO.executeUpdate(query)

        // THEN
        assertEquals(value, expectedMDPair.value)
    }

    @Test
    @Throws(MDException::class)
    fun should_call_execute_query_and_map_entity() {
        // GIVEN
        val miniDAO = MiniDAO(url, port, login, password, database)
        val expectedResultSet = mock(ResultSet::class.java)

        val mockedMiniDAO = spy(miniDAO)

        // WHEN
        doReturn(expectedResultSet).`when`(mockedMiniDAO).executeQuery(query)
        doReturn(this.userTest).`when`(mockedMiniDAO).mapResultSetToEntity(expectedResultSet, UserTest::class.java)
        val userTest = mockedMiniDAO.executeQueryAndMapToEntity(query, UserTest::class.java)

        // THEN
        verify(mockedMiniDAO, times(1)).executeQuery(query)
        verify(mockedMiniDAO, times(1)).mapResultSetToEntity(expectedResultSet, UserTest::class.java)
        assertEquals(userTest, this.userTest)
    }

    @Test
    @Throws(MDException::class)
    fun should_call_execute_query_and_map_entities() {
        // GIVEN
        val miniDAO = MiniDAO(url, port, login, password, database)
        val expectedResultSet = mock(ResultSet::class.java)
        val expectedUserTests = listOf(this.userTest)

        val mockedMiniDAO = Mockito.spy(miniDAO)

        // WHEN
        doReturn(expectedResultSet).`when`(mockedMiniDAO).executeQuery(query)
        doReturn(expectedUserTests).`when`(mockedMiniDAO).mapResultSetToEntities(expectedResultSet, UserTest::class.java)
        val userTests = mockedMiniDAO.executeQueryAndMapToEntities(query, UserTest::class.java)

        // THEN
        verify(mockedMiniDAO, times(1)).executeQuery(query)
        verify(mockedMiniDAO, times(1)).mapResultSetToEntities(expectedResultSet, UserTest::class.java)
        assertEquals(userTests, expectedUserTests)
    }
}