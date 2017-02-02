package rs.telenor.intrep.db.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class ComplexParameter {
	
	int paramId;
	String paramName;	
	ArrayList<HashMap<String, SimpleParameter>> simpleParams;
	
	

	public ComplexParameter(int paramId, String paramName) {
		super();
		this.paramId = paramId;
		this.paramName = paramName;
		this.simpleParams = new ArrayList<HashMap<String, SimpleParameter>>();
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

	public ArrayList<HashMap<String, SimpleParameter>> getSimpleParams() {
		return simpleParams;
	}

	public void setSimpleParams(ArrayList<HashMap<String, SimpleParameter>> simpleParams) {
		this.simpleParams = simpleParams;
	}
	
	public void addSimpleParameter(HashMap<String, SimpleParameter> sp) {
		simpleParams.add(sp);
	}
	
}