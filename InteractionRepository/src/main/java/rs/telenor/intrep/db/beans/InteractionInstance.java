package rs.telenor.intrep.db.beans;

import java.util.*;

class SimpleParameter {
	int paramId;
	String paramName;
	String valueString;
	int valueInt;
	double valueDouble;
	
	SimpleParameter(int id, String name, String value) {
		paramId = id;
		paramName = name;
		valueString = value;
	}
	
	SimpleParameter(int id, String name, int value) {
		paramId = id;
		paramName = name;
		valueInt = value;
	}
	
	SimpleParameter(int id, String name, double value) {
		paramId = id;
		paramName = name;
		valueDouble = value;
	}
	
}

class ComplexParameter {
	int paramId;
	String paramName;	
	ArrayList<SimpleParameter> simpleParams;
	
	public void addSimpleParameter(SimpleParameter sp) {
		simpleParams.add(sp);
	}
	
}

public class InteractionInstance {
	int interactionInstanceId;
	int componentId; //interaction type
	String interactionDT;
	ArrayList<SimpleParameter> simpleParams;
	ArrayList<ComplexParameter> complexParams;
	
	public InteractionInstance(int interactionInstanceId, int componentId) {
		this.interactionInstanceId = interactionInstanceId;
		this.componentId = componentId;
		simpleParams = new ArrayList<SimpleParameter>();
		complexParams = new ArrayList<ComplexParameter>();
	}
		public void addSimpleParameter(SimpleParameter sp) {
			simpleParams.add(sp);
	}
				
}
