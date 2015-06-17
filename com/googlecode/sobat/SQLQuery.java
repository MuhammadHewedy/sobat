package com.googlecode.sobat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.sobat.mappings.Mapper;
import com.googlecode.sobat.mappings.ObjectMapping;
import com.googlecode.sobat.ql.QueryConstants;
import com.googlecode.sobat.util.ReflectionUtil;

public class SQLQuery implements Query {

	private String sqlQuery;
	private Connection connection;
	private Class clazz;
	Logger logger = Logger.getLogger(SQLQuery.class.getName());
	
	public SQLQuery(String sqlQuery, Connection connection, Class clazz) {
		this.sqlQuery = sqlQuery;
		this.connection = connection;
		this.clazz = clazz;
	}
	
	public List execute() throws SQLException{
		logger.info("Sobat : " + sqlQuery);
		List list = null;
		ResultSet rs = connection.prepareStatement(sqlQuery).executeQuery();
		
		if (sqlQuery.contains(QueryConstants.ALL)) {
			list = new ArrayList();
			String clazzName = ReflectionUtil.getClassName(clazz);
			ObjectMapping objectMapping = Mapper.getClassMapping(clazz);
			try {
				ObjectResultSet.fillObjectList(rs,objectMapping, clazzName, list);
			}catch(Exception ex) {
				throw new SQLException(ex.getMessage(), ex);
			}
			
		}else {
			list = new ArrayList<Object[]>();
			ObjectResultSet.fillObjectArrayList(rs, list);
		}
		
		return list;
	}

}
