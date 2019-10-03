package com.thomaskint.minidao

import com.thomaskint.minidao.connection.MDConnection
import com.thomaskint.minidao.connection.MDConnectionConfig
import com.thomaskint.minidao.connection.MDDriver
import com.thomaskint.minidao.model.MDPair
import com.thomaskint.minidao.testonly.Config
import com.thomaskint.minidao.testonly.model.UserTest
import io.mockk.*
import org.junit.Test

import java.sql.ResultSet
import java.sql.Statement

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.samePropertyValuesAs
import org.junit.Assert.assertEquals


class MiniDAOTest {

    private val driver = MDDriver.H2
    private val url = "url"
    private val port = "3306"
    private val login = "login"
    private val password = "password"
    private val database = "database"

    private val QUERY = "query"

    private var miniDAO = MiniDAO(Config.connectionConfig)
    private var userTest = mockk<UserTest>()

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
    fun should_call_execute_query() {
        // GIVEN
        val expectedResultSet = mockk<ResultSet>()

        mockkStatic(MDConnection::class)

        // WHEN
        every { MDConnection.executeQuery(miniDAO.connectionConfig, QUERY) } returns expectedResultSet
        val resultSet = miniDAO.executeQuery(QUERY)

        // THEN
        verify { MDConnection.executeQuery(miniDAO.connectionConfig, QUERY) }
        assertEquals(resultSet, expectedResultSet)
    }

    @Test
    fun should_call_execute_update() {
        // GIVEN
        val statement = mockk<Statement>()
        val expectedMDPair: MDPair<Statement?, Int?> = MDPair(statement, 0)

        mockkStatic(MDConnection::class)

        // WHEN
        every { MDConnection.executeUpdate(miniDAO.connectionConfig, QUERY, null) } returns expectedMDPair
        val value = miniDAO.executeUpdate(QUERY)

        // THEN
        verify { MDConnection.executeUpdate(miniDAO.connectionConfig, QUERY, null) }
        assertEquals(value, expectedMDPair.value)
    }

    @Test
    fun should_call_execute_query_and_map_entity() {
        // GIVEN
        val resultSet = mockk<ResultSet>()
        val spiedMiniDAO = spyk(miniDAO)

        // WHEN
        every { spiedMiniDAO.executeQuery(QUERY) } returns resultSet
        every { spiedMiniDAO.mapResultSetToEntity(resultSet, UserTest::class.java) } returns userTest
        val returnedUserTest = spiedMiniDAO.executeQueryAndMapToEntity(QUERY, UserTest::class.java)

        // THEN
        verify { spiedMiniDAO.executeQuery(QUERY) }
        verify { spiedMiniDAO.mapResultSetToEntity(resultSet, UserTest::class.java) }
        assertEquals(returnedUserTest, userTest)
    }

    @Test
    fun should_call_execute_query_and_map_entities() {
        // GIVEN
        val resultSet = mockk<ResultSet>()
        val spiedMiniDAO = spyk(miniDAO)
        val userTests = listOf(userTest)

        // WHEN
        every { spiedMiniDAO.executeQuery(QUERY) } returns resultSet
        every { spiedMiniDAO.mapResultSetToEntities(resultSet, UserTest::class.java) } returns userTests
        val returnedUserTests = spiedMiniDAO.executeQueryAndMapToEntities(QUERY, UserTest::class.java)

        // THEN
        verify { spiedMiniDAO.executeQuery(QUERY) }
        verify { spiedMiniDAO.mapResultSetToEntities(resultSet, UserTest::class.java) }
        assertEquals(returnedUserTests, userTests)
    }
}