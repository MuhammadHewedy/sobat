package com.googlecode.sobat.ql;

import com.googlecode.sobat.configuration.Bootstrap;
import com.googlecode.sobat.ql.dialect.Dialect;

public class QueryConstants {
	
	private static Dialect dialect = Bootstrap.getDialect();
	
	public final static String SELECT 				= dialect.getSELECT();
	public final static String DESTINCT 			= dialect.getDESTINCT();
	public final static String FROM 				= dialect.getFROM();
	public final static String WHERE 				= dialect.getWHERE();
	public final static String ORDERBY 			= dialect.getORDERBY();
	public final static String ASC 				= dialect.getASC();
	public final static String DESC 				= dialect.getDESC();
	public final static String ALL 					= dialect.getALL();
	public final static String SELECT_ALL 			= dialect.getSELECT_ALL();
	public final static String COL_SEPARATOR 		= dialect.getCOL_SEPARATOR();
	public final static String EQ 					= dialect.getEQ();
	public final static String GT 					= dialect.getGT();
	public final static String GE 					= dialect.getGE();
	public final static String LT 					= dialect.getLT();
	public final static String LE 					= dialect.getLE();
	public final static String NEQ 				= dialect.getNEQ();
	public final static String LIKE 				= dialect.getLIKE();
	public final static String AND 				= dialect.getAND();
	public final static String OR 					= dialect.getOR();
}
