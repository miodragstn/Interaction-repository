package rs.telenor.intrep.db.beans;

import java.util.ArrayList;

public class ConditionSet {
	int journeyInteractionId;
	ArrayList<ComplexCondition> complexConds;
	
	public ConditionSet(int id) {
		journeyInteractionId = id;
		complexConds = new ArrayList<ComplexCondition>();
		
		
	}

	public int getJourneyInteractionId() {
		return journeyInteractionId;
	}

	public void setJourneyInteractionId(int journeyInteractionId) {
		this.journeyInteractionId = journeyInteractionId;
	}

	public ArrayList<ComplexCondition> getComplexConds() {
		return complexConds;
	}

	public void setComplexConds(ArrayList<ComplexCondition> complexConds) {
		this.complexConds = complexConds;
	}
}
