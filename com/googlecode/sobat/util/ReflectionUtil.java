package com.googlecode.sobat.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
	
	public static String forSetter(String field) {
		return "set" + Character.toUpperCase(field.charAt(0)) +field.substring(1, field.length());
	}
	
	public static String forGetter(String field) {
		return "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1, field.length());
	}
	
	public static synchronized void setSetterValue(String field, Object object, String typeClass, Object value, boolean isProperty) 
		throws ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		setSetterValue(field, object, Class.forName(typeClass), value, isProperty);
	}
	public static synchronized void setSetterValue(String field, Object object, Class typeClass, Object value, boolean isProperty) 
		throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		String propertyName = field;
		if (!isProperty)
			propertyName = forSetter(field);
		Method m = object.getClass().getDeclaredMethod(propertyName, typeClass);
		m.setAccessible(true);
		m.invoke(object, value);
	}
	
	public static synchronized Object getGetterValue(String field, Object object, boolean isProperty)
		throws  IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {

		Class clazz = object.getClass();
		String propertyName = field;
		if (!isProperty)
			propertyName = forGetter(field);
		Method getter = clazz.getMethod(propertyName ,(Class[]) null);
		return  getter.invoke(object, (Object[]) null);
	}

	public static synchronized Object getGetterValue(String field, Object object)
		throws  IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		return getGetterValue(field, object, false);
	}
	
	public static String getClassName (Class clazz) {
		String clazzName = clazz.toString();
		return clazzName.substring(clazzName.indexOf(" ") +1);
	}
}
