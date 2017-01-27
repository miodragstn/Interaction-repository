package rs.telenor.intrep.db.beans;

public class SimpleParameter {
	int paramId;
	String paramName;
	String valueString;
	int valueInt;
	double valueDouble;
	
	public SimpleParameter(int id, String name, String value) {
		paramId = id;
		paramName = name;
		valueString = value;
	}
	
	public SimpleParameter(int id, String name, int value) {
		paramId = id;
		paramName = name;
		valueInt = value;
	}
	
	public SimpleParameter(int id, String name, double value) {
		paramId = id;
		paramName = name;
		valueDouble = value;
	}
	
}