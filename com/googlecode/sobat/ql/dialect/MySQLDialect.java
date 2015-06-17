package com.googlecode.sobat.ql.dialect;

public class MySQLDialect implements Dialect{

	public String getALL() {
		return " * ";
	}
	public String getAND() {
		return " AND ";
	}
	public String getASC() {
		return " ASC ";
	}
	public String getCOL_SEPARATOR() {
		return ", ";
	}
	public String getDESC() {
		return " DESC ";
	}
	public String getDESTINCT() {
		return " DESTINCT ";
	}
	public String getEQ() {
		return " =  ";
	}
	public String getFROM() {
		return " FROM ";
	}
	public String getLE() {
		return " <= ";
	}
	public String getLIKE() {
		return " LIKE ";
	}
	public String getLT() {
		return " < ";
	}
	public String getNEQ() {
		return " <> ";
	}
	public String getOR() {
		return " OR ";
	}
	public String getORDERBY() {
		return " ORDER BY ";
	}
	public String getGE() {
		return " >= ";
	}
	public String getGT() {
		return " > ";
	}
	public String getSELECT() {
		return " SELECT ";
	}
	public String getSELECT_ALL() {
		return getSELECT() + getALL();
	}
	public String getWHERE() {
		return " WHERE ";
	}
	
}
