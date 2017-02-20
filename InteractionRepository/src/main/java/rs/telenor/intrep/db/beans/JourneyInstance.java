package rs.telenor.intrep.db.beans;

public class JourneyInstance {
	long journeyInstanceId;
	int journeyId;
	String journeyStartDt;
	String journeyEndDt;
	int journeyCurrentStep;
	int journeyIdentifierParamId;
	String journeyIdentifierValue;
	long currentInteractionInstanceId;
	int currentInteractionOrder;
	int updateType;
	public long getJourneyInstanceId() {
		return journeyInstanceId;
	}
	public void setJourneyInstanceId(long journeyInstanceId) {
		this.journeyInstanceId = journeyInstanceId;
	}
	public int getJourneyId() {
		return journeyId;
	}
	public void setJourneyId(int journeyId) {
		this.journeyId = journeyId;
	}
	public String getJourneyStartDt() {
		return journeyStartDt;
	}
	public void setJourneyStartDt(String journeyStartDt) {
		this.journeyStartDt = journeyStartDt;
	}
	public String getJourneyEndDt() {
		return journeyEndDt;
	}
	public void setJourneyEndDt(String journeyEndDt) {
		this.journeyEndDt = journeyEndDt;
	}
	public int getJourneyCurrentStep() {
		return journeyCurrentStep;
	}
	public void setJourneyCurrentStep(int journeyCurrentStep) {
		this.journeyCurrentStep = journeyCurrentStep;
	}
	public int getJourneyIdentifierParamId() {
		return journeyIdentifierParamId;
	}
	public void setJourneyIdentifierParamId(int journeyIdentifierParamId) {
		this.journeyIdentifierParamId = journeyIdentifierParamId;
	}
	public String getJourneyIdentifierValue() {
		return journeyIdentifierValue;
	}
	public void setJourneyIdentifierValue(String journeyIdentifierValue) {
		this.journeyIdentifierValue = journeyIdentifierValue;
	}
	public long getCurrentInteractionInstanceId() {
		return currentInteractionInstanceId;
	}
	public void setCurrentInteractionInstanceId(long currentInteractionInstanceId) {
		this.currentInteractionInstanceId = currentInteractionInstanceId;
	}
	public int getCurrentInteractionOrder() {
		return currentInteractionOrder;
	}
	public void setCurrentInteractionOrder(int currentInteractionOrder) {
		this.currentInteractionOrder = currentInteractionOrder;
	}
	public int getUpdateType() {
		return updateType;
	}
	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}
	
	
	
}
