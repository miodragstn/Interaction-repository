package rs.telenor.intrep.db.beans;

public class NotificationInstance {
	long interactionInstanceID;
	int coID;
	String msisdn;
	int mssgTypeId;
	String mssgChnlCd;
	int notificationStatusId;
	
	public NotificationInstance(long interactionInstanceID, int mssgTypeId, int notificationStatusId,
								String msisdn, String mssgChnlCd){
		this.interactionInstanceID = interactionInstanceID;
		this.mssgTypeId = mssgTypeId;
		this.notificationStatusId = notificationStatusId;
		this.msisdn = msisdn;
		this.mssgChnlCd = mssgChnlCd;
	}
	
	public long getInteractionInstanceID() {
		return interactionInstanceID;
	}
	public void setInteractionInstanceID(long interactionInstanceID) {
		this.interactionInstanceID = interactionInstanceID;
	}
	public int getCoID() {
		return coID;
	}
	public void setCoID(int coID) {
		this.coID = coID;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public int getMssgTypeId() {
		return mssgTypeId;
	}
	public void setMssgTypeId(int mssgTypeId) {
		this.mssgTypeId = mssgTypeId;
	}
	public String getMssgChnnlCd() {
		return mssgChnlCd;
	}
	public void setMssgChnnlCd(String mssgChnlCd) {
		this.mssgChnlCd = mssgChnlCd;
	}
	public int getNotificationStatusId() {
		return notificationStatusId;
	}
	public void setNotificationStatusId(int notificationStatusId) {
		this.notificationStatusId = notificationStatusId;
	}
	
	
	
}
