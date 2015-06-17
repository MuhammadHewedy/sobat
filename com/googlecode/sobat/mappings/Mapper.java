package com.googlecode.sobat.mappings;

import java.util.List;

import org.w3c.dom.Document;

import com.googlecode.sobat.configuration.Bootstrap;
import com.googlecode.sobat.configuration.ObjectXmlWrapper;
import com.googlecode.sobat.configuration.ObjectXmlWrapper.IdElement;
import com.googlecode.sobat.configuration.ObjectXmlWrapper.PropertyElement;
import com.googlecode.sobat.exceptions.NotMappedException;
import com.googlecode.sobat.util.ReflectionUtil;

/**
 * very important class
 * @author mohammed hewedy
 *
 */
public class Mapper {

	public static ObjectMapping getObjectMapping(Document doc, Object obj) {
		ObjectXmlWrapper objectWrapper = new ObjectXmlWrapper(doc);
		
		ObjectMapping objMap = new ObjectMapping();
		
		objMap.setClassName(objectWrapper.getClassElement().getName());
		objMap.setTableName(objectWrapper.getClassElement().getTable());
		boolean dynamicUpdate = objectWrapper.getClassElement().isDynamicUpdate();
		objMap.setDynamicUpdate(dynamicUpdate);
		
		IdMapping idmapping = new IdMapping();
		IdElement idele = objectWrapper.getIdElement(obj);
		idmapping.setName(idele.getName());
		idmapping.setColumnName(idele.getColumn());
		idmapping.setType(idele.getType());
		idmapping.setColumnValue(idele.getValue());
		objMap.setIdMapping(idmapping);
		
		List<PropertyElement> propElementList =objectWrapper.getPropertyElements(obj);
		for (PropertyElement propElement : propElementList) {
			PropertyMapping propertyMapping = new PropertyMapping();
			propertyMapping.setName(propElement.getName());
			propertyMapping.setColumnName(propElement.getColumn());
			propertyMapping.setType(propElement.getType());
			propertyMapping.setColumnValue(propElement.getValue());
			propertyMapping.setChanged(!dynamicUpdate);
			objMap.addPropertyMapping(propertyMapping);
		}

		return objMap;
	}
	
	public static Document forName (Class clazz) throws NotMappedException {
		char pathSeparator = System.getProperty("file.separator").charAt(0);
		String className = clazz.getName().replace('.', pathSeparator) + ".xml";
		Document doc = Bootstrap.getObjcetsMocumentMap().get(className);
		if (doc == null)
			throw new NotMappedException(clazz.getName() + " Not mapped");
		return doc;
	}

	/**
	 * very important method
	 * @param object
	 * @return
	 */
	public static ObjectMapping getObjectMapping(Object object) {
		Document doc = forName(object.getClass());
		return getObjectMapping(doc, object);
	}

	/**
	 * very important method
	 * @param object
	 * @return
	 */
	public static ObjectMapping getClassMapping(Class clazz) {
		try {
			String clazzName = ReflectionUtil.getClassName(clazz);
			return getObjectMapping(Class.forName(clazzName).newInstance());
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	public static String getPropertyName(String columnName, ObjectMapping objectMapping) {
		columnName = columnName.trim();
		return null;
	}
		
	public static String getColumnName(String property, ObjectMapping objectMapping) {
		property = property.trim();
		if (objectMapping.getIdMapping().getName().equals(property))
			return objectMapping.getIdMapping().getColumnName();
		
		List<PropertyMapping> propMapList = objectMapping.getPropertyMappingList();
		for (PropertyMapping propMap : propMapList) {
			if (propMap.getName().equals(property))
				return propMap.getColumnName();
		}
		throw new RuntimeException("Property name \"" + property+ "\" unmapped for type \"" + objectMapping.getClassName() + "\"");
	}
	
}
