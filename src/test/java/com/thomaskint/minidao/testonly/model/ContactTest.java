package com.thomaskint.minidao.testonly.model;

import com.thomaskint.minidao.annotation.MDEntity;
import com.thomaskint.minidao.annotation.MDField;
import com.thomaskint.minidao.annotation.MDId;

import java.math.BigDecimal;

/**
 * @author Thomas Kint
 */
@MDEntity(name = "contact")
public class ContactTest {

	public static final String idContactField = "id_contact";

	@MDId
	@MDField(name = idContactField)
	public BigDecimal idContact;

	@MDField(name = "first_name")
	public String firstName;

	@MDField(name = "last_name")
	public String lastName;

	@Override
	public String toString() {
		return "ContactTest{" +
				"idContact=" + idContact +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				'}';
	}
}
