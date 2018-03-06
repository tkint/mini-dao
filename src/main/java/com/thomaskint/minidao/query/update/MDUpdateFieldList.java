package com.thomaskint.minidao.query.update;

import com.thomaskint.minidao.exception.MDFieldNotDeclaredException;
import com.thomaskint.minidao.utils.MDFieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.thomaskint.minidao.enumeration.MDParam.UPDATE;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDUpdateFieldList {

	private List<MDUpdateFieldValue> mdUpdateFieldValues;

	public MDUpdateFieldList(Class entityClass, MDUpdateFieldValue... mdUpdateFieldValues) throws MDFieldNotDeclaredException {
		this.mdUpdateFieldValues = new ArrayList<>();
		for (MDUpdateFieldValue mdUpdateFieldValue : mdUpdateFieldValues) {
			Field field = MDFieldUtils.getFieldByName(entityClass, mdUpdateFieldValue.getField());
			if (MDFieldUtils.includeParam(field, UPDATE)) {
				this.mdUpdateFieldValues.add(mdUpdateFieldValue);
			}
		}
	}

	public String build() {
		String str = " SET ";
		for (int i = 0; i < mdUpdateFieldValues.size(); i++) {
			if (i > 0) {
				str += ", ";
			}
			str += mdUpdateFieldValues.get(i).build();
		}
		return str;
	}


	public void addMDUpdateFieldValue(MDUpdateFieldValue mdUpdateFieldValue) {
		mdUpdateFieldValues.add(mdUpdateFieldValue);
	}
}
