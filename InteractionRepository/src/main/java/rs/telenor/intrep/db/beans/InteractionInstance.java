package rs.telenor.intrep.db.beans;

import java.util.*;



public class InteractionInstance {
	int interactionInstanceId;
	int componentId; //interaction type
	String interactionDT;
	LinkedHashMap<String, SimpleParameter> simpleParams;
	LinkedHashMap<String, ComplexParameter> complexParams;
	
	
	public int getInteractionInstanceId() {
		return interactionInstanceId;
	}


	public void setInteractionInstanceId(int interactionInstanceId) {
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


	public InteractionInstance(int interactionInstanceId, int componentId) {
		this.interactionInstanceId = interactionInstanceId;
		this.componentId = componentId;
		simpleParams = new LinkedHashMap<String, SimpleParameter>();
		complexParams = new LinkedHashMap<String, ComplexParameter>();			
		
	}
	
				
}
