package com.googlecode.sobat;

import java.util.HashMap;
import java.util.Map;

public class CacheProvider {
	
	private HashMap<Key, CachedObject> cache = new HashMap<Key, CachedObject>();
	
	public void cache(Key key, CachedObject cObject) {
		cache.put(key, cObject);
	}

	public CachedObject fromCache(Key key) {
		return cache.get(key);
	}
	
	public Map<Key, CachedObject> getCachedObjects(){
		return cache;
	}
	
	public void deCache(Key key) {
		cache.remove(key);
	}
	
	public static class Key{
		private Object value;
		private Class clazz;
		
		public Key(Object value, Class clazz) {
			super();
			this.value = value;
			this.clazz = clazz;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public Class getClazz() {
			return clazz;
		}
		public void setClazz(Class clazz) {
			this.clazz = clazz;
		}
		
		public boolean equals(Object obj) {
			if (!(obj instanceof Key)) return false;
			Key other = (Key) obj;
			return this.getValue().equals(other.getValue()) && this.getClazz().equals(other.getClazz());
		}
		public int hashCode() {
			return getValue().hashCode() * getClazz().hashCode();
		}
	}
}
