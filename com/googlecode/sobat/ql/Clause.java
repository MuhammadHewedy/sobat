package com.googlecode.sobat.ql;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.sobat.Query;
import com.googlecode.sobat.Session;
import com.googlecode.sobat.SessionManager;
import com.googlecode.sobat.mappings.Mapper;
import com.googlecode.sobat.mappings.ObjectMapping;

public abstract class Clause {
	
	public abstract From getFrom();
	public abstract String getValue();
	
	public List execute() {
		List list = null;
		try {
			SessionManager mgr = SessionManager.getSessionManager();
			Session session =  mgr.newSession();
			session.beginTransaction();
			Query query = session.createSQLQuery(getValue(), getFrom().getClazz());
			list = query.execute();
			session.commitTransaction();
			session.close();
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return list;
	}
	
	public ObjectMapping getObjectMapping () {
		return Mapper.getClassMapping(getFrom().getClazz());
	}
}
