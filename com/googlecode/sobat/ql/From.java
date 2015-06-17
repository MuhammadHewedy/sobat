package com.googlecode.sobat.ql;

@SuppressWarnings("unchecked")
public class From extends Clause {
	private Class clazz;
	
	public From(Class clazz) {
		this.clazz = clazz;
	}
	public String getValue() {
		return QueryConstants.SELECT_ALL + QueryConstants.FROM + getObjectMapping().getTableName();
	}

	public From getFrom() {
		return this;
	}
	
	public Class getClazz() {
		return clazz;
	}
}
