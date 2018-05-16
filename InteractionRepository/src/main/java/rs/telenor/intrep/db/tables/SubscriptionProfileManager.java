package rs.telenor.intrep.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import rs.telenor.intrep.db.ConnectionManager;
import rs.telenor.intrep.db.beans.SubscriptionProfile;

public class SubscriptionProfileManager {
	public volatile static ConcurrentHashMap<String, SubscriptionProfile> subscriptionProfileMap = null;

	private SubscriptionProfileManager(){}

	public static synchronized void database2hashmap(Logger serviceLog, String dsJNDIName) {

		DataSource ds = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dsName = null;
		String query = null;
		//String dbschema = System.getProperty("dbschema", "fuppdev");
		String tableName = "SUBSCRIPTION_PROFILE";

		if (subscriptionProfileMap == null ) {
			//serviceLog.info("database2hashmap():  monitoredMap and monitoredSubscriber maps are not intialized, initializing them.");
			SubscriptionProfileManager.subscriptionProfileMap = new ConcurrentHashMap<String, SubscriptionProfile>(3000000);


			dsName = dsJNDIName;

			serviceLog.info("database2hashmap(): Load subscribers from database..."+ dsName + " to customerProfileMap");
			int i = 0;
			try {
				ds = (DataSource) InitialContext.doLookup(dsName);

				conn = ds.getConnection();
				serviceLog.info("database2hashmap(): Connected to database: "+ dsName);

				query = "SELECT CUSTOMER_ID,"+
						"CUSTCODE,"+
						"CO_ID,"+
						"MSISDN,"+
						"MACRO_SEGMENT,"+
						"SUBSCRIPTION_TYPE_CD,"+
						"REGISTRATION_DT,"+
						"ONBOARD_DT,"+
						"LAST_ACTION1_DT,"+
						"LAST_ACTION2_DT,"+
						"LAST_ACTION3_DT,"+
						"LAST_APP_DT,"+
						"ACTIVATION_DT,"+
						"DEACTIVATION_DT,"+
						"HANDSET_DT,"+
						"TUTORIAL_DT,"+
						"RENEWAL_DT,"+
						"NOTIFICATION_DT,"+
						"PHONE_TYPE_CD,"+
						"API,"+
						"APK FROM "+ tableName;

				serviceLog.info("database2hashmap(): Prepare Query: " + query);
				stmt = conn.prepareStatement(query);

				rs = stmt.executeQuery();
				serviceLog.info("database2hashmap(): Query Executed...");

				serviceLog.info("database2hashmap(): Loading subscribers into memory...");

				while (rs.next()) {
					i++;			
					Long customer_id=rs.getLong(1);
					String cust_code=rs.getString(2);
					Long co_id=rs.getLong(3);
					String msisdn=rs.getString(4);
					String macro_segment=rs.getString(5);
					String subscription_type_cd=rs.getString(6);
					String registration_dt=rs.getString(7);
					String onboard_dt=rs.getString(8);
					if (onboard_dt == null || onboard_dt.length() == 0)
						onboard_dt = "";
					String last_action1_dt=rs.getString(9);
					if (last_action1_dt == null || last_action1_dt.length() == 0)
						last_action1_dt = "";
					String last_action2_dt=rs.getString(10);
					if (last_action2_dt == null || last_action2_dt.length() == 0)
						last_action2_dt = "";
					String last_action3_dt=rs.getString(11);
					if (last_action3_dt == null || last_action3_dt.length() == 0)
						last_action3_dt = "";					
					String last_app_dt=rs.getString(12);
					if (last_app_dt == null || last_app_dt.length() == 0)
						last_app_dt = "";					
					String activation_dt=rs.getString(13);					
					String handset_dt = rs.getString(14);
					String tutorial_dt = rs.getString(15);
					String renewal_dt = rs.getString(16);
					String notification_dt = rs.getString(17);
					String phone_type = rs.getString(18);
					String phone_api = rs.getString(19);
					String phone_apk = rs.getString(20);

					SubscriptionProfile sub = new SubscriptionProfile(customer_id, cust_code, co_id, msisdn,
							macro_segment, subscription_type_cd,
							registration_dt, onboard_dt, last_action1_dt,
							last_action2_dt, last_action3_dt, last_app_dt,
							activation_dt, handset_dt, tutorial_dt,
							renewal_dt, notification_dt, phone_type, phone_api, phone_apk);

					subscriptionProfileMap.put(msisdn, sub);
					serviceLog.info("database2hashmap(): Customer Profile from "
							+ tableName
							+ " with MSISDN: "
							+ msisdn
							+ " saved in memory!");
				}

				//			serviceLog.warn("database2hashmap(): Load FINISHED. No. of subscriptions added  = "+ i);
				//			rs.close();
				//			serviceLog.info("database2hashmap(): Closing statement: " + stmt);
				//			stmt.close();
				//			serviceLog.info("database2hashmap(): Disconnect from database: "+ dsName);
				//			conn.close();



			} catch (SQLException e) {			
				serviceLog.error(e.getMessage(), e);
			} catch (NamingException e) {
				serviceLog.error(e.getMessage(), e);
			}
			finally {
				//Closing database resources
				if (rs != null)
					try {
						serviceLog.warn("database2hashmap(): Load FINISHED. No. of subscriptions added  = "+ i);
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						serviceLog.error(e.getMessage(), e);
					}
				if (stmt != null) {
					try {
						serviceLog.info("database2hashmap(): Closing statement: " + stmt);
						stmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						serviceLog.error(e.getMessage(), e);
					}
				}
				if (conn != null)
					try {
						serviceLog.info("database2hashmap(): Disconnect from database: "+ dsName);
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						serviceLog.error(e.getMessage(), e);
					}

			}
		}
	}
	
