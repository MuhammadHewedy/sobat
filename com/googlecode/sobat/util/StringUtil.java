package com.googlecode.sobat.util;

public class StringUtil {
	public static boolean isNullSpacesOrEmpty(String str) {
		return (str == null) || (str.trim().length() == 0);
	}
	
	public static boolean isNullSpacesEmptyOrZero(Object obj) {
		if (obj instanceof String)
			return isNullSpacesOrEmpty((String)obj);
		if (obj instanceof Number)
			return isNullOrZero((Number)obj);
		return (obj == null);
	}
	
	public static boolean isNullOrZero(Number number) {
		return (number == null) || ((Number) number).doubleValue() == 0;
	}
	
	public static void main(String[] args) {
		int x = 1;
		System.out.println(isNullSpacesEmptyOrZero(x));
	}
}
