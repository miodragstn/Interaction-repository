

package rs.telenor.intrep.db.beans;

import java.util.HashMap;
import java.util.ArrayList;

import rs.telenor.intrep.db.beans.Parameter;
import rs.telenor.intrep.db.beans.JourneyInteraction;









public class Interaction {
	private int id;
	private int interactionTypeId;
	private String desc;
	private HashMap<String, Parameter> parameters;
	private ArrayList<JourneyInteraction> journeys;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInteractionTypeId() {
		return interactionTypeId;
	}
	public void setInteractionTypeId(int interactionTypeId) {
		this.interactionTypeId = interactionTypeId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public HashMap<String, Parameter> getParameters() {
		if (parameters == null) parameters = new HashMap<String, Parameter>();
		return parameters;
	}
	
	public void setParameters(HashMap<String, Parameter> parameters) {
		this.parameters = parameters;
	}
	public ArrayList<JourneyInteraction> getJourneys() {
		if (journeys == null) journeys = new ArrayList<JourneyInteraction>();
		return journeys;
	}
	public void setJourneys(ArrayList<JourneyInteraction> journeys) {
		this.journeys = journeys;
	}
	
	
	
	
	/*Interaction(int id, int interactionTypeId, String desc) {
		this.id = id;
		this.interactionTypeId = interactionTypeId;
		this.desc = desc;
		parameters = new HashMap<String, Parameter>();
	}*/
}
