package com.thomaskint.minidao.testonly.model;

import com.thomaskint.minidao.annotation.MDEntity;
import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDId;
import com.thomaskint.minidao.annotation.MDManyToOne;

import java.math.BigDecimal;

import static com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY;
import static com.thomaskint.minidao.enumeration.MDVerb.SELECT;
import static com.thomaskint.minidao.testonly.model.UserTest.idUserFieldName;

/**
 * @author Thomas Kint
 */
@MDEntity(tableName = "message")
public class MessageTest {

	public static final String idAuthorFieldName = "id_author";

	@MDId
	@MDField(fieldName = "id_message", verbs = SELECT)
	public BigDecimal idMessage;

	@MDField(fieldName = idAuthorFieldName)
	public BigDecimal idUser;

	@MDManyToOne(fieldName = idAuthorFieldName, target = UserTest.class, loadPolicy = HEAVY)
	public UserTest userTest;

	@MDField(fieldName = "content")
	public String content;

	public MessageTest() {
	}

	public MessageTest(BigDecimal idMessage, BigDecimal idUser, UserTest userTest, String content) {
		this.idMessage = idMessage;
		this.idUser = idUser;
		this.userTest = userTest;
		this.content = content;
	}

	@Override
	public String toString() {
		return "MessageTest{" +
				"idMessage=" + idMessage +
				", idUser=" + idUser +
				", userTest=" + userTest +
				", content='" + content + '\'' +
				'}';
	}
}
