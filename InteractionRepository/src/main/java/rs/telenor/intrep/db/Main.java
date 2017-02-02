package rs.telenor.intrep.db;

import rs.telenor.intrep.db.tables.*;
import rs.telenor.intrep.db.beans.*;

import java.sql.SQLException;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		HashMap<Integer, Interaction> interactions = InteractionManager.getInteractionHierarchy();
//		Interaction i = interactions.get(10);
//		System.out.println("Interaction: " + i.getDesc());
		InteractionInstance inst = null;
		try {
			inst = InteractionInstanceManager.createInteractionInstance(15, "2017-01-31", 3);
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "IMEI", "1234567890");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "IMSI", "9999999999");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "Phone Type", "S");
			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
