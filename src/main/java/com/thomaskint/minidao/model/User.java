package com.thomaskint.minidao.model;

import com.thomaskint.minidao.annotations.MDEntity;
import com.thomaskint.minidao.annotations.MDField;
import com.thomaskint.minidao.annotations.MDId;

import static com.thomaskint.minidao.enumeration.MDParam.SELECT;

/**
 * Created by tkint on 19/01/2018.
 */
@MDEntity(name = "user")
public class User {

	@MDId
	@MDField(name = "id_user", params = SELECT)
	public int id;

	@MDField(name = "pseudo")
	public String pseudo;

	@MDField(name = "login")
	public String login;

	@MDField(name = "password")
	public String password;

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", pseudo='" + pseudo + '\'' +
				", login='" + login + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
