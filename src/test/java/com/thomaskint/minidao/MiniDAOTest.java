package com.thomaskint.minidao;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.connection.MDDriver;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.model.MDPair;
import com.thomaskint.minidao.testonly.Config;
import com.thomaskint.minidao.testonly.model.UserTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.mockito.*"})
@PrepareForTest(MDConnection.class)
public class MiniDAOTest {

	private final MDDriver driver = MDDriver.H2;
	private final String url = "url";
	private final String port = "3306";
	private final String login = "login";
	private final String password = "password";
	private final String database = "database";

	private final String QUERY = "query";

	private MiniDAO mockedMiniDAO;
	private UserTest userTest;

	@Before
	public void init() {
		mockedMiniDAO = spy(new MiniDAO(Config.connectionConfig));
		userTest = mock(UserTest.class);
	}

	@Test
	public void should_build_with_connection_config() {
		// GIVEN
		MDConnectionConfig expectedConfig = new MDConnectionConfig(driver, url, port, login, password, database);

		// WHEN
		MiniDAO miniDAO = new MiniDAO(driver, url, port, login, password, database);

		// THEN
		assertThat(miniDAO.getConnectionConfig(), samePropertyValuesAs(expectedConfig));
	}

	@Test
	public void should_build_with_connection_config_without_driver() {
		// GIVEN
		MDDriver driver = MDDriver.DEFAULT;
		MDConnectionConfig expectedConfig = new MDConnectionConfig(driver, url, port, login, password, database);

		// WHEN
		MiniDAO miniDAO = new MiniDAO(url, port, login, password, database);

		// THEN
		assertThat(miniDAO.getConnectionConfig(), samePropertyValuesAs(expectedConfig));
	}

	@Test
	public void should_call_execute_query() throws MDException {
		// GIVEN
		ResultSet expectedResultSet = mock(ResultSet.class);

		PowerMockito.mockStatic(MDConnection.class);

		// WHEN
		when(MDConnection.executeQuery(mockedMiniDAO.getConnectionConfig(), QUERY)).thenReturn(expectedResultSet);
		ResultSet resultSet = mockedMiniDAO.executeQuery(QUERY);

		// THEN
		assertEquals(resultSet, expectedResultSet);
	}

	@Test
	public void should_call_execute_update() throws MDException {
		// GIVEN
		Statement statement = mock(Statement.class);
		MDPair expectedMDPair = new MDPair<>(statement, 0);

		PowerMockito.mockStatic(MDConnection.class);

		// WHEN
		Mockito.when(MDConnection.executeUpdate(mockedMiniDAO.getConnectionConfig(), QUERY, null)).thenReturn(expectedMDPair);
		int value = mockedMiniDAO.executeUpdate(QUERY);

		// THEN
		assertEquals(value, expectedMDPair.getValue());
	}

	@Test
	public void should_call_execute_query_and_map_entity() throws MDException {
		// GIVEN
		ResultSet resultSet = mock(ResultSet.class);

		// WHEN
		doReturn(resultSet).when(mockedMiniDAO).executeQuery(QUERY);
		doReturn(userTest).when(mockedMiniDAO).mapResultSetToEntity(resultSet, UserTest.class);
		UserTest returnedUserTest = mockedMiniDAO.executeQueryAndMapToEntity(QUERY, UserTest.class);

		// THEN
		verify(mockedMiniDAO, times(1)).executeQuery(QUERY);
		verify(mockedMiniDAO, times(1)).mapResultSetToEntity(resultSet, UserTest.class);
		assertEquals(returnedUserTest, userTest);
	}

	@Test
	public void should_call_execute_query_and_map_entities() throws MDException {
		// GIVEN
		ResultSet resultSet = mock(ResultSet.class);
		List<UserTest> expectedUserTests = Collections.singletonList(userTest);

		// WHEN
		doReturn(resultSet).when(mockedMiniDAO).executeQuery(QUERY);
		doReturn(expectedUserTests).when(mockedMiniDAO).mapResultSetToEntities(resultSet, UserTest.class);
		List<UserTest> returnedUserTests = mockedMiniDAO.executeQueryAndMapToEntities(QUERY, UserTest.class);

		// THEN
		verify(mockedMiniDAO, times(1)).executeQuery(QUERY);
		verify(mockedMiniDAO, times(1)).mapResultSetToEntities(resultSet, UserTest.class);
		assertEquals(returnedUserTests, expectedUserTests);
	}
}