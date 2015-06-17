package com.googlecode.sobat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.googlecode.sobat.mappings.IdMapping;
import com.googlecode.sobat.mappings.ObjectMapping;
import com.googlecode.sobat.mappings.PropertyMapping;
import com.googlecode.sobat.util.ReflectionUtil;

public class ObjectResultSet {
	
	public static synchronized <T> void fillObjectList(ResultSet rs, ObjectMapping objectMapping, String clazzName, List<T> list) throws Exception {
		while (rs.next()) {
			T object = (T) Class.forName(clazzName).newInstance(); 
			
			IdMapping idMapping = objectMapping.getIdMapping();
			Object idValue = rs.getObject(idMapping.getColumnName(), Type.getTypesMap());
			ReflectionUtil.setSetterValue(idMapping.getName(), object, Type.fromString2JavaClass(idMapping.getType()), idValue, false);
			
			List<PropertyMapping> propMappingList = objectMapping.getPropertyMappingList();
			for (PropertyMapping propMap : propMappingList ) {
				Object propertyValue = rs.getObject(propMap.getColumnName(), Type.getTypesMap());
				ReflectionUtil.setSetterValue(propMap.getName(), object, Type.fromString2JavaClass(propMap.getType()), propertyValue, false);
			}
			list.add(object);
		}
	}
	public static synchronized <T> void fillObjectArrayList(ResultSet rs, List<Object[]> list) throws SQLException {
		while (rs.next()) {
			Object objectArr []= new Object[rs.getMetaData().getColumnCount()];
			for (int i=1; i <=objectArr.length; i++) {
				objectArr[i-1] = rs.getObject(i, Type.getTypesMap());
			}
			list.add(objectArr);
		}
	}
}
