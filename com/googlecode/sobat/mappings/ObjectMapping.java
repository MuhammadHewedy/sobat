package com.googlecode.sobat.mappings;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapping {
	private String className;
	private String tableName;
	private boolean dynamicUpdate;
	private List<PropertyMapping> propertyMappingList = new ArrayList<PropertyMapping>();
	private IdMapping idMapping;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void addPropertyMapping(PropertyMapping propMapping) {
		propertyMappingList.add(propMapping);
	}
	
	public List<PropertyMapping> getPropertyMappingList() {
		return propertyMappingList;
	}
	public void setPropertyMappingList(List<PropertyMapping> propertyMappingList) {
		this.propertyMappingList = propertyMappingList;
	}
	public void setIdMapping(IdMapping idMapping) {
		this.idMapping = idMapping;
	}
	public IdMapping getIdMapping() {
		return idMapping;
	}
	public void setDynamicUpdate(boolean dynamicUpdate) {
		this.dynamicUpdate = dynamicUpdate;
	}
	public boolean isDynamicUpdate() {
		return dynamicUpdate;
	}
}
