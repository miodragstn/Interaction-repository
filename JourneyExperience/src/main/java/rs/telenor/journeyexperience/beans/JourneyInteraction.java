package rs.telenor.journeyexperience.beans;

public class JourneyInteraction {
	int interactionTypeId;
	String interactionDate;
	String agentUsername;
	String agentType;
	int agentID;
	String devicePayment;
	String deliveryMethod;
	public int getInteractionTypeId() {
		return interactionTypeId;
	}
	public void setInteractionTypeId(int interactionTypeId) {
		this.interactionTypeId = interactionTypeId;
	}
	public String getInteractionDate() {
		return interactionDate;
	}
	public void setInteractionDate(String interactionDate) {
		this.interactionDate = interactionDate;
	}
	public String getAgentUsername() {
		return agentUsername;
	}
	public void setAgentUsername(String agentUsername) {
		this.agentUsername = agentUsername;
	}
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public int getAgentID() {
		return agentID;
	}
	public void setAgentID(int agentID) {
		this.agentID = agentID;
	}
	public String getDevicePayment() {
		return devicePayment;
	}
	public void setDevicePayment(String devicePayment) {
		this.devicePayment = devicePayment;
	}
	public String getDeliveryMethod() {
		return deliveryMethod;
	}
	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
	
	
}
