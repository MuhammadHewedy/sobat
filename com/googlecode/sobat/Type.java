package com.googlecode.sobat;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * The following SQL Types are the only Should be passed <br/>
 * TINYINT SMALLINT INTEGER BIGINT REAL FLOAT DOUBLE DECIMAL NUMERIC BIT CHAR VARCHAR LONGVARCHAR BINARY VARBINARY 
 * LONGVARBINARY DATE TIME TIMESTAMP
 * @author mohammed hewedy
 * @version 1.0
 */
public class Type {
	
	private static Map<String, Class<?>> typesMap = new HashMap<String, Class<?>>();
	
	/**
	 * Map of supported types
	 */
	public static Map<String, Class<?>> getTypesMap(){
		return typesMap;
	}
	
	public static Class fromString2JavaClass(String jdbcType) {
		Class type = typesMap.get(jdbcType.toUpperCase());
		if (type == null) {
			throw new RuntimeException("type not mapped");
		}
		return type; 
	}
		
	public static int fromString2Jdbc(String type) {
		try {
			return Integer.valueOf(Types.class.getField(type.toUpperCase()).get(null).toString());
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	static {
		// only supported SQL types
		typesMap.put("TINYINT", byte.class);
		typesMap.put("SMALLINT", short.class);
		typesMap.put("INTEGER", int.class);
		typesMap.put("BIGINT", long.class);
		typesMap.put("REAL", float.class);
		typesMap.put("FLOAT", double.class);
		typesMap.put("DOUBLE", double.class);
		typesMap.put("DECIMAL", BigDecimal.class);
		typesMap.put("NUMERIC", BigDecimal.class);
		typesMap.put("BIT", boolean.class);
		typesMap.put("CHAR", String.class);
		typesMap.put("VARCHAR", String.class);
		typesMap.put("LONGVARCHAR", InputStream.class);
		typesMap.put("BINARY", byte[].class);
		typesMap.put("VARBINARY", byte[].class);
		typesMap.put("LONGVARBINARY", InputStream.class);
		typesMap.put("DATE", java.sql.Date.class);
		typesMap.put("TIME", java.sql.Time.class);
		typesMap.put("TIMESTAMP", java.sql.Timestamp.class);
	}
}
