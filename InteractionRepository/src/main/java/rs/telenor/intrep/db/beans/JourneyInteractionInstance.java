package rs.telenor.intrep.db.beans;

public class JourneyInteractionInstance {
	long journeyInstanceId;
	long interactionInstanceId;
	String interactionDate;
	int componentId;
	int journeyId;
	public JourneyInteractionInstance(long journeyInstanceId, long interactionInstanceId, String interactionDate,
			int componentId, int journeyId) {		
		this.journeyInstanceId = journeyInstanceId;
		this.interactionInstanceId = interactionInstanceId;
		this.interactionDate = interactionDate;
		this.componentId = componentId;
		this.journeyId = journeyId;
	}
	public long getJourneyInstanceId() {
		return journeyInstanceId;
	}
	public void setJourneyInstanceId(long journeyInstanceId) {
		this.journeyInstanceId = journeyInstanceId;
	}
	public long getInteractionInstanceId() {
		return interactionInstanceId;
	}
	public void setInteractionInstanceId(long interactionInstanceId) {
		this.interactionInstanceId = interactionInstanceId;
	}
	public String getInteractionDate() {
		return interactionDate;
	}
	public void setInteractionDate(String interactionDate) {
		this.interactionDate = interactionDate;
	}
	public int getComponentId() {
		return componentId;
	}
	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}
	public int getJourneyId() {
		return journeyId;
	}
	public void setJourneyId(int journeyId) {
		this.journeyId = journeyId;
	}
	
	
}
