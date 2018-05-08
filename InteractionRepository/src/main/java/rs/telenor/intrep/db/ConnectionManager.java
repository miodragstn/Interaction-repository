package rs.telenor.intrep.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class ConnectionManager {
	public static ConnectionManager instance = null;
	
	private static final String USERNAME = "IR";
//	Pass za test
	private static final String PASSWORD = "Telen_20161017";
//	Pass za produkciju
//	private static final String PASSWORD = "Telen_20161125";
	private static final String CONN_STRING =
			"jdbc:oracle:thin:@intrep-ora-d-01.telenor.rs:2016/intrep.dev";
//			"jdbc:oracle:thin:@intrep-ora-p-01.telenor.rs:2016/intrep";
	
	private Connection conn = null;
	
	private ConnectionManager() {
		
	}
	
	private static final Logger serviceLog = Logger
            .getLogger(ConnectionManager.class);

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
	
	public boolean openCEPConnection(Logger log) {
		
		String dsName = "java:/TEST_DWH_IR";
		
		try {
			DataSource ds = (DataSource) InitialContext.doLookup(dsName);		
			conn = ds.getConnection();			
			return true;		
						
		}catch (SQLException e) {
			serviceLog.error(e.getMessage(),e) ;
			return false;
		} catch (NamingException e) {
			serviceLog.error(e.getMessage(),e) ;
			return false;
		}
	}
	
	public Connection getCEPConnection(Logger log){
		
		try {
			
			if (conn == null) {
				if (openCEPConnection(log)) {
					serviceLog.info("Connection opened_MX");
					return conn;
				}
				else {
					return null;
				}
			}
			else {			
				return conn;
			}
			
		} catch (Exception e) {
			serviceLog.error(e.getMessage(),e) ;
		}
		
		return conn;
		
	}
	public Connection getConnection() {
		try {
			if (conn == null || !conn.isValid(0)) {
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
		} catch (SQLException e) {
			return null;
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