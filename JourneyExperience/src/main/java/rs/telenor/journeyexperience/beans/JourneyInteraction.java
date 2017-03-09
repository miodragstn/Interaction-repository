package rs.telenor.journeyexperience.beans;

public class JourneyInteraction {
	int interactionTypeId;
	String interactionDate;
	String agentUsername;
	String agentType;
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
	
	
}
