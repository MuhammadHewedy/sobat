package com.googlecode.sobat.mappings;

public class PropertyMapping {
	private String name;
	private String columnName;
	private Object columnValue;
	private String type;
	private boolean changed;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getColumnValue() {
		return columnValue;
	}
	public void setColumnValue(Object columnValue) {
		this.columnValue = columnValue;
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
