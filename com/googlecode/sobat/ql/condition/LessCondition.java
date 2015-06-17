package com.googlecode.sobat.ql.condition;

import com.googlecode.sobat.ql.QueryConstants;

class LessCondition extends Condition {

	LessCondition(String property, Object value) {
		super(property, value);
	}

	protected String buildCondition(String columnName, Object values) {
		return columnName + QueryConstants.LT + values;
	}
}
