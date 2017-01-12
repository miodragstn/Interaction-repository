

package rs.telenor.intrep.db.beans;

import java.util.HashMap;

import rs.telenor.intrep.db.beans.Parameter;

import java.util.ArrayList;

enum ConditionOperator {
	EQ, GT, GTE, LT, LTE, DIF, LK, N, NN
}

class SimpleCondition {
	int id;
	String Name;
	int parameterId;
	String valueString;
	int valueInt;
	double valueDouble;
	ConditionOperator operator;
	
	SimpleCondition(Parameter pt, ConditionOperator op, Object condValue) {
		
	}
	
}


class ComplexCondition {
	int id;
	String Name;
		
	ArrayList<SimpleCondition> simpleConditions;
	
}

public class Interaction {
	private int id;
	private int interactionTypeId;
	private String desc;
	HashMap<String, Parameter> parameters;
	
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
		return parameters;
	}
	
	public void setParameters(HashMap<String, Parameter> parameters) {
		this.parameters = parameters;
	}
	
	
	
	
	/*Interaction(int id, int interactionTypeId, String desc) {
		this.id = id;
		this.interactionTypeId = interactionTypeId;
		this.desc = desc;
		parameters = new HashMap<String, Parameter>();
	}*/
}
