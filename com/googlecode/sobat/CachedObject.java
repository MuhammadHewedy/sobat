package com.googlecode.sobat;

public class CachedObject {
	private Object object;
	private boolean updated;
	
	public CachedObject(Object object, boolean updated) {
		this.object = object;
		this.updated = updated;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public boolean isUpdated() {
		return updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
}
