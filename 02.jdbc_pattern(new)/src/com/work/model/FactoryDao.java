package com.work.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 모든 DAO 클래스에서 사용하기 위한 
 * --Connection 반환, 
 * --close() 자원해제를 담당하는 기능으로만 분리설계
 * --모든 DAO 클래스에서 Connection, close(conn, stmt, rs)
 * 
 * -- Singleton pattern
 * 1. private 생성자
 * 2. private static 클래스이름 instance = new 클래스이름(); 
 * 3. instance 반환 메서드
 * @author jun
 *
 */
public class FactoryDao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
	private String user = "scott";
	private String password = "tiger";
	
	private FactoryDao() {
		try {
			Class.forName(driver);
			System.out.println("[성공] 드라이버 로딩");
		} catch (ClassNotFoundException e) {
			System.out.println("[오류] 드라이버 로딩");
			e.printStackTrace();
		}	
	}
	private static FactoryDao instance = new FactoryDao(); // 2. private static 클래스이름 instance = new 클래스이름(); 
	
	public static FactoryDao getInstance() { // 3. instance 반환 메서드
		return instance;
	}

	// DB 연결 Connection 반환 메서드
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return conn;
	}
	
	public void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if(rs != null) {
			rs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(stmt != null) {
				stmt.close();
			}				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(conn != null) {
				conn.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(Connection conn, Statement stmt) {
		close(conn, stmt, null);
//		try {
//			if(stmt != null) {
//				stmt.close();
//			}				
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			if(conn != null) {
//				conn.close();
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
