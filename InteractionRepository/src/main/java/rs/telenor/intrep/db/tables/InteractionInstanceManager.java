package rs.telenor.intrep.db.tables;

import rs.telenor.intrep.db.beans.Interaction;
import rs.telenor.intrep.db.beans.InteractionInstance;
import rs.telenor.intrep.db.beans.Parameter;
import rs.telenor.intrep.db.beans.ParameterType;
import rs.telenor.intrep.db.beans.SimpleParameter;
import rs.telenor.intrep.db.beans.ComplexParameter;
import rs.telenor.intrep.db.beans.RawParameter;
import java.util.HashMap;
import java.util.ArrayList;



public class InteractionInstanceManager {
	public InteractionInstance interactionInstance;
	
	
	public void createInteractionInstance(int interactionInstanceId, int componentId) {
		interactionInstance = new InteractionInstance(interactionInstanceId, componentId);
	}
	
	public void addSimpleParameter(int componentId, String name, String value) {
		Interaction i = InteractionManager.getInteractionHierarchy().get(componentId);
		SimpleParameter sp = null;
		if (i != null) {
			
			sp = createSimpleParameter(i, name, value);
				if (sp != null) interactionInstance.getSimpleParams().put(name, sp);
			}				
			
	}
	
	public SimpleParameter createSimpleParameter(Interaction i, String name, String value) {
		SimpleParameter sp = null;
		Parameter p = i.getParameters().get(name);
		if (p != null) {
			ParameterType pt = p.getParamType();
			switch (pt) {
			case ValueInt: 
				sp = new SimpleParameter(p.getParamId(), name, Integer.parseInt(value));					
				break;
			case ValueNumber:
				sp = new SimpleParameter(p.getParamId(), name, Double.parseDouble(value));
				break;
			case ValueString:
				sp = new SimpleParameter(p.getParamId(), name, value);
			default:
				break;
			}
			if (sp != null) return sp;
			else return null;
		}
		else return null;
	}
	
	public void addComplexParameter(int componentId, String name) {
		Interaction i = InteractionManager.getInteractionHierarchy().get(componentId);
		ComplexParameter cp = null;
		if (i != null) {
			
			Parameter p = i.getParameters().get(name);
			if (p != null) {
				cp = new ComplexParameter(p.getParamId(), p.getParamName());				
				interactionInstance.getComplexParams().put(name, cp);
			}				
		}
			
	}
	
	public void addSimpleParamToComplex(InteractionInstance i, String cpName, ArrayList<RawParameter> rawSimpleParams) {
		Interaction inter = InteractionManager.getInteractionHierarchy().get(i.getComponentId());
		if (inter != null) {
			ComplexParameter cp = i.getComplexParams().get(cpName);
			SimpleParameter sp;		
			HashMap<String, SimpleParameter> simpleParamMap = new HashMap<String, SimpleParameter>();
			if (cp != null) {
								
				for (RawParameter rp : rawSimpleParams) {
					sp = createSimpleParameter(inter, rp.getName(), rp.getValue());
					if (sp != null) simpleParamMap.put(rp.getName(), sp);
				}
				if (!simpleParamMap.isEmpty()) cp.addSimpleParameter(simpleParamMap);
			}
		}
	}
	
}
