package com.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	private final static String DB_URL = "jdbc:mysql://localhost:3306/library";
	private final static String USER = "root";
	private final static String PASS = "root";
	/*private final static String USER = "SYSTEM";
	private final static String PASS = "admin@123"; */
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(DB_URL, USER, PASS);
	}

}
