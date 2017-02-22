package rs.telenor.intrep.db.beans;

import java.util.ArrayList;

public class JourneyInteraction {
	private int id;
	private int journeyId;
	private int componentId;
	private int componentOrder;
	private int previousStep;
	private int nextStep;
	private int journeyIdentifierParamId;
	private String journeyIdentifierParamName;
	private int journeyExpiryPeriod;
	private int componentNumberOfRepetitions;
	private int journeyActionId;
	private int ConditionDefId;
	
	ArrayList<ComplexCondition> conditionSet;
	
	public JourneyInteraction() {
		conditionSet = new ArrayList<ComplexCondition>();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getJourneyId() {
		return journeyId;
	}
	public void setJourneyId(int journeyId) {
		this.journeyId = journeyId;
	}
	public int getComponentId() {
		return componentId;
	}
	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}
	public int getComponentOrder() {
		return componentOrder;
	}
	public void setComponentOrder(int componentOrder) {
		this.componentOrder = componentOrder;
	}
	public int getPreviousStep() {
		return previousStep;
	}
	public void setPreviousStep(int previousStep) {
		this.previousStep = previousStep;
	}
	public int getNextStep() {
		return nextStep;
	}
	public void setNextStep(int nextStep) {
		this.nextStep = nextStep;
	}
	public int getJourneyIdentifierParamId() {
		return journeyIdentifierParamId;
	}
	public void setJourneyIdentifierParamId(int journeyIdentifierParamId) {
		this.journeyIdentifierParamId = journeyIdentifierParamId;
	}
	
	
	public String getJourneyIdentifierParamName() {
		return journeyIdentifierParamName;
	}

	public void setJourneyIdentifierParamName(String journeyIdentifierParamName) {
		this.journeyIdentifierParamName = journeyIdentifierParamName;
	}

	public int getJourneyExpiryPeriod() {
		return journeyExpiryPeriod;
	}
	public void setJourneyExpiryPeriod(int journeyExpiryPeriod) {
		this.journeyExpiryPeriod = journeyExpiryPeriod;
	}
	public int getComponentNumberOfRepetitions() {
		return componentNumberOfRepetitions;
	}
	public void setComponentNumberOfRepetitions(int componentNumberOfRepetitions) {
		this.componentNumberOfRepetitions = componentNumberOfRepetitions;
	}
	public int getJourneyActionId() {
		return journeyActionId;
	}
	public void setJourneyActionId(int journeyActionId) {
		this.journeyActionId = journeyActionId;
	}
	public ArrayList<ComplexCondition> getConditionSet() {
		return conditionSet;
	}
	public void setConditionSet(ArrayList<ComplexCondition> conditionSet) {
		this.conditionSet = conditionSet;
	}

	public int getConditionDefId() {
		return ConditionDefId;
	}

	public void setConditionDefId(int conditionDefId) {
		ConditionDefId = conditionDefId;
	}
	
	
	
}
