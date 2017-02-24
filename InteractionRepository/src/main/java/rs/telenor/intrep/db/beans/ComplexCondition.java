package rs.telenor.intrep.db.beans;

import java.util.ArrayList;

public class ComplexCondition {
	int id;	
		
	ArrayList<SimpleCondition> simpleConditions;
	
	
	public ComplexCondition(int id) {
		this.id = id;
		simpleConditions = new ArrayList<SimpleCondition>();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	

	


	public ArrayList<SimpleCondition> getSimpleConditions() {
		return simpleConditions;
	}


	public void setSimpleConditions(ArrayList<SimpleCondition> simpleConditions) {
		this.simpleConditions = simpleConditions;
	}
	
	
}
