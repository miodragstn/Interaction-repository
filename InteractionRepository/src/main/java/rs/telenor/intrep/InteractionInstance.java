package rs.telenor.intrep;

import java.util.*;

class SimpleParameter {
	int paramId;
	String paramName;
	String valueString;
	int valueInt;
	double valueDouble;
}

class ComplexParameter {
	int paramId;
	String paramName;
	ArrayList<HashMap<String, SimpleParameter>> simpleParams;
	
}

public class InteractionInstance {
	int interactionId;
	int componentId; //interaction type
	String interactionDT;
	HashMap<String, SimpleParameter> simpleParams;
	HashMap<String, ComplexParameter> complexParams;
}
