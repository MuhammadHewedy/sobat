package com.googlecode.sobat;

import java.sql.SQLException;
import java.util.List;

public interface Query {
	public List execute() throws SQLException;
}
