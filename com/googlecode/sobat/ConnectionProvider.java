package com.googlecode.sobat;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.googlecode.sobat.configuration.MappingXmlWrapper;
import com.googlecode.sobat.configuration.MappingXmlWrapper.DatasourceElement;
import com.googlecode.sobat.configuration.MappingXmlWrapper.DriverManagerElement;
import com.googlecode.sobat.util.StringUtil;

public class ConnectionProvider {
	private static volatile DataSource datasource;
	
	private static String JNDI_NAME;
	private static String PROVIDER_URL;
	private static String INITIAL_CONTEXT_FACTORY;
	
	private static Driver getDriver() throws RuntimeException{
		Driver driver = null;
		try {
			DriverManagerElement dme = MappingXmlWrapper.getBootstrapInstance().getDriverManagerElement();
			if (MappingXmlWrapper.getBootstrapInstance().getDriverManagerElement() == null)
				return null;

			Class.forName(dme.getDriverClass());
			driver = DriverManager.getDriver(dme.getConnectionUrl());
		}catch(Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return driver;
	}
	
	/**
	 * try to get connection from Datasource, if cannot, try to get it from DriverManager, if not throws SQLException
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException{
		DataSource ds = getDataSource();
		if (ds != null)
			return ds.getConnection();
		Driver d = getDriver();
		if (d != null) {
			DriverManagerElement dme = MappingXmlWrapper.getBootstrapInstance().getDriverManagerElement();
			Properties info = new Properties();
			info.put("user", dme.getUserName());
			info.put("password", dme.getPassword());
			
			return d.connect(dme.getConnectionUrl(), info);
		}
		throw new SQLException("Cannot get a connection, please setup mapping.xml");
	}
	
	private static DataSource getDataSource() throws RuntimeException{
		synchronized (ConnectionProvider.class) {
			if (datasource == null) {
				try {
					if (MappingXmlWrapper.getBootstrapInstance().getDatasourceElement()== null)
						return null;
					populateDatasourceElementProperties();
					Properties env = new Properties();
					if (!StringUtil.isNullSpacesOrEmpty(PROVIDER_URL))
						env.put(Context.PROVIDER_URL, PROVIDER_URL);
					if (!StringUtil.isNullSpacesOrEmpty(INITIAL_CONTEXT_FACTORY))
						env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
					InitialContext context = new InitialContext(env);
					datasource =  (DataSource) context.lookup(JNDI_NAME);
				}catch (Exception ex) {
					throw new RuntimeException(ex.getMessage(), ex);
				}
			}
		}
		return datasource;
	}
	
	private static void populateDatasourceElementProperties() {
		DatasourceElement dsElement = MappingXmlWrapper.getBootstrapInstance().getDatasourceElement();
		JNDI_NAME = dsElement.getJndi();
		INITIAL_CONTEXT_FACTORY = dsElement.getInitialContextFactory();
		PROVIDER_URL = dsElement.getProviderUrl();		
	}
}