	//za standalone app - u svrhu testiranja
	public static synchronized void database2hashmap() {

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		String query = null;
		//String dbschema = System.getProperty("dbschema", "fuppdev");
		String tableName = "SUBSCRIPTION_PROFILE";

		if (subscriptionProfileMap == null ) {
			//serviceLog.info("database2hashmap():  monitoredMap and monitoredSubscriber maps are not intialized, initializing them.");
			SubscriptionProfileManager.subscriptionProfileMap = new ConcurrentHashMap<String, SubscriptionProfile>(3000000);

			

			System.out.println("database2hashmap(): Load subscribers from database... to customerProfileMap");
			int i = 0;
			try {
				
				conn = ConnectionManager.getInstance().getConnection();
				
				System.out.println("database2hashmap(): Connected to database.");

				query = "SELECT CUSTOMER_ID,"+
						"CUSTCODE,"+
						"CO_ID,"+
						"MSISDN,"+
						"MACRO_SEGMENT,"+
						"SUBSCRIPTION_TYPE_CD,"+
						"REGISTRATION_DT,"+
						"ONBOARD_DT,"+
						/*"LAST_ACTION1_DT,"+
						"LAST_ACTION2_DT,"+
						"LAST_ACTION3_DT,"+*/
						"LAST_APP_DT,"+
						"ACTIVATION_DT,"+
						//"DEACTIVATION_DT,"+
						"HANDSET_DT,"+
						"TUTORIAL_DT,"+
						"RENEWAL_DT,"+
						"NOTIFICATION_DT,"+
						"PHONE_TYPE_CD,"+
						"API,"+
						"APK FROM "+ tableName;

				System.out.println("database2hashmap(): Prepare Query: " + query);
				stmt = conn.prepareStatement(query);

				rs = stmt.executeQuery();
				System.out.println("database2hashmap(): Query Executed...");
				
				System.out.println("database2hashmap():" + LocalDateTime.now().toString()+" Loading subscribers into memory...");

				while (rs.next()) {
					i++;			
					Long customer_id=rs.getLong(1);
					String cust_code=rs.getString(2);
					Long co_id=rs.getLong(3);		
					if (rs.getString(4) == null) continue;
					String msisdn=rs.getString(4);					
					String macro_segment=rs.getString(5);
					String subscription_type_cd=rs.getString(6);
					String registration_dt=rs.getString(7);
//					String onboard_dt=rs.getString(8);
//					if (onboard_dt == null || onboard_dt.length() == 0)
//						onboard_dt = "";
//					String last_action1_dt=rs.getString(9);
//					if (last_action1_dt == null || last_action1_dt.length() == 0)
//						last_action1_dt = "";
//					String last_action2_dt=rs.getString(10);
//					if (last_action2_dt == null || last_action2_dt.length() == 0)
//						last_action2_dt = "";
//					String last_action3_dt=rs.getString(11);
//					if (last_action3_dt == null || last_action3_dt.length() == 0)
//						last_action3_dt = "";					
					String last_app_dt=rs.getString(8);
					if (last_app_dt == null || last_app_dt.length() == 0)
						last_app_dt = "";					
					String activation_dt=rs.getString(9);
					if (activation_dt == null || activation_dt.length() == 0)
						activation_dt = "";
					String handset_dt = rs.getString(10);
					if (handset_dt == null || handset_dt.length() == 0)
						handset_dt = "";					
					String renewal_dt = rs.getString(11);
					if (renewal_dt == null || renewal_dt.length() == 0)
						renewal_dt = "";
					String notification_dt = rs.getString(12);
					if (notification_dt == null || notification_dt.length() == 0)
						notification_dt = "";
					String phone_type = rs.getString(13);
					String phone_api = rs.getString(14);
					String phone_apk = rs.getString(15);
										

					SubscriptionProfile sub = new SubscriptionProfile(customer_id, cust_code, co_id, msisdn,
							macro_segment, subscription_type_cd,registration_dt, last_app_dt,activation_dt, handset_dt, 
							renewal_dt, notification_dt, phone_type, phone_api, phone_apk);

					subscriptionProfileMap.put(msisdn, sub);
//					System.out.println("database2hashmap(): Customer Profile from "
//							+ tableName
//							+ " with MSISDN: "
//							+ msisdn
//							+ " saved in memory!");
				}

				//			serviceLog.warn("database2hashmap(): Load FINISHED. No. of subscriptions added  = "+ i);
				//			rs.close();
				//			serviceLog.info("database2hashmap(): Closing statement: " + stmt);
				//			stmt.close();
				//			serviceLog.info("database2hashmap(): Disconnect from database: "+ dsName);
				//			conn.close();



			} catch (SQLException e) {			
				e.printStackTrace();
			} 
			finally {
				//Closing database resources
				
				if (rs != null)
					try {
						System.out.println("database2hashmap():" + LocalDateTime.now().toString()+" Load FINISHED. No. of subscriptions added  = "+ i);
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
	

	public static synchronized void applySubscriptionStatus(Logger serviceLog, String dsJNDIName, int coId, 
			int customerId, String custCode, String msisdn,
			String segment, String subsType, String cid,
			String status, String statusDate, int statusReason) {
		DataSource ds = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dsName = null;
		String query = null;
		//String dbschema = System.getProperty("dbschema", "fuppdev");
		String tableName = "SUBSCRIPTION_PROFILE";




		dsName = dsJNDIName;

		serviceLog.info("applySubscriptionStatus(): start");
		int i = 0;
		try {
			ds = (DataSource) InitialContext.doLookup(dsName);

			conn = ds.getConnection();
			serviceLog.info("applySubscriptionStatus(): Connected to database: "+ dsName);

			//Aktivacija subscription-a. Treba ga insertovati u bazu, eventualno update-ovati, ako vec postoji taj MSISDN u tabeli (kao rezultat neke greske ) 
			if (status.equalsIgnoreCase("A")) {

				serviceLog.info("applySubscriptionStatus(): Prepare MERGE Statement: " + query);

				String qCoId, qCustomerId, qCustCode, qMsisdn,qSegment, qSubsType, qCid, qStatus, qStatusDate;

				if (customerId != 0) {
					qCustomerId  = Integer.toString(customerId);
				}
				else {
					qCustomerId = "NULL";
				}
				if (custCode != null && !custCode.isEmpty()) {
					qCustCode = "'"+custCode+"'";
				}
				else {
					qCustCode = "NULL";
				}
				if (coId != 0) {
					qCoId = Integer.toString(coId);
				}
				else {
					qCoId = "NULL";
				}
				if (msisdn != null && !msisdn.isEmpty()) {
					qMsisdn = "'"+msisdn+"'";
				}
				else {
					qMsisdn = "NULL";
				}
				if (segment != null && !segment.isEmpty()) {
					qSegment = "'"+segment+"'";
				}
				else {
					qSegment = "NULL";				
				}
				if (subsType != null && !subsType.isEmpty()) {
					qSubsType = "'"+subsType+"'";
				}
				else {
					qSubsType = "NULL";
				}
				if (statusDate != null && !statusDate.isEmpty()) {
					qStatusDate = "'"+statusDate+"'";
				}
				else {
					qStatusDate = "NULL";
				}

				query = "MERGE INTO SUBSCRIPTION_PROFILE sp"+
						" USING (SELECT '"+ msisdn + "' MSISDN FROM dual ) pom"  +
						" ON  (sp.MSISDN = pom.MSISDN) " +
						" WHEN MATCHED THEN UPDATE SET "+
						" CUSTOMER_ID="+qCustomerId+", CUSTCODE="+qCustCode+", CO_ID="+qCoId+", MACRO_SEGMENT="+qSegment+", SUBSCRIPTION_TYPE_CD="+qSubsType+", ACTIVATION_DT="+qStatusDate +
						" WHEN NOT MATCHED THEN INSERT"
						+ " (CUSTOMER_ID, CUSTCODE, CO_ID, MSISDN, MACRO_SEGMENT, SUBSCRIPTION_TYPE_CD, ACTIVATION_DT) " +
						"VALUES (" + qCustomerId+", " + qCustCode+", " + qCoId + ", " + qMsisdn + ", " + qSegment + ", "+ qSubsType + ", " + qStatusDate + ")";




				stmt = conn.prepareStatement(query);


				rs = stmt.executeQuery();
				serviceLog.info("applySubscriptionStatus(): Merge Executed...");	

				SubscriptionProfile pomSub = new SubscriptionProfile(msisdn);
				pomSub.setmCoId(coId);
				pomSub.setmCustomerId(customerId);
				pomSub.setmCustCode(custCode);
				pomSub.setmSubscriptionTypeCd(subsType);
				pomSub.setmMacroSegment(segment);
				pomSub.setmActivationDate(statusDate);
				subscriptionProfileMap.put(msisdn, pomSub);

			}


		} catch (SQLException e) {			
			serviceLog.error(e.getMessage(), e);
		} catch (NamingException e) {
			serviceLog.error(e.getMessage(), e);
		}
		finally {
			//Closing database resources
			if (rs != null)
				try {
					serviceLog.warn("applySubscriptionStatus(): FINISHED");
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					serviceLog.error(e.getMessage(), e);
				}
			if (stmt != null) {
				try {
					serviceLog.info("applySubscriptionStatus(): Closing statement: " + stmt);
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					serviceLog.error(e.getMessage(), e);
				}
			}
			if (conn != null)
				try {
					serviceLog.info("applySubscriptionStatus(): Disconnect from database: "+ dsName);
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					serviceLog.error(e.getMessage(), e);
				}

		}
		
		
		
	}
	
	//Verzija za standalone aplikaciju - za testiranje
	public static synchronized void applySubscriptionStatus(int coId, int customerId, String custCode, String msisdn,
															String segment, String subsType, String cid,
															String status, String statusDate, int statusReason) 
	{		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		String query = null;
		//String dbschema = System.getProperty("dbschema", "fuppdev");
		String tableName = "SUBSCRIPTION_PROFILE";



		

		System.out.println("applySubscriptionStatus(): start");
		int i = 0;
		try {			

			conn = ConnectionManager.getInstance().getConnection();
			System.out.println("applySubscriptionStatus(): Connected to database.");

			//Aktivacija subscription-a. Treba ga insertovati u bazu, eventualno update-ovati, ako vec postoji taj MSISDN u tabeli (kao rezultat neke greske ) 
			if (status.equalsIgnoreCase("A")) {

				System.out.println("applySubscriptionStatus(): Prepare MERGE Statement: " + query);

				String qCoId, qCustomerId, qCustCode, qMsisdn,qSegment, qSubsType, qCid, qStatus, qStatusDate;

				if (customerId != 0) {
					qCustomerId  = Integer.toString(customerId);
				}
				else {
					qCustomerId = "NULL";
				}
				if (custCode != null && !custCode.isEmpty()) {
					qCustCode = "'"+custCode+"'";
				}
				else {
					qCustCode = "NULL";
				}
				if (coId != 0) {
					qCoId = Integer.toString(coId);
				}
				else {
					qCoId = "NULL";
				}
				if (msisdn != null && !msisdn.isEmpty()) {
					qMsisdn = "'"+msisdn+"'";
				}
				else {
					qMsisdn = "NULL";
				}
				if (segment != null && !segment.isEmpty()) {
					qSegment = "'"+segment+"'";
				}
				else {
					qSegment = "NULL";				
				}
				if (subsType != null && !subsType.isEmpty()) {
					qSubsType = "'"+subsType+"'";
				}
				else {
					qSubsType = "NULL";
				}
				if (statusDate != null && !statusDate.isEmpty()) {
					qStatusDate = "'"+statusDate+"'";
				}
				else {
					qStatusDate = "NULL";
				}

				query = "MERGE INTO SUBSCRIPTION_PROFILE sp"+
						" USING (SELECT '"+ msisdn + "' MSISDN FROM dual ) pom"  +
						" ON  (sp.MSISDN = pom.MSISDN) " +
						" WHEN MATCHED THEN UPDATE SET "+
						" CUSTOMER_ID="+qCustomerId+", CUSTCODE="+qCustCode+", CO_ID="+qCoId+", MACRO_SEGMENT="+qSegment+", SUBSCRIPTION_TYPE_CD="+qSubsType+", ACTIVATION_DT="+qStatusDate +
						" WHEN NOT MATCHED THEN INSERT"
						+ " (CUSTOMER_ID, CUSTCODE, CO_ID, MSISDN, MACRO_SEGMENT, SUBSCRIPTION_TYPE_CD, ACTIVATION_DT) " +
						"VALUES (" + qCustomerId+", " + qCustCode+", " + qCoId + ", " + qMsisdn + ", " + qSegment + ", "+ qSubsType + ", " + qStatusDate + ")";



				
				stmt = conn.prepareStatement(query);


				rs = stmt.executeQuery();
				System.out.println("applySubscriptionStatus(): Merge Executed...");	

				SubscriptionProfile pomSub = new SubscriptionProfile(msisdn);
				pomSub.setmCoId(coId);
				pomSub.setmCustomerId(customerId);
				pomSub.setmCustCode(custCode);
				pomSub.setmSubscriptionTypeCd(subsType);
				pomSub.setmMacroSegment(segment);
				pomSub.setmActivationDate(statusDate);
				subscriptionProfileMap.put(msisdn, pomSub);

			}


		} catch (SQLException e) {			
			e.printStackTrace();
		} 
		finally {
			//Closing database resources
			if (rs != null)
				try {
					System.out.println("applySubscriptionStatus() FINISHED. No. of subscriptions added  = "+ i);
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (stmt != null) {
				try {
					System.out.println("applySubscriptionStatus(): Closing statement: " + stmt);
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null)
				try {
					System.out.println("applySubscriptionStatus(): Disconnect from database.");
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		
		
		
	}	
}

