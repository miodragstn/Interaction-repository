package rs.telenor.intrep.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	public static ConnectionManager instance = null;
	
	private static final String USERNAME = "IR";
	private static final String PASSWORD = "Telen_20161017";
	private static final String CONN_STRING =
			"jdbc:oracle:thin:@intrep-ora-d-01.telenor.rs:2016/intrep.dev";
	
	private Connection conn = null;
	
	private ConnectionManager() {
		
	}
	
	
	public static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();			
		}
		return instance;
	}
	
		
	public boolean openConnection() {
		try {
					
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);			
			return true;		
						
		}catch (SQLException e) {
			System.err.println(e);
			return false;
		}
	}
	
	public Connection getConnection() {
		if (conn == null) {
			if (openConnection()) {
				System.out.println("Connection opened");
				return conn;
			}
			else {
				return null;
			}
		}
		else {
			return conn;
		}
	}
	
	public void close() {
		System.out.println("Closing connection");
		try {
			if (conn != null) {			

				conn.close();
				conn = null;
			}
		} catch (Exception e) {

		}

	}
	
}