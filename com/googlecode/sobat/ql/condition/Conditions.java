package com.googlecode.sobat.ql.condition;

import com.googlecode.sobat.mappings.Mapper;
import com.googlecode.sobat.ql.From;
import com.googlecode.sobat.ql.QueryConstants;
import com.googlecode.sobat.ql.condition.CreateCondition.Type;

public class Conditions extends Condition {
	
	private StringBuffer sb;
	private From from;
	
	public Conditions(From from) {
		super(null, null);
		sb = new StringBuffer();
		this.from = from;
	}
	
	public void andCondition(Type conditionType, String property, Object value) {
		if (sb.length() != 0) {
			sb.append(QueryConstants.AND);
		}
		append(conditionType, property, value);
	}
	
	public void addCondition(Type conditionType, String property, Object value) {
		if (sb.length() > 0)
			throw new RuntimeException("Invalid invocation of method, choose either andCondition() or orCondition() ");
		append(conditionType, property, value);
	}
	
	public void orCondition(Type conditionType, String property, Object value) {
		if (sb.length() != 0) {
			sb.append(QueryConstants.OR);
		}
		append(conditionType, property, value);
	}
	
	private void append(Type conditionType, String property, Object value) {
		String columnName = Mapper.getColumnName(property, from.getObjectMapping());
		sb.append(CreateCondition.create(conditionType, columnName, value).getCondition());
	}
	
	public String getCondition() {
		return sb.toString();
	}
	
	protected String buildCondition(String columnName, Object values) {
		return null;
	}
}
