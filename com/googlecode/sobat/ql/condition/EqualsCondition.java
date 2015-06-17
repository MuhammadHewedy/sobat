package com.googlecode.sobat.ql.condition;

import com.googlecode.sobat.ql.QueryConstants;

class EqualsCondition extends Condition {

	EqualsCondition(String property, Object value) {
		super(property, value);
	}
	protected String buildCondition(String columnName, Object value) {
		return columnName + QueryConstants.EQ + value;
	}
}
