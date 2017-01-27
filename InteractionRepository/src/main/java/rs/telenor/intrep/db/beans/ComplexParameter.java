package rs.telenor.intrep.db.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class ComplexParameter {
	public ComplexParameter(int paramId, String paramName) {
		super();
		this.paramId = paramId;
		this.paramName = paramName;
		this.simpleParams = new ArrayList<HashMap<String, SimpleParameter>>();
	}

	int paramId;
	String paramName;	
	ArrayList<HashMap<String, SimpleParameter>> simpleParams;
	
	public void addSimpleParameter(HashMap<String, SimpleParameter> sp) {
		simpleParams.add(sp);
	}
	
}