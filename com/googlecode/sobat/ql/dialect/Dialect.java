package com.googlecode.sobat.ql.dialect;

public interface Dialect{
	
	public abstract String getSELECT();
	public abstract String getDESTINCT();
	public abstract String getFROM();
	public abstract String getWHERE();
	public abstract String getORDERBY();
	public abstract String getASC();
	public abstract String getDESC();
	public abstract String getALL();
	public abstract String getSELECT_ALL();
	public abstract String getCOL_SEPARATOR();
	public abstract String getEQ();
	public abstract String getGT();
	public abstract String getGE();
	public abstract String getLT();
	public abstract String getLE();
	public abstract String getNEQ();
	public abstract String getLIKE();
	public abstract String getAND();
	public abstract String getOR();
}
