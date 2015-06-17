package com.googlecode.sobat.ql.condition;

import com.googlecode.sobat.ql.QueryConstants;

class LikeCondition extends Condition{

	LikeCondition(String property, Object value) {
		super(property, value);
	}
	protected String buildCondition(String columnName, Object values) {
		return columnName + QueryConstants.LIKE + values.toString();
	}
}
