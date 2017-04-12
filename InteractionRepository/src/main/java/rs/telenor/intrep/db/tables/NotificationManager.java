package rs.telenor.intrep.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import rs.telenor.intrep.db.ConnectionManager;
import rs.telenor.intrep.db.beans.NotificationInstance;

public class NotificationManager {
	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	public static synchronized String resolveMsgChannel(String msisdn) throws SQLException{
		String sqlResolveChnnl = "SELECT  nvl(REGISTRATION_DT,-1) as REGISTRATION_DT "+
								 "FROM    SUBSCRIPTION_PROFILE p1"+
								 "WHERE   MSISDN = ?";
		
		ResultSet rsNotif = null;
		String regDate = null;
		
		try(PreparedStatement pstNotif = conn.prepareStatement(sqlResolveChnnl, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
			
			pstNotif.setString(1, msisdn);
			rsNotif = pstNotif.executeQuery();
			
			if (rsNotif.isBeforeFirst()) {
				rsNotif.first();
				regDate = rsNotif.getString("REGISTRATION_DT");
			}
		}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (rsNotif != null) rsNotif.close();
			}
		
		return (regDate == "-1") ? "SMS" : "PUSH";
		
	}

	public static synchronized void writeNotification2DB(NotificationInstance nInst) throws SQLException{
		if(nInst != null){
			String ntfInsrt = "INSERT INTO IR.NOTIFICATION "+
							   "("+
							   "INTERACTION_ID, "+
							   "CO_ID, "+
							   "MSISDN, "+
							   "MESSAGE_TYPE_ID, "+
							   "MESSAGE_CHANNEL_CD, "+
							   "NOTIFICATION_DT, "+
							   "NOTIFICATION_STATUS_ID, "+
							   "CREATION_DT, "+
							   ") "+
							   "VALUES "+
							   "(" +
							   	"?,?,?,?,?,TO_CHAR(SYSDATE(),'YYYY-MM-DD HH24:MI:SS'),?,SYSDATE() "+
							   ")";
			try(
					PreparedStatement psInstNotif = conn.prepareStatement(ntfInsrt);
					){
				psInstNotif.setLong(1, nInst.getInteractionInstanceID());
				psInstNotif.setInt(2, nInst.getCoID());
				psInstNotif.setString(3, nInst.getMsisdn());
				psInstNotif.setInt(4, nInst.getMssgTypeId());
				psInstNotif.setString(5, nInst.getMssgChnnlCd());
				psInstNotif.setInt(7, nInst.getNotificationStatusId());
				psInstNotif.execute();
			}
		}
		
	}
}
