package rs.telenor.intrep.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import rs.telenor.intrep.db.ConnectionManager;
import rs.telenor.intrep.db.beans.NotificationInstance;
import rs.telenor.intrep.db.beans.NotificationInstanceList;
import rs.telenor.intrep.db.beans.SubscriptionProfile;

import org.apache.log4j.Logger;

public class NotificationManager {
//	private static Connection conn = ConnectionManager.getInstance().getConnection();
	private static Connection conn;
	private static LinkedHashMap<String, NotificationInstanceList> subscriptionNotifications;
	
	
	//za standalone app - u svrhu testiranja
	
		public static synchronized void database2hashmap() {

			
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;		
			String query = null;
			//String dbschema = System.getProperty("dbschema", "fuppdev");
			String tableName = "SUBSCRIPTION_PROFILE";

			if (subscriptionNotifications == null ) {
				//serviceLog.info("database2hashmap():  monitoredMap and monitoredSubscriber maps are not intialized, initializing them.");
				subscriptionNotifications  = new LinkedHashMap<String, NotificationInstanceList>(3000000);

				

				System.out.println("database2hashmap(): Load notifications from database... to subscriptionNotifications");
				int i = 0;
				try {
					
					conn = ConnectionManager.getInstance().getConnection();
					
					System.out.println("database2hashmap(): Connected to database.");

					query = "select  	INTERACTION_ID,"+
								        "CO_ID,"+
								        "MSISDN," +
								        "MESSAGE_TYPE_ID," +
								        "MESSAGE_CHANNEL_CD,"+
								        "NOTIFICATION_DT,"+
								        "NOTIFICATION_STATUS_ID,"+
								        "CREATION_DT "+
								"from notification n "+
								"where N.NOTIFICATION_DT >= to_char(add_months(trunc(sysdate),-1), 'yyyy-mm-dd') "+
								"and N.NOTIFICATION_STATUS_ID = 2";

					System.out.println("database2hashmap(): Prepare Query: " + query);
					stmt = conn.prepareStatement(query);

					rs = stmt.executeQuery();
					System.out.println("database2hashmap(): Query Executed...");
					
					System.out.println("database2hashmap():" + LocalDateTime.now().toString()+" Loading subscribers into memory...");
					Long interactionId;						
					Long co_id;								
					String msisdn;	
					int messageTypeId;
					String messageChannelCd;
					String notificationDt;
					int notificationStatusId;
					String creationDt;
					NotificationInstance notificationInstance;
					NotificationInstanceList notificationInstanceList;
					while (rs.next()) {
						i++;			
						interactionId=rs.getLong(1);						
						co_id=rs.getLong(2);								
						msisdn=rs.getString(3);	
						messageTypeId = rs.getInt(4);
						messageChannelCd=rs.getString(5);
						notificationDt=rs.getString(6);
						notificationStatusId = rs.getInt(7);
						creationDt=rs.getString(8);

						notificationInstance = new NotificationInstance(interactionId, messageTypeId, notificationStatusId, msisdn, messageChannelCd);
						
						if (subscriptionNotifications.get(msisdn) == null) {
							notificationInstanceList = new NotificationInstanceList();
							notificationInstanceList.getmNotificationInstanceList().add(notificationInstance);							
							subscriptionNotifications.put(msisdn, notificationInstanceList);							
						}
						else {
							subscriptionNotifications.get(msisdn).getmNotificationInstanceList().add(notificationInstance);
						}
						
					}				

				} catch (SQLException e) {			
					e.printStackTrace();
				} 
				finally {
					//Closing database resources
					
					if (rs != null)
						try {
							System.out.println("database2hashmap():" + LocalDateTime.now().toString()+" Load FINISHED. No. of notifications added  = "+ i);
							rs.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					if (stmt != null) {
						try {
							System.out.println("database2hashmap(): Closing statement: " + stmt);
							stmt.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (conn != null)
						try {
							System.out.println("database2hashmap(): Disconnect from database.");
							conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

				}
			}
		}
	
	public static String resolveMsgChannel(String msisdn) throws SQLException{
		
		if (conn==null){
			conn = ConnectionManager.getInstance().getConnection();
		}
		
		String sqlResolveChnnl = "SELECT  nvl(REGISTRATION_DT,-1) as REGISTRATION_DT "+
								 "FROM    SUBSCRIPTION_PROFILE p1 "+
								 "WHERE   MSISDN = ?";
		
		ResultSet rsNotif = null;
		String regDate = null;
		String retValue = "PUSH";
		
		try(PreparedStatement pstNotif = conn.prepareStatement(sqlResolveChnnl, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
			
			pstNotif.setString(1, msisdn);
			rsNotif = pstNotif.executeQuery();
			
			if (rsNotif.isBeforeFirst()) {
				rsNotif.first();
				regDate = rsNotif.getString("REGISTRATION_DT");
				retValue = (regDate.equalsIgnoreCase("-1")) ? "SMS" : "PUSH";
			}
		}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (rsNotif != null) rsNotif.close();
			}
		
		return retValue;
		
	}
	
public static String resolveMsgChannel(String msisdn, Logger log) throws SQLException{
		
		if (conn==null){
			conn = ConnectionManager.getInstance().getCEPConnection(log);
		}
		
		String sqlResolveChnnl = "SELECT  nvl(REGISTRATION_DT,-1) as REGISTRATION_DT "+
								 "FROM    SUBSCRIPTION_PROFILE p1 "+
								 "WHERE   MSISDN = ?";
		
		ResultSet rsNotif = null;
		String regDate = null;
		String retValue = "PUSH";
		
		try(PreparedStatement pstNotif = conn.prepareStatement(sqlResolveChnnl, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
			
			pstNotif.setString(1, msisdn);
			rsNotif = pstNotif.executeQuery();
			
			if (rsNotif.isBeforeFirst()) {
				rsNotif.first();
				regDate = rsNotif.getString("REGISTRATION_DT");
				retValue = (regDate.equalsIgnoreCase("-1")) ? "SMS" : "PUSH";
			}
		}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (rsNotif != null) rsNotif.close();
			}
		
		return retValue;
		
	}

	public static void writeNotification2DB(NotificationInstance nInst) throws SQLException{
		if(nInst != null){
//			String ntfInsrt = "INSERT INTO IR.NOTIFICATION "+
//							   "("+
//							   "INTERACTION_ID, "+
//							   "CO_ID, "+
//							   "MSISDN, "+
//							   "MESSAGE_TYPE_ID, "+
//							   "MESSAGE_CHANNEL_CD, "+
//							   "NOTIFICATION_DT, "+
//							   "NOTIFICATION_STATUS_ID, "+
//							   "CREATION_DT "+
//							   ") "+
//							   "VALUES "+
//							   "(" +
//							   	"?,?,?,?,?,TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'),?,TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'))";
			String ntfInsrt = "{call WriteNotificationEOB(?,?, ?,?,?)}";
			try(
					PreparedStatement psInstNotif = conn.prepareStatement(ntfInsrt);
					){
				psInstNotif.setLong(1, nInst.getInteractionInstanceID());
				psInstNotif.setInt(2, nInst.getCoID());
				psInstNotif.setString(3, nInst.getMsisdn());
				psInstNotif.setInt(4, nInst.getMssgTypeId());
				psInstNotif.setString(5, nInst.getMssgChnnlCd());
//				psInstNotif.setInt(6, nInst.getNotificationStatusId());
				psInstNotif.execute();
			}
		}
		
	}
	
	public static void writeNotification2DB(NotificationInstance nInst, Logger log) throws SQLException{		
		if(nInst != null){
//			String ntfInsrt = "INSERT INTO IR.NOTIFICATION "+
//							   "("+
//							   "INTERACTION_ID, "+
//							   "CO_ID, "+
//							   "MSISDN, "+
//							   "MESSAGE_TYPE_ID, "+
//							   "MESSAGE_CHANNEL_CD, "+
//							   "NOTIFICATION_DT, "+
//							   "NOTIFICATION_STATUS_ID, "+
//							   "CREATION_DT "+
//							   ") "+
//							   "VALUES "+
//							   "(" +
//							   	"?,?,?,?,?,TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'),?,TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'))";
			if (conn==null){
				conn = ConnectionManager.getInstance().getCEPConnection(log);
			}
			String ntfInsrt = "{call WriteNotificationEOB(?,?, ?,?,?)}";
			try(
					PreparedStatement psInstNotif = conn.prepareStatement(ntfInsrt);
					){
				psInstNotif.setLong(1, nInst.getInteractionInstanceID());
				psInstNotif.setInt(2, nInst.getCoID());
				psInstNotif.setString(3, nInst.getMsisdn());
				psInstNotif.setInt(4, nInst.getMssgTypeId());
				psInstNotif.setString(5, nInst.getMssgChnnlCd());
//				psInstNotif.setInt(6, nInst.getNotificationStatusId());
				psInstNotif.execute();
				
			}
		}
		
	}
	
}
