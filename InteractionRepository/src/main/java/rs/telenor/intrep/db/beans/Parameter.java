

package rs.telenor.intrep.db.beans;

import java.util.HashMap;


 public class Parameter {
	int paramId;
	String paramName;
	int parentParamId;
	String parentParamName;
	ParameterType paramType;
	
	String valueDomain;
	String valueDomainField;
	HashMap<String, Parameter> childParams;
	
	public void addChildParam(Parameter p) {
		if (childParams == null) childParams = new HashMap<String, Parameter>();
		childParams.put(p.getParamName(), p);
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
	public int getParentParamId() {
		return parentParamId;
	}
	public void setParentParamId(int parentParamId) {
		this.parentParamId = parentParamId;
	}
	public String getParentParamName() {
		return parentParamName;
	}
	public void setParentParamName(String parentParamName) {
		this.parentParamName = parentParamName;
	}
	public ParameterType getParamType() {
		return paramType;
	}
	public void setParamType(ParameterType paramType) {
		this.paramType = paramType;
	}
	public String getValueDomain() {
		return valueDomain;
	}
	public void setValueDomain(String valueDomain) {
		this.valueDomain = valueDomain;
	}
	public String getValueDomainField() {
		return valueDomainField;
	}
	public void setValueDomainField(String valueDomainField) {
		this.valueDomainField = valueDomainField;
	}
	public HashMap<String, Parameter> getChildParams() {
		return childParams;
	}
	public void setChildParams(HashMap<String, Parameter> childParams) {
		this.childParams = childParams;
	}
	
	
}
