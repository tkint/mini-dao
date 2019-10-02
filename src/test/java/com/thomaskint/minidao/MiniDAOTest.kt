package com.thomaskint.minidao

import com.thomaskint.minidao.connection.MDConnection
import com.thomaskint.minidao.connection.MDConnectionConfig
import com.thomaskint.minidao.connection.MDDriver
import com.thomaskint.minidao.exception.MDException
import com.thomaskint.minidao.testonly.model.UserTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

import java.sql.ResultSet

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.samePropertyValuesAs
import org.junit.Assert.assertEquals
import org.powermock.api.mockito.PowerMockito.*


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
        verifyStatic(MDConnection::class.java)
        assertEquals(resultSet, expectedResultSet)
    }

    @Test
    @Throws(MDException::class)
    fun should_call_execute_query_and_map_entity() {
        // GIVEN
        val miniDAO = MiniDAO(url, port, login, password, database)
        val expectedResultSet = mock(ResultSet::class.java)

        val mockedMiniDAO = spy(miniDAO)
        mockStatic(MDConnection::class.java)

        // WHEN
        `when`(MDConnection.executeQuery(mockedMiniDAO.connectionConfig, query)).thenReturn(expectedResultSet)
        doReturn(this.userTest).`when`(mockedMiniDAO).mapResultSetToEntity(expectedResultSet, UserTest::class.java)
        val userTest = mockedMiniDAO.executeQueryAndMapToEntity(query, UserTest::class.java)

        // THEN
        verifyStatic(MDConnection::class.java)
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
        mockStatic(MDConnection::class.java)

        // WHEN
        `when`(MDConnection.executeQuery(mockedMiniDAO.connectionConfig, query)).thenReturn(expectedResultSet)
        doReturn(expectedUserTests).`when`(mockedMiniDAO).mapResultSetToEntities(expectedResultSet, UserTest::class.java)
        val userTests = mockedMiniDAO.executeQueryAndMapToEntities(query, UserTest::class.java)

        // THEN
        verifyStatic(MDConnection::class.java)
        assertEquals(userTests, expectedUserTests)
    }
}