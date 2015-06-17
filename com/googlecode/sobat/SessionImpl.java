package com.googlecode.sobat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.sobat.exceptions.NotUpdatedException;
import com.googlecode.sobat.mappings.IdMapping;
import com.googlecode.sobat.mappings.ObjectMapping;
import com.googlecode.sobat.mappings.PropertyMapping;
import com.googlecode.sobat.util.ReflectionUtil;


public class SessionImpl extends Session{
	
	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	Object doSave(ObjectMapping objectMapping, Object object) throws NotUpdatedException{
		try {
			StringBuffer sql = new StringBuffer();
			StringBuffer columns = new StringBuffer();
			StringBuffer values = new StringBuffer();
			int rowcount = 0;
			
			String tableName = objectMapping.getTableName();
			sql.append("INSERT INTO " + tableName + " (");
			
			for (PropertyMapping propMapping : objectMapping.getPropertyMappingList()) {
				String columnName = propMapping.getColumnName();
				Object columnValue = propMapping.getColumnValue();
				
				if (columnValue == null)
					continue;
				columns.append(columnName + ", ");
				values.append("?, ");
			}
			String columnsSql = columns.toString();
			String valuesSql = values.toString();
			if (columnsSql.length() >= 2)
				columnsSql = columnsSql.substring(0, columnsSql.length() -2);
			if (valuesSql.length() >= 2)
				valuesSql= valuesSql.substring(0, valuesSql.length() -2);
			
			if (valuesSql.length() <= 0)
				return null;
			
			sql.append(columnsSql + " ) ").append(" VALUES ( ").append(valuesSql).append(" )" );
			
			logger.info("SOBAT: " + sql.toString());
			
			PreparedStatement pStmt = getConnection().prepareStatement(sql.toString());
			
			int counter = 0;
			for (PropertyMapping propMapping : objectMapping.getPropertyMappingList()) {
				Object columnValue = propMapping.getColumnValue();
				String columnType = propMapping.getType();
				if (columnValue == null)
					continue;
				counter ++;
				pStmt.setObject(counter, columnValue, Type.fromString2Jdbc(columnType));
			}
			
			rowcount = pStmt.executeUpdate();
			if (rowcount == 0)
				throw new NotUpdatedException("No insertion done!");
			// getting the PK value:
			IdMapping idProp = objectMapping.getIdMapping();
			String idQuery = "SELECT MAX(" + idProp.getColumnName()+ ") FROM " + tableName;
			ResultSet rs = pStmt.executeQuery(idQuery);
			rs.next();
			Object id = rs.getObject(1, Type.getTypesMap());
			ReflectionUtil.setSetterValue(idProp.getName(), object, Type.fromString2JavaClass(idProp.getType()), id, false);
			return id;
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	//  make this check against a saved snapshot instead of the database
	void doColumnChecking(ObjectMapping objectMapping) throws SQLException{
		String tableName = objectMapping.getTableName();
		String idColName = objectMapping.getIdMapping().getColumnName();
		Object idColValue = objectMapping.getIdMapping().getColumnValue();
		
		for (PropertyMapping propMapping : objectMapping.getPropertyMappingList()) {
			String columnName = propMapping.getColumnName();
			Object memoryColumnValue = propMapping.getColumnValue();
			if (memoryColumnValue == null)
				continue;
			String changedSql = "SELECT " + columnName + " FROM " + tableName + " WHERE " + idColName + " =  " + idColValue ;

			ResultSet rs = getConnection().createStatement().executeQuery(changedSql);
			rs.next();
			Object dbColumnValue = rs.getObject(1, Type.getTypesMap());
			propMapping.setChanged(!memoryColumnValue.equals(dbColumnValue));
		}
	}
	//  make this check against a saved snapshot instead of the database
	void doUpdate(ObjectMapping objectMapping, Object object) {
		try {
			StringBuffer sql = new StringBuffer();
			StringBuffer columns = new StringBuffer();
			int rowcount = 0;
			
			String tableName = objectMapping.getTableName();
			sql.append("UPDATE " + tableName + " SET ");
			
			if (objectMapping.isDynamicUpdate()) {
				doColumnChecking(objectMapping);
			}
			
			for (PropertyMapping propMapping : objectMapping.getPropertyMappingList()) {
				String columnName = propMapping.getColumnName();
				Object columnValue = propMapping.getColumnValue();
				boolean isChanged = propMapping.isChanged();
				if (columnValue == null || !isChanged)
					continue;
				columns.append(columnName + " = ");
				columns.append("?, ");
			}
			String columnsSql = columns.toString();
			if (columnsSql.length() >= 2) // to remove the comma
				columnsSql= columnsSql.substring(0, columnsSql.length() -2);
			
			if (columnsSql.length() <= 0)
				return;
			
			sql.append(columnsSql);
			sql.append(" WHERE " +  objectMapping.getIdMapping().getColumnName() + " = " + objectMapping.getIdMapping().getColumnValue());
			
			logger.info("SOBAT: " + sql.toString());
			
			PreparedStatement pStmt = getConnection().prepareStatement(sql.toString());
			
			int counter = 0;
			for (PropertyMapping propMapping : objectMapping.getPropertyMappingList()) {
				Object columnValue = propMapping.getColumnValue();
				String columnType = propMapping.getType();
				boolean isChanged = propMapping.isChanged();
				if (columnValue == null || !isChanged)
					continue;
				counter ++;
				pStmt.setObject(counter, columnValue, Type.fromString2Jdbc(columnType));
			}
			
			rowcount = pStmt.executeUpdate();
			if (rowcount == 0)
				throw new NotUpdatedException("No update done!");
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	void doDelete(ObjectMapping objectMapping) {
		try {
			StringBuffer sql = new StringBuffer();
			int rowcount = 0;
			
			String tableName = objectMapping.getTableName();
			sql.append("DELETE FROM  " + tableName );
			sql.append(" WHERE " +  objectMapping.getIdMapping().getColumnName() + " = " + objectMapping.getIdMapping().getColumnValue());
			
			logger.info("SOBAT: " + sql.toString());
			
			PreparedStatement pStmt = getConnection().prepareStatement(sql.toString());
			rowcount = pStmt.executeUpdate();
			if (rowcount == 0)
				throw new NotUpdatedException("No Delete done!");
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	<T> T doGet(ObjectMapping objectMapping, String clazzName, Object key) {
		try {
			List<T> list = new ArrayList<T>();
			String tableName = objectMapping.getTableName();
			String sql = "SELECT * FROM " + tableName + " WHERE " + objectMapping.getIdMapping().getColumnName() +  " = " + key;
			PreparedStatement pStmt = getConnection().prepareStatement(sql);
			logger.info("SOBAT: " + sql);
			ResultSet rs = pStmt.executeQuery();
			ObjectResultSet.fillObjectList(rs,objectMapping, clazzName, list);
			return (list.isEmpty() ? null : list.get(0));
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	<T> List<T> doGet(ObjectMapping objectMapping, String clazzName) {
		try {
			List<T> list = new ArrayList<T>();
			String tableName = objectMapping.getTableName();
			String sql = "SELECT * FROM " + tableName;
			PreparedStatement pStmt = getConnection().prepareStatement(sql);
			logger.info("SOBAT: " + sql);
			ResultSet rs = pStmt.executeQuery();
			ObjectResultSet.fillObjectList(rs,objectMapping, clazzName, list);
			return list;
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} 
	}
}
