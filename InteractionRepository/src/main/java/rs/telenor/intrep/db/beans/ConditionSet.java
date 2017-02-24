package rs.telenor.intrep.db.beans;

import java.util.ArrayList;

public class ConditionSet {
	int conditionDefId;
	int conditionSetId;
	ArrayList<ComplexCondition> complexConds;
	
	public ConditionSet(int id, int conditionSetId) {
		conditionDefId = id;
		this.conditionSetId = conditionSetId;
		complexConds = new ArrayList<ComplexCondition>();
		
		
	}

	public int getConditionDefId() {
		return conditionDefId;
	}

	public void setConditionDefId(int conditionDefId) {
		this.conditionDefId = conditionDefId;
	}
	
	
	public int getConditionSetId() {
		return conditionSetId;
	}

	public void setConditionSetId(int conditionSetId) {
		this.conditionSetId = conditionSetId;
	}

	public ArrayList<ComplexCondition> getComplexConds() {
		return complexConds;
	}

	public void setComplexConds(ArrayList<ComplexCondition> complexConds) {
		this.complexConds = complexConds;
	}
}
