package com.googlecode.sobat;

import java.sql.SQLException;

public class SessionManager {

	private static volatile SessionManager sessionManager = null;
	private static Session currentSession = null;
	
	private SessionManager() {
	}
	
	public static SessionManager getSessionManager() {
		synchronized (SessionManager.class) {
			if (sessionManager == null) {
				try {
				Class.forName("com.googlecode.sobat.configuration.Bootstrap");
				sessionManager = new SessionManager();
				}catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
		return sessionManager;
	}
	
	/**
	 * non-thread save
	 * @return
	 */
	public Session getCurrentSession() {
		if (currentSession == null) {
			currentSession = new SessionImpl();
		}
		return currentSession;
	}
	
	public Session newSession() throws SQLException {
		currentSession = new SessionImpl();
		return currentSession;
	}
}
