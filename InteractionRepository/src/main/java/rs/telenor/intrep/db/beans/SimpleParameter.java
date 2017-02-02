package rs.telenor.intrep.db.beans;

public class SimpleParameter {
	int paramId;
	String paramName;
	String valueString;
	int valueInt;
	double valueDouble;
	ParameterType pType;
	
	public SimpleParameter(int id, String name, String value) {
		paramId = id;
		paramName = name;
		valueString = value;
		pType = ParameterType.ValueString;
	}
	
	public SimpleParameter(int id, String name, int value) {
		paramId = id;
		paramName = name;
		valueInt = value;
		pType = ParameterType.ValueInt;
	}
	
	public SimpleParameter(int id, String name, double value) {
		paramId = id;
		paramName = name;
		valueDouble = value;
		pType = ParameterType.ValueNumber;
	}

	public int getParamId() {
		return paramId;
	}

	public void setParamId(int paramId) {
		this.paramId = paramId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
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

	public ParameterType getpType() {
		return pType;
	}

	public void setpType(ParameterType pType) {
		this.pType = pType;
	}
	
	
	
	
	
}