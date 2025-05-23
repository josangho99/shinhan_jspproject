package com.codingshop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	// Oracle DB연결을 Util로 만들었음
	public static Connection getConnection() {
		Connection conn = null;
		//String url = "jdbc:oracle:thin:@192.168.0.18:1521:xe", userid = "hr", userpassword = "hr"; //강사컴
		String url = "jdbc:oracle:thin:@192.168.0.94:1521:xe", userid = "codingshop", userpassword = "coding";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, userid, userpassword);
		} catch (SQLException e) {
			// TODO: handle exception
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	// DB 연결시 사용한 자원해제
	public static void dbDisconnect(Connection conn, Statement st, ResultSet rs) {
		try {
			if(rs != null)rs.close();
			if(st != null)st.close();
			if(conn != null)conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
