package rs.telenor.intrep.db.tables;

import rs.telenor.intrep.db.beans.Interaction;
import rs.telenor.intrep.db.beans.InteractionInstance;
import rs.telenor.intrep.db.beans.Parameter;


public class InteractionInstanceManager {
	public InteractionInstance interactionInstance;
	
	public void createInteractionInstance(int interactionInstanceId, int componentId) {
		interactionInstance = new InteractionInstance(interactionInstanceId, componentId);
	}
	
	public void addSimpleParameter(int componentId, String name, String value) {
		Interaction i = InteractionManager.getInteractionHierarchy().get(componentId);
		if (i != null) {
			//interactionInstance.
		}
			
	}
	
}
