package com.thomaskint.minidao.testonly.model;

import com.thomaskint.minidao.annotation.MDEntity;
import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDId;
import com.thomaskint.minidao.annotation.MDInheritLink;

import java.math.BigDecimal;

/**
 * @author Thomas Kint
 */
@MDEntity(name = "player")
public class PlayerTest extends ContactTest {

	@MDId
	@MDField(name = "id_player")
	public BigDecimal idPlayer;

	@MDInheritLink
	@MDField(name = "id_contact_parent")
	public BigDecimal idContact;

	@MDField(name = "pseudo")
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


