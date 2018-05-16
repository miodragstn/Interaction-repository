package rs.telenor.intrep.db.beans;

public class Notification {
	private long mInteractionId;
	private long mCoId;
	private String mMSISDN;
	private int mMessageId;
	private String mChannelCd;
	private String mNotificationDate;
	private int mNotificationStatusId;
	private String mCreationDate;
	
	public Notification(long mInteractionId, long mCoId, String mMSISDN, int mMessageId, String mChannelCd,
						String mNotificationDate, int mNotificationStatusId, String mCreationDate) {
		
		this.mInteractionId = mInteractionId;
		this.mCoId = mCoId;
		this.mMSISDN = mMSISDN;
		this.mMessageId = mMessageId;
		this.mChannelCd = mChannelCd;
		this.mNotificationDate = mNotificationDate;
		this.mNotificationStatusId = mNotificationStatusId;
		this.mCreationDate = mCreationDate;
	}
	public long getmInteractionId() {
		return mInteractionId;
	}
	public void setmInteractionId(long mInteractionId) {
		this.mInteractionId = mInteractionId;
	}
	public long getmCoId() {
		return mCoId;
	}
	public void setmCoId(long mCoId) {
		this.mCoId = mCoId;
	}
	public String getmMSISDN() {
		return mMSISDN;
	}
	public void setmMSISDN(String mMSISDN) {
		this.mMSISDN = mMSISDN;
	}
	public int getmMessageId() {
		return mMessageId;
	}
	public void setmMessageId(int mMessageId) {
		this.mMessageId = mMessageId;
	}
	public String getmChannelCd() {
		return mChannelCd;
	}
	public void setmChannelCd(String mChannelCd) {
		this.mChannelCd = mChannelCd;
	}
	public String getmNotificationDate() {
		return mNotificationDate;
	}
	public void setmNotificationDate(String mNotificationDate) {
		this.mNotificationDate = mNotificationDate;
	}
	public int getmNotificationStatusId() {
		return mNotificationStatusId;
	}
	public void setmNotificationStatusId(int mNotificationStatusId) {
		this.mNotificationStatusId = mNotificationStatusId;
	}
	public String getmCreationDate() {
		return mCreationDate;
	}
	public void setmCreationDate(String mCreationDate) {
		this.mCreationDate = mCreationDate;
	}	
	
	
	
	
}
