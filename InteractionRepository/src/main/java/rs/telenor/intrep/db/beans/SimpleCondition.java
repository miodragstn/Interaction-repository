package rs.telenor.intrep.db.beans;

public class SimpleCondition {
	int id;	
	int parameterId;
	ParameterType valueType;
	String valueString;
	int valueInt;
	double valueDouble;
	ConditionOperator operator;
	
	public SimpleCondition(int condId, int paramId, ConditionOperator op, String value) {
		id = condId;
		parameterId = paramId;
		valueType = ParameterType.ValueString;
		operator = op;
		valueString = value;
	}
	
	public SimpleCondition(int condId, int paramId, ConditionOperator op, Integer value) {
		id = condId;
		parameterId = paramId;		
		valueType = ParameterType.ValueInt;
		operator = op;
		valueInt = value;
	}
	
	public SimpleCondition(int condId, int paramId, ConditionOperator op, Double value) {
		id = condId;
		parameterId = paramId;		
		valueType = ParameterType.ValueNumber;
		operator = op;
		valueDouble = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParameterId() {
		return parameterId;
	}

	public void setParameterId(int parameterId) {
		this.parameterId = parameterId;
	}

	public ParameterType getValueType() {
		return valueType;
	}

	public void setValueType(ParameterType valueType) {
		this.valueType = valueType;
	}

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public int getValueInt() {
		return valueInt;
	}

	public void setValueInt(int valueInt) {
		this.valueInt = valueInt;
	}

	public double getValueDouble() {
		return valueDouble;
	}

	public void setValueDouble(double valueDouble) {
		this.valueDouble = valueDouble;
	}

	public ConditionOperator getOperator() {
		return operator;
	}

	public void setOperator(ConditionOperator operator) {
		this.operator = operator;
	}
	
	
	
}
