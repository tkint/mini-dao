package com.thomaskint.minidao.testonly.model;

import com.thomaskint.minidao.annotation.MDEntity;
import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDId;
import com.thomaskint.minidao.annotation.MDOneToMany;

import java.util.List;

import static com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY;

/**
 * @author Thomas Kint
 */
@MDEntity(tableName = "role")
public class RoleTest {

	public static final String valueFieldName = "value";

	@MDId
	@MDField(fieldName = valueFieldName)
	public String value;

	@MDOneToMany(fieldName = valueFieldName, targetFieldName = UserTest.roleFieldName, target = UserTest.class, loadPolicy = HEAVY)
	public List<UserTest> users;

	@Override
	public String toString() {
		return "RoleTest{" +
				"value='" + value + '\'' +
				", users=" + users +
				'}';
	}
}
