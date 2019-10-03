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

import com.thomaskint.minidao.enumeration.MDConditionOperator;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.querybuilder.MDCondition;
import com.thomaskint.minidao.testonly.Config;
import com.thomaskint.minidao.testonly.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Thomas Kint
 */
public class MDReadIT {

	private MDRead read;

	@Before
	public void init() throws Exception {
		read = new MDRead(Config.getConnectionConfig());
		Config.initDB();
	}

	@Test
	public void should_return_list_of_users() {
		// GIVEN
		List<UserTest> users = null;
		try {
			users = read.getEntities(UserTest.class);
			for (UserTest user : users) {
				System.out.println(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(users);
	}

	@Test
	public void should_return_one_user() {
		// GIVEN
		UserTest user = null;
		try {
			user = read.getEntityById(UserTest.class, 1);
			System.out.println(user);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(user);
	}

	@Test
	public void should_return_list_of_roles_with_users() {
		// GIVEN
		List<RoleTest> roles = null;
		try {
			roles = read.getEntities(RoleTest.class);
			for (RoleTest role : roles) {
				System.out.println(role);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(roles);
	}

	@Test
	public void should_return_list_of_players() {
		// GIVEN
		List<PlayerTest> players = null;
		try {
			players = read.getEntities(PlayerTest.class);
			for (PlayerTest player : players) {
				System.out.println(player);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(players);
	}

	@Test
	public void should_return_list_of_messages_with_author() {
		// GIVEN
		List<MessageTest> messages = null;
		try {
			messages = read.getEntities(MessageTest.class);
			for (MessageTest message : messages) {
				System.out.println(message);
			}
		} catch (MDException e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(messages);
	}

	@Test
	public void should_return_list_of_messagetypes_with_messages_and_authors() {
		// GIVEN
		List<MessageTypeTest> messageTypes = null;
		try {
			messageTypes = read.getEntities(MessageTypeTest.class);
			for (MessageTypeTest messageType : messageTypes) {
				System.out.println(messageType);
			}
		} catch (MDException e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(messageTypes);
	}

	@Test
	public void should_return_list_of_messages_based_on_author_id() {
		// GIVEN
		MDCondition condition = new MDCondition(MessageTest.idAuthorFieldName, MDConditionOperator.EQUAL, 10);
		List<MessageTest> messages = null;
		try {
			messages = read.getEntities(MessageTest.class, condition);
			for (MessageTest message : messages) {
				System.out.println(message);
			}
		} catch (MDException e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(messages);
	}
}
