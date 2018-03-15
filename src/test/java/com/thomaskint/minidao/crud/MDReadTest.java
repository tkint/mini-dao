package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.testonly.model.MessageTest;
import com.thomaskint.minidao.testonly.model.PlayerTest;
import com.thomaskint.minidao.testonly.model.UserTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.thomaskint.minidao.testonly.Config.mdConnectionConfig;

/**
 * @author Thomas Kint
 */
public class MDReadTest {

	private MDRead mdRead;

	@Before
	public void init() {
		mdRead = new MDRead(mdConnectionConfig);
	}

	@Test
	public void should_return_list_of_users() {
		// GIVEN
		List<UserTest> users;
		try {
			users = mdRead.getEntities(UserTest.class);
			for (UserTest user : users) {
				System.out.println(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}
		Assert.assertTrue(true);
	}

	@Test
	public void should_return_list_of_players() {
		// GIVEN
		List<PlayerTest> players;
		try {
			players = mdRead.getEntities(PlayerTest.class);
			for (PlayerTest player : players) {
				System.out.println(player);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}
		Assert.assertTrue(true);
	}

	@Test
	public void should_return_list_of_messages_with_author() {
		// GIVEN
		List<MessageTest> messages;
		try {
			messages = mdRead.getEntities(MessageTest.class);
			for (MessageTest message : messages) {
				System.out.println(message);
			}
		} catch (MDException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}
		Assert.assertTrue(true);
	}
}
