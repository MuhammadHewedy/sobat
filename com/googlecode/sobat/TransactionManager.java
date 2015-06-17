package com.googlecode.sobat;

import java.sql.Connection;
import java.sql.SQLException;

class TransactionManager {
	
	private Connection connection;
	
	TransactionManager() {
	}
	
	void beginTransaction() throws SQLException{
		if (connection == null) {
			connection = ConnectionProvider.getConnection();
		}
		connection.setAutoCommit(false);
	}
	
	void commitTransaction() throws SQLException{
		this.getConnection().commit();
	}
	void rollbackTransaction() throws SQLException{
		this.getConnection().rollback();
	}
	
	void close() throws SQLException {
		this.getConnection().close();
		connection = null;
	}
	Connection getConnection() {
		if (connection == null)
			throw new RuntimeException("Connection is null");
		return connection;
	}
	
}
