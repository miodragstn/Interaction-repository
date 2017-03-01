package rs.telenor.intrep.db.beans;

import java.util.*;



public class InteractionInstance {
	Long interactionInstanceId;
	int componentId; //interaction class id
	String interactionDT;
	int interactionSourceId;
	LinkedHashMap<String, SimpleParameter> simpleParams;
	LinkedHashMap<String, ComplexParameter> complexParams;
	ArrayList<JourneyInstance> journeys;
	ArrayList<JourneyInteractionInstance> journeyInteractionInstance;
	
	
	public Long getInteractionInstanceId() {
		return interactionInstanceId;
	}


	public void setInteractionInstanceId(Long interactionInstanceId) {
		this.interactionInstanceId = interactionInstanceId;
	}


	public int getComponentId() {
		return componentId;
	}


	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}


	public String getInteractionDT() {
		return interactionDT;
	}


	public void setInteractionDT(String interactionDT) {
		this.interactionDT = interactionDT;
	}

	
	public int getInteractionSourceId() {
		return interactionSourceId;
	}


	public void setInteractionSourceId(int interactionSourceId) {
		this.interactionSourceId = interactionSourceId;
	}


	public LinkedHashMap<String, SimpleParameter> getSimpleParams() {
		return simpleParams;
	}


	public void setSimpleParams(LinkedHashMap<String, SimpleParameter> simpleParams) {
		this.simpleParams = simpleParams;
	}


	public LinkedHashMap<String, ComplexParameter> getComplexParams() {
		return complexParams;
	}


	public void setComplexParams(LinkedHashMap<String, ComplexParameter> complexParams) {
		this.complexParams = complexParams;
	}

	
	
	public ArrayList<JourneyInstance> getJourneys() {
		return journeys;
	}


	public void setJourneys(ArrayList<JourneyInstance> journeys) {
		this.journeys = journeys;
	}
	
	


	public ArrayList<JourneyInteractionInstance> getJourneyInteractionInstance() {
		return journeyInteractionInstance;
	}


	public void setJourneyInteractionInstance(ArrayList<JourneyInteractionInstance> journeyInteractionInstance) {
		this.journeyInteractionInstance = journeyInteractionInstance;
	}


	public InteractionInstance(Long interactionInstanceId, int componentId, String interactionDT, int interactionSourceId) {
		this.interactionInstanceId = interactionInstanceId;
		this.componentId = componentId;
		this.interactionDT = interactionDT;
		this.interactionSourceId = interactionSourceId;
		simpleParams = new LinkedHashMap<String, SimpleParameter>();
		complexParams = new LinkedHashMap<String, ComplexParameter>();			
		journeys = new ArrayList<JourneyInstance>();
		journeyInteractionInstance = new ArrayList<JourneyInteractionInstance>();
	}
	
				
}
