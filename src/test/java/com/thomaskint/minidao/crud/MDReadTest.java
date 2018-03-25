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

package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.testonly.model.MessageTest;
import com.thomaskint.minidao.testonly.model.MessageTypeTest;
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

	private MDRead read;

	@Before
	public void init() {
		read = new MDRead(mdConnectionConfig);
	}

	@Test
	public void should_return_list_of_users() {
		// GIVEN
		List<UserTest> users;
		try {
			users = read.getEntities(UserTest.class);
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
			players = read.getEntities(PlayerTest.class);
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
			messages = read.getEntities(MessageTest.class);
			for (MessageTest message : messages) {
				System.out.println(message);
			}
		} catch (MDException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}
		Assert.assertTrue(true);
	}

	@Test
	public void should_return_list_of_messagetypes_with_messages_and_authors() {
		// GIVEN
		List<MessageTypeTest> messageTypes;
		try {
			messageTypes = read.getEntities(MessageTypeTest.class);
			for (MessageTypeTest messageType : messageTypes) {
				System.out.println(messageType);
			}
		} catch (MDException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}
		Assert.assertTrue(true);
	}
}
