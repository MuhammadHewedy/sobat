package com.googlecode.sobat.ql;

import java.util.Arrays;
import java.util.List;

import com.googlecode.sobat.mappings.Mapper;

public class Projection extends Clause {

	private List<String> propertyNames ;
	private From from;
	
	/**
	 * 
	 * @param from the from object on which this projection will apply
	 * @param propertyName array of property names
	 */
	public Projection(From from, String[] propertyName) {
		this(from, Arrays.asList(propertyName));
	}
	public Projection(From from, List<String> propertyName) {
		this.from = from;
		this.propertyNames = propertyName;
	}
	
	public String getValue() {
		StringBuffer sb = new StringBuffer(propertyNames.size());
		
		for (String propertyName : propertyNames) {
			sb.append(Mapper.getColumnName(propertyName, getObjectMapping()) + QueryConstants.COL_SEPARATOR);
		}
		
		String fromQl = from.getValue();
		String propertyQl = QueryConstants.ALL;
		
		if (sb.length() != 0) {
			propertyQl = sb.toString().substring(0, sb.toString().length() -2) + " ";
		}
		return QueryConstants.SELECT + propertyQl +fromQl.substring(fromQl.indexOf(QueryConstants.FROM.trim()));
	}

	public From getFrom() {
		return from;
	}

}
