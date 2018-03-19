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
