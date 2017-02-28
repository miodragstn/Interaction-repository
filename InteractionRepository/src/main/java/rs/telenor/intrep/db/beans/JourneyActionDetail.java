package rs.telenor.intrep.db.beans;

import java.util.ArrayList;

public class JourneyActionDetail {
	int journeyActionDetailId;
	int journeyActionId;
	int conditionDefId;
	ParameterType journeyActionValueType;
	String actionParamValueString;
	int actionParamValueInt;
	double actionParamValueDouble;
	
	ArrayList<ConditionSet> conditionSets;
	
	public JourneyActionDetail(int journeyActionDetailId, int journeyActionId) {
		this.journeyActionDetailId = journeyActionDetailId;
		this.journeyActionId = journeyActionId;		
	}

	public int getJourneyActionDetailId() {
		return journeyActionDetailId;
	}

	public void setJourneyActionDetailId(int journeyActionDetailId) {
		this.journeyActionDetailId = journeyActionDetailId;
	}

	public int getJourneyActionId() {
		return journeyActionId;
	}

	public void setJourneyActionId(int journeyActionId) {
		this.journeyActionId = journeyActionId;
	}

	public int getConditionDefId() {
		return conditionDefId;
	}

	public void setConditionDefId(int conditionDefId) {
		this.conditionDefId = conditionDefId;
	}

	public ParameterType getJourneyActionValueType() {
		return journeyActionValueType;
	}

	public void setJourneyActionValueType(ParameterType journeyActionValueType) {
		this.journeyActionValueType = journeyActionValueType;
	}

	public String getActionParamValueString() {
		return actionParamValueString;
	}

	public void setActionParamValueString(String actionParamValueString) {
		this.actionParamValueString = actionParamValueString;
	}

	public int getActionParamValueInt() {
		return actionParamValueInt;
	}

	public void setActionParamValueInt(int actionParamValueInt) {
		this.actionParamValueInt = actionParamValueInt;
	}

	public double getActionParamValueDouble() {
		return actionParamValueDouble;
	}

	public void setActionParamValueDouble(double actionParamValueDouble) {
		this.actionParamValueDouble = actionParamValueDouble;
	}

	public ArrayList<ConditionSet> getConditionSets() {
		return conditionSets;
	}

	public void setConditionSets(ArrayList<ConditionSet> conditionSets) {
		this.conditionSets = conditionSets;
	}
	
	
}
