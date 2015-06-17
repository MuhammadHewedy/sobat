package com.googlecode.sobat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.googlecode.sobat.CacheProvider.Key;
import com.googlecode.sobat.exceptions.NotMappedException;
import com.googlecode.sobat.exceptions.NotUpdatedException;
import com.googlecode.sobat.exceptions.PersistenceException;
import com.googlecode.sobat.mappings.Mapper;
import com.googlecode.sobat.mappings.ObjectMapping;
import com.googlecode.sobat.util.ReflectionUtil;
import com.googlecode.sobat.util.StringUtil;


public abstract class Session {
	
	private CacheProvider cacheProvider ;
	private TransactionManager tnxMgr;
	
	Session() {
		tnxMgr = new TransactionManager();
	}
	
	/**
	 * Save transient Object, if you pass a detached object, update performed instead
	 * @param object transient object
	 * @throws NotMappedException
	 * @throws NotUpdatedException
	 */
	public void save(Object object) throws NotMappedException, NotUpdatedException{
		if (tnxMgr.getConnection() == null) throw new RuntimeException("session is closed");
		
		ObjectMapping objMapping = Mapper.getObjectMapping(object);
		if (!StringUtil.isNullSpacesEmptyOrZero(objMapping.getIdMapping().getColumnValue())) {
			update(object);
			return;
		}
		Object key = doSave(objMapping, object);
		cacheProvider.cache(new Key(key, object.getClass()), new CachedObject(object, false));
	}
	/**
	 * Updates a detached Object
	 * @param object detached Object
	 */
	public void update(Object object) {
		if (tnxMgr.getConnection() == null) throw new RuntimeException("session is closed");
		
		Object key = Mapper.getObjectMapping(object).getIdMapping().getColumnValue();
		if (StringUtil.isNullSpacesEmptyOrZero(key))
			throw new RuntimeException(object + " Not a persistence object");
		cacheProvider.cache(new Key(key, object.getClass()), new CachedObject(object, true));
	}
	/**
	 * Deletes a detached Object
	 * @param object Detached Object
	 */
	public void delete (Object object) {
		if (tnxMgr.getConnection() == null) throw new RuntimeException("session is closed");
		
		ObjectMapping objMapping = Mapper.getObjectMapping(object);
		doDelete(objMapping);
		cacheProvider.deCache(new Key(objMapping.getIdMapping().getColumnValue(), object.getClass()));
	}
	
	/**
	 * Get a specific row that with the passed primary key
	 * @param <T>
	 * @param clazz
	 * @param key the value of the key (note: use the same object as defined in the Model class (long, int, .. etc)
	 * @return
	 */
	public<T> T get (Class<T> clazz, Object key) {
		if (tnxMgr.getConnection() == null) throw new RuntimeException("session is closed");
		
		CachedObject object = cacheProvider.fromCache(new Key(key,clazz)); // try to get the object first from cache
		if (object != null)
			return (T) object.getObject();
		
		String clazzName = ReflectionUtil.getClassName(clazz);
		ObjectMapping objMapping = Mapper.getClassMapping(clazz);
		T ret = doGet(objMapping, clazzName, key);
		if (ret == null)
			throw new PersistenceException("Object not peresisted");
		cacheProvider.cache(new Key(key, ret.getClass()), new CachedObject(ret, false));		// caching the object for later usage
		return ret;
	}
	/**
	 * Get all Database rows for a Class
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T> List<T> get (Class<T> clazz) {
		if (tnxMgr.getConnection() == null) throw new RuntimeException("session is closed");
		
		try {
			String clazzName = ReflectionUtil.getClassName(clazz);
			ObjectMapping objMapping = Mapper.getClassMapping(clazz);
			return doGet(objMapping, clazzName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	
	abstract Object doSave(ObjectMapping objectMapping, Object object);
	abstract void doUpdate(ObjectMapping objectMapping, Object object);
	abstract void doDelete(ObjectMapping objectMapping);
	abstract <T> T doGet(ObjectMapping objectMapping, String clazzName, Object key);
	abstract <T> List<T> doGet(ObjectMapping objectMapping, String clazzName);
	
	/**
	 * This method expects native SQL Query and therefore, reduces portability across Databases
	 * @param sqlQuery native SQL Query
	 * @param clazz used in case of not querying by projection, if you specified list of columns to be retrieved, this parameter not being used
	 * and you can pass null instead
	 * @return
	 */
	public Query createSQLQuery(String sqlQuery, Class clazz) {
		return new SQLQuery(sqlQuery, getConnection(), clazz);
	}

	//------------------------------------------------------------------------ Transaction Management methods --------------------------------------------------------------------
	/**
	 * Creates a new {@link java.sql.Connection} Object if the session was closed by calling {@link #close()}, 
	 * Otherwise, it returns the the same {@link java.sql.Connection} object .
	 * @throws SQLException
	 */
	public void beginTransaction() throws SQLException{
		tnxMgr.beginTransaction();
		cacheProvider = new CacheProvider();
	}
	
	public void commitTransaction() throws SQLException{
		flushCache();
		tnxMgr.commitTransaction();
		cacheProvider = null;
	}
	public void rollbackTransaction() throws SQLException{
		tnxMgr.rollbackTransaction();
		cacheProvider = null;
	}
	
	public void close() throws SQLException {
		tnxMgr.close();
		cacheProvider = null;
	}
	
	Connection getConnection() {
		return tnxMgr.getConnection();
	}
	
	//------------------------------------------------------------------------ End Transaction Management methods --------------------------------------------------------------------
	
	private void flushCache() {
		Set<Key> keys = cacheProvider.getCachedObjects().keySet();
		for (Key key : keys) {
			CachedObject cObject = cacheProvider.getCachedObjects().get(key);
			if (cObject.isUpdated()) {
				Object object = cObject.getObject();
				doUpdate(Mapper.getObjectMapping(object), object);
			}
		}
	}
}
