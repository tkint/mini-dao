package com.thomaskint.minidao.testonly.model;

import com.thomaskint.minidao.annotation.MDEntity;
import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDId;
import com.thomaskint.minidao.annotation.MDManyToOne;
import com.thomaskint.minidao.enumeration.MDVerb;

import java.math.BigDecimal;

import static com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY;
import static com.thomaskint.minidao.enumeration.MDVerb.SELECT;

/**
 * @author Thomas Kint
 */
@MDEntity(name = "message")
public class MessageTest {

	private static final String idUserFieldName = "id_author";

	@MDId
	@MDField(name = "id_message", verbs = SELECT)
	public BigDecimal idMessage;

	@MDField(name = idUserFieldName)
	public BigDecimal idUser;

	@MDManyToOne(name = idUserFieldName, entity = UserTest.class, loadPolicy = HEAVY)
	public UserTest userTest;

	@MDField(name = "content")
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
