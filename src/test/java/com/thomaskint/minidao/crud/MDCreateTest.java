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

import com.thomaskint.minidao.MiniDAO;
import com.thomaskint.minidao.testonly.Config;
import com.thomaskint.minidao.testonly.model.UserTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Thomas Kint
 */
public class MDCreateTest {

	private MDCreate create;

	@Before
	public void init() throws Exception {
		create = new MDCreate(Config.connectionConfig, MiniDAO.getDefaultDateFormat());
		Config.initDB();
	}

	@Test
	public void should_create_user() {
		String rand = String.valueOf(Math.random() * 100000).substring(0, 5);

		UserTest userTest = new UserTest(1L, "pseudo", "login", "password");

		boolean created = false;
		try {
			created = create.createEntity(userTest);
			System.out.println(userTest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(created);
	}
}
