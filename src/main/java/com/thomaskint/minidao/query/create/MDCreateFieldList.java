package com.thomaskint.minidao.query.create;

import com.thomaskint.minidao.exception.MDFieldNotDeclaredException;
import com.thomaskint.minidao.utils.MDFieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.thomaskint.minidao.enumeration.MDParam.INSERT;

/**
 * Created by tkint on 22/01/2018.
 */
public class MDCreateFieldList {

	private List<MDCreateFieldValue> mdCreateFieldValues;

	public MDCreateFieldList(Class entityClass, MDCreateFieldValue... mdCreateFieldValues) throws MDFieldNotDeclaredException {
		this.mdCreateFieldValues = new ArrayList<>();
		for (MDCreateFieldValue mdCreateFieldValue : mdCreateFieldValues) {
			Field field = MDFieldUtils.getFieldByName(entityClass, mdCreateFieldValue.getField());
			if (MDFieldUtils.includeParam(field, INSERT)) {
				this.mdCreateFieldValues.add(mdCreateFieldValue);
			}
		}
	}

	public void addMDCreateFieldValue(MDCreateFieldValue mdCreateFieldValue) {
		mdCreateFieldValues.add(mdCreateFieldValue);
	}

	public String build() {
		String str = "(";
		for (int i = 0; i < mdCreateFieldValues.size(); i++) {
			str += mdCreateFieldValues.get(i).getField();
			if (i < mdCreateFieldValues.size() - 1) {
				str += ", ";
			}
		}
		str += ") VALUES ('";
		for (int i = 0; i < mdCreateFieldValues.size(); i++) {
			str += mdCreateFieldValues.get(i).getValue();
			if (i < mdCreateFieldValues.size() - 1) {
				str += "', '";
			}
		}
		str += "')";

		return str;
	}
}
