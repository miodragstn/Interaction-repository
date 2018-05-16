package rs.telenor.intrep.db.beans;

import java.util.ArrayList;

public class SubscriptionProfile {
	private long mCustomerId;
	private String mCustCode;
	private long mCoId;
	private String mMSISDN;
	private String mMacroSegment;
	private String mSubscriptionTypeCd;
	private String mRegistrationDate;
	private String mOnboardDate;
	private String mLastAction1Date;
	private String mLastAction2Date;
	private String mLastAction3Date;
	private String mLastAppDate;
	private String mActivationDate;
	private String mHandsetDate;
	private String mTutorialDate;
	private String mRenewalDate;
	private String mNotificationDate;
	private String mPhoneTypeCd;
	private String mAPI;
	private String mAPK;
	
	 
	
	public SubscriptionProfile() {				
	}
	
	public SubscriptionProfile(String mMSISDN) {		
		this.mMSISDN = mMSISDN;
	}
	
	
	
	public SubscriptionProfile(long mCustomerId, String mCustCode, long mCoId, String mMSISDN, String mMacroSegment,
			String mSubscriptionTypeCd, String mRegistrationDate, String mOnboardDate, String mLastAction1Date,
			String mLastAction2Date, String mLastAction3Date, String mLastAppDate, String mActivationDate,
			String mHandsetDate, String mTutorialDate, String mRenewalDate, String mNotificationDate,
			String mPhoneTypeCd, String mAPI, String mAPK) {
		
		this.mCustomerId = mCustomerId;
		this.mCustCode = mCustCode;
		this.mCoId = mCoId;
		this.mMSISDN = mMSISDN;
		this.mMacroSegment = mMacroSegment;
		this.mSubscriptionTypeCd = mSubscriptionTypeCd;
		this.mRegistrationDate = mRegistrationDate;
		this.mOnboardDate = mOnboardDate;
		this.mLastAction1Date = mLastAction1Date;
		this.mLastAction2Date = mLastAction2Date;
		this.mLastAction3Date = mLastAction3Date;
		this.mLastAppDate = mLastAppDate;
		this.mActivationDate = mActivationDate;
		this.mHandsetDate = mHandsetDate;
		this.mTutorialDate = mTutorialDate;
		this.mRenewalDate = mRenewalDate;
		this.mNotificationDate = mNotificationDate;
		this.mPhoneTypeCd = mPhoneTypeCd;
		this.mAPI = mAPI;
		this.mAPK = mAPK;
	}
	
	public SubscriptionProfile(long mCustomerId, String mCustCode, long mCoId, String mMSISDN, String mMacroSegment,
			String mSubscriptionTypeCd, String mRegistrationDate, String mLastAppDate, String mActivationDate,
			String mHandsetDate, String mRenewalDate, String mNotificationDate,
			String mPhoneTypeCd, String mAPI, String mAPK) {
		
		this.mCustomerId = mCustomerId;
		this.mCustCode = mCustCode;
		this.mCoId = mCoId;
		this.mMSISDN = mMSISDN;
		this.mMacroSegment = mMacroSegment;
		this.mSubscriptionTypeCd = mSubscriptionTypeCd;
		this.mRegistrationDate = mRegistrationDate;				
		this.mLastAppDate = mLastAppDate;
		this.mActivationDate = mActivationDate;
		this.mHandsetDate = mHandsetDate;		
		this.mRenewalDate = mRenewalDate;
		this.mNotificationDate = mNotificationDate;
		this.mPhoneTypeCd = mPhoneTypeCd;
		this.mAPI = mAPI;
		this.mAPK = mAPK;
	}

	public long getmCustomerId() {
		return mCustomerId;
	}
	public void setmCustomerId(long mCustomerId) {
		this.mCustomerId = mCustomerId;
	}
	public String getmCustCode() {
		return mCustCode;
	}
	public void setmCustCode(String mCustCode) {
		this.mCustCode = mCustCode;
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
	public String getmMacroSegment() {
		return mMacroSegment;
	}
	public void setmMacroSegment(String mMacroSegment) {
		this.mMacroSegment = mMacroSegment;
	}
	public String getmSubscriptionTypeCd() {
		return mSubscriptionTypeCd;
	}
	public void setmSubscriptionTypeCd(String mSubscriptionTypeCd) {
		this.mSubscriptionTypeCd = mSubscriptionTypeCd;
	}
	public String getmRegistrationDate() {
		return mRegistrationDate;
	}
	public void setmRegistrationDate(String mRegistrationDate) {
		this.mRegistrationDate = mRegistrationDate;
	}
	public String getmOnboardDate() {
		return mOnboardDate;
	}
	public void setmOnboardDate(String mOnboardDate) {
		this.mOnboardDate = mOnboardDate;
	}
	public String getmLastAction1Date() {
		return mLastAction1Date;
	}
	public void setmLastAction1Date(String mLastAction1Date) {
		this.mLastAction1Date = mLastAction1Date;
	}
	public String getmLastAction2Date() {
		return mLastAction2Date;
	}
	public void setmLastAction2Date(String mLastAction2Date) {
		this.mLastAction2Date = mLastAction2Date;
	}
	public String getmLastAction3Date() {
		return mLastAction3Date;
	}
	public void setmLastAction3Date(String mLastAction3Date) {
		this.mLastAction3Date = mLastAction3Date;
	}
	public String getmLastAppDate() {
		return mLastAppDate;
	}
	public void setmLastAppDate(String mLastAppDate) {
		this.mLastAppDate = mLastAppDate;
	}
	public String getmActivationDate() {
		return mActivationDate;
	}
	public void setmActivationDate(String mActivationDate) {
		this.mActivationDate = mActivationDate;
	}
	public String getmHandsetDate() {
		return mHandsetDate;
	}
	public void setmHandsetDate(String mHandsetDate) {
		this.mHandsetDate = mHandsetDate;
	}
	public String getmTutorialDate() {
		return mTutorialDate;
	}
	public void setmTutorialDate(String mTutorialDate) {
		this.mTutorialDate = mTutorialDate;
	}
	public String getmRenewalDate() {
		return mRenewalDate;
	}
	public void setmRenewalDate(String mRenewalDate) {
		this.mRenewalDate = mRenewalDate;
	}
	public String getmNotificationDate() {
		return mNotificationDate;
	}
	public void setmNotificationDate(String mNotificationDate) {
		this.mNotificationDate = mNotificationDate;
	}
	public String getmPhoneTypeCd() {
		return mPhoneTypeCd;
	}
	public void setmPhoneTypeCd(String mPhoneTypeCd) {
		this.mPhoneTypeCd = mPhoneTypeCd;
	}
	public String getmAPI() {
		return mAPI;
	}
	public void setmAPI(String mAPI) {
		this.mAPI = mAPI;
	}
	public String getmAPK() {
		return mAPK;
	}
	public void setmAPK(String mAPK) {
		this.mAPK = mAPK;
	}

	
	
	
	
	
}
