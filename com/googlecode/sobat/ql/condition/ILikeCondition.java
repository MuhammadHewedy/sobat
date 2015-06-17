package com.googlecode.sobat.ql.condition;

import com.googlecode.sobat.ql.QueryConstants;

class ILikeCondition extends LikeCondition {

	ILikeCondition(String property, Object value) {
		super(property, value);
	}
	protected String buildCondition(String columnName, Object values) {
		return columnName + QueryConstants.LIKE + values.toString().toLowerCase();
	}
}
