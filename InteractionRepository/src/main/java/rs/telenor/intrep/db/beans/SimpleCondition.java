package rs.telenor.intrep.db.beans;

public class SimpleCondition {
	int id;	
	int parameterId;
	String parameterName;
	ParameterType valueType;
	String valueString;
	int valueInt;
	double valueDouble;
	String valueDomain;		//lookup tabela
	String valueDomainLookup;	//polje iz lookup tabele po kojem se pretrazuje - lookup polje
	String valueDomainValue;	//polje iz lookup tabele koje se poredi - lookup value
	ParameterType valueDomainValueType; //tip lookup value-a
	ConditionOperator operator;
	String scope; // ALL, ANY -> ima smisla kod parametar koji imaju parent parametre, tj. ponavljaju se u interakciji - ovo odredjuje da li uslov vazi za sve parametre
	
	public SimpleCondition(int condId, int paramId, String paramName, ConditionOperator op, String value) {
		id = condId;
		parameterId = paramId;
		parameterName = paramName;
		valueType = ParameterType.ValueString;
		operator = op;
		valueString = value;
	}
	
	public SimpleCondition(int condId, int paramId, String paramName, ConditionOperator op, Integer value) {
		id = condId;
		parameterId = paramId;	
		parameterName = paramName;
		valueType = ParameterType.ValueInt;
		operator = op;
		valueInt = value;
		
	}
	
	public SimpleCondition(int condId, int paramId, String paramName, ConditionOperator op, Double value) {
		id = condId;
		parameterId = paramId;		
		parameterName = paramName;
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

	
	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
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

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getValueDomain() {
		return valueDomain;
	}

	public void setValueDomain(String valueDomain) {
		this.valueDomain = valueDomain;
	}

	public String getValueDomainLookup() {
		return valueDomainLookup;
	}

	public void setValueDomainLookup(String valueDomainLookup) {
		this.valueDomainLookup = valueDomainLookup;
	}

	public String getValueDomainValue() {
		return valueDomainValue;
	}

	public void setValueDomainValue(String valueDomainValue) {
		this.valueDomainValue = valueDomainValue;
	}

	public ParameterType getValueDomainValueType() {
		return valueDomainValueType;
	}

	public void setValueDomainValueType(ParameterType valueDomainValueType) {
		this.valueDomainValueType = valueDomainValueType;
	}
	
	
	
}
