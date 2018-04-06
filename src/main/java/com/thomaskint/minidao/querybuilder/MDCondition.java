/*
 * MIT License
 *
 * Copyright (c) 2017 Thomas Kint
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.enumeration.MDConditionLink;
import com.thomaskint.minidao.enumeration.MDConditionOperator;
import com.thomaskint.minidao.model.MDEntityInfo;

import static com.thomaskint.minidao.utils.MDStringUtils.*;

public class MDCondition {

	private String fieldName;

	private MDConditionOperator operator;

	private Object value;

	private MDConditionLink conditionLink = null;

	private MDCondition subCondition = null;

	public MDCondition(String fieldName, MDConditionOperator operator, Object value, MDConditionLink conditionLink, MDCondition subCondition) {
		this.fieldName = fieldName;
		this.operator = operator;
		this.value = value;
		this.conditionLink = conditionLink;
		this.subCondition = subCondition;
	}

	public MDCondition(String fieldName, MDConditionOperator operator, Object value) {
		this.fieldName = fieldName;
		this.operator = operator;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public MDConditionOperator getOperator() {
		return operator;
	}

	public Object getValue() {
		return value;
	}

	public String build(MDEntityInfo entityInfo) {
		StringBuilder conditionBuilder = new StringBuilder();
		conditionBuilder.append(entityInfo.getTableName());
		conditionBuilder.append(DOT);
		conditionBuilder.append(fieldName);
		conditionBuilder.append(SPACE);
		conditionBuilder.append(operator);
		conditionBuilder.append(SPACE);
		if (!(value instanceof Number)) {
			conditionBuilder.append(QUOTE);
		}
		conditionBuilder.append(value);
		if (!(value instanceof Number)) {
			conditionBuilder.append(QUOTE);
		}
		if (hasSubCondition()) {
			conditionBuilder.append(SPACE);
			conditionBuilder.append(conditionLink);
			conditionBuilder.append(SPACE);
			conditionBuilder.append(subCondition.build(entityInfo));
		}
		return conditionBuilder.toString();
	}

	public boolean hasSubCondition() {
		return subCondition != null && conditionLink != null;
	}

	@Override
	public String toString() {
		return "MDCondition{" +
				"fieldName='" + fieldName + '\'' +
				", operator=" + operator +
				", value=" + value +
				", conditionLink=" + conditionLink +
				", subCondition=" + subCondition +
				'}';
	}
}
