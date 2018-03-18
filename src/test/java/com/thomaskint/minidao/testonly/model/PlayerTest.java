package com.thomaskint.minidao.testonly.model;

import com.thomaskint.minidao.annotation.MDEntity;
import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDId;
import com.thomaskint.minidao.annotation.MDInheritLink;

import java.math.BigDecimal;

/**
 * @author Thomas Kint
 */
@MDEntity(tableName = "player")
public class PlayerTest extends ContactTest {

	@MDId
	@MDField(fieldName = "id_player")
	public BigDecimal idPlayer;

	@MDInheritLink
	@MDField(fieldName = "id_contact_parent")
	public BigDecimal idContact;

	@MDField(fieldName = "pseudo")
	public String pseudo;

	@Override
	public String toString() {
		return "PlayerTest{" +
				"idPlayer=" + idPlayer +
				", idContact=" + idContact +
				", pseudo='" + pseudo + '\'' +
				"} " + super.toString();
	}
}


