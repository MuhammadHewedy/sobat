package com.googlecode.sobat.ql;

import com.googlecode.sobat.mappings.Mapper;

public class OrderBy extends Clause {

	private Clause clause;
	private Order order;
	private String property;
	
	public OrderBy(Clause clause, String property, Order order) {
		this.clause = clause;
		this.order = order;
		this.property = property;
	}
	
	public String getValue() {
		String columnName = Mapper.getColumnName(property, getObjectMapping());
		String ret = "";
		if (!clause.getValue().contains(QueryConstants.ORDERBY))
			ret = clause.getValue() + QueryConstants.ORDERBY + columnName + (order==Order.ASC ? QueryConstants.ASC : QueryConstants.DESC);
		else {
			String value  = clause.getValue();
			
			int lastIndex = value.lastIndexOf(QueryConstants.ASC) + QueryConstants.ASC.length();
			if (value.lastIndexOf(QueryConstants.DESC) + QueryConstants.DESC.length() > lastIndex)
				lastIndex = value.lastIndexOf(QueryConstants.DESC) + QueryConstants.DESC.length();
			
			StringBuffer before = new StringBuffer(value.substring(0, lastIndex));
			before.append(QueryConstants.COL_SEPARATOR + columnName + (order==Order.ASC ? QueryConstants.ASC : QueryConstants.DESC));
			StringBuffer after = new StringBuffer(value.substring(lastIndex, value.length()));
			before.append(after);
			ret = before.toString();
		
		}
		return ret;
	}

	public enum Order{
		ASC, DESC;
	}

	public From getFrom() {
		return clause.getFrom();
	}
}
