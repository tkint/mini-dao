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

package com.thomaskint.minidao.testonly.model;

import com.thomaskint.minidao.annotation.MDEntity;
import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDId;
import com.thomaskint.minidao.annotation.MDOneToMany;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY;
import static com.thomaskint.minidao.enumeration.MDSQLAction.SELECT;
import static com.thomaskint.minidao.testonly.model.MessageTest.idAuthorFieldName;

/**
 * @author Thomas Kint
 */
@MDEntity(tableName = "user")
public class UserTest {

	public static final String idUserFieldName = "id_user";

	public static final String pseudoFieldName = "pseudo";

	public static final String loginFieldName = "login";

	@MDId
	@MDField(fieldName = idUserFieldName, allowedSQLActions = SELECT)
	public BigDecimal id;

	@MDField(fieldName = pseudoFieldName)
	public String pseudo;

	@MDField(fieldName = loginFieldName)
	public String login;

	@MDField(fieldName = "password")
	public String password;

	@MDOneToMany(fieldName = idUserFieldName, targetFieldName = idAuthorFieldName, target = MessageTest.class, loadPolicy = HEAVY)
	public List<MessageTest> messages;

	public UserTest() {
	}

	public UserTest(BigDecimal id, String pseudo, String login, String password) {
		this.id = id;
		this.pseudo = pseudo;
		this.login = login;
		this.password = password;
		this.messages = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "UserTest{" +
				"id=" + id +
				", pseudo='" + pseudo + '\'' +
				", login='" + login + '\'' +
				", password='" + password + '\'' +
				", messages=" + messages +
				'}';
	}
}
