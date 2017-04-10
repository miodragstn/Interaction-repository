package rs.telenor.intrep.db.tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import rs.telenor.intrep.db.ConnectionManager;
import org.apache.log4j.Logger;

class Key {
	String tableName;
	Long currentValue;
	Long maxValue;
}

public class SurrogateKeyManager {
//	private static Connection conn = ConnectionManager.getInstance().getConnection();
	private static Connection conn;
	public static SurrogateKeyManager keyInstance = null; 
	private HashMap<String, Key> keys;
	
	private SurrogateKeyManager() {	
		if (conn == null) conn = ConnectionManager.getInstance().getConnection();
		if (keys == null) keys = new HashMap<String, Key>();		
	}
	
	private SurrogateKeyManager(Logger log) {	
		if (conn == null) conn = ConnectionManager.getInstance().getCEPConnection(log);
		if (keys == null) keys = new HashMap<String, Key>();		
	}
		
	
	public static SurrogateKeyManager getInstance() {
		if (keyInstance == null) keyInstance = new SurrogateKeyManager();
		return keyInstance;
	}
	
	public static SurrogateKeyManager getInstance(Logger log) {
		if (keyInstance == null) keyInstance = new SurrogateKeyManager(log);
		return keyInstance;
	}
	
	private Long getDBKey(String tableName) throws SQLException {
		Long key = -1L;
		try (
				
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery("SELECT " + tableName + "_SEQ.NEXTVAL as DBKEY FROM DUAL");
			) {
			
			if (rs.first()) 
				key = rs.getLong("DBKEY");
			return key;
		}
	}
	
	public Long getKeyValue(String tableName) {
		Key key = keys.get(tableName);
		long keyValue = -1;
		if (key != null) {
			keyValue = key.currentValue + 1;
			if (keyValue == key.maxValue) {
				try {
					key.currentValue = getDBKey(tableName) ;
					key.maxValue = key.currentValue + 1000L;
					keys.put(tableName, key);
					return keyValue;
				} catch (SQLException e) {
					return -1L;
				}
				
			}
			else {
				key.currentValue++;
				keys.put(tableName, key);
				keyValue = key.currentValue;
				return keyValue;
			}			
		}
		else {
			key = new Key();
			try {
				key.currentValue = getDBKey(tableName) ;
				key.maxValue = key.currentValue + 1000L;
				keys.put(tableName, key);
				keyValue = key.currentValue;
				return keyValue;
			} catch (SQLException e) {
				e.printStackTrace();
				return -1L;
			}
		}
	}
	
}
