package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.MiniDAO;
import com.thomaskint.minidao.testonly.model.UserTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.thomaskint.minidao.testonly.Config.mdConnectionConfig;

/**
 * @author Thomas Kint
 */
public class MDCreateTest {

	private MiniDAO miniDAO;

	@Before
	public void init() {
		miniDAO = new MiniDAO(mdConnectionConfig);
	}

	@Test
	public void should_create_user() {
		String rand = String.valueOf(Math.random() * 100000).substring(0, 5);

		UserTest userTest = new UserTest(BigDecimal.valueOf(1), "pseudo", "login", "password");

		boolean created = false;
		try {
			created = miniDAO.create().createEntity(userTest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(created);
	}
}
