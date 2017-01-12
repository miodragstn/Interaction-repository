

package rs.telenor.intrep.db.beans;

import java.util.HashMap;

 enum ParameterType {
	ValueInt, ValueString, ValueNumber, ValueItems, ValueDomain
}

 public class Parameter {
	int paramId;
	String paramName;
	int parentParamId;
	String parentParamName;
	ParameterType paramType;
	
	String valueDomain;
	String valueDomainField;
	HashMap<String, Parameter> childParams;
}
