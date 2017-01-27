package rs.telenor.intrep.db.beans;

public class RawParameter {
	String name;
	String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public RawParameter(String name, String value) {		
		this.name = name;
		this.value = value;
	}
	
	
}
