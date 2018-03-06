package com.thomaskint.minidao.model;

import com.thomaskint.minidao.annotations.MDEntity;
import com.thomaskint.minidao.annotations.MDField;
import com.thomaskint.minidao.annotations.MDId;

import static com.thomaskint.minidao.enumeration.MDParam.SELECT;

/**
 * Created by tkint on 19/01/2018.
 */
@MDEntity(name = "user")
public class UserTest {

	public static final String id_userField = "id_user";

	public static final String pseudoField = "pseudo";

	public static final String loginField = "login";

	@MDId @MDField(name = id_userField, params = SELECT)
	public int id;

	@MDField(name = pseudoField)
	public String pseudo;

	@MDField(name = loginField)
	public String login;

	@MDField(name = "password")
	public String password;
}
