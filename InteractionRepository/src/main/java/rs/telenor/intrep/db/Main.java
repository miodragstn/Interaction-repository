package rs.telenor.intrep.db;


import rs.telenor.intrep.db.beans.ComplexCondition;
import rs.telenor.intrep.db.beans.Interaction;
import rs.telenor.intrep.db.beans.InteractionInstance;
import rs.telenor.intrep.db.beans.JourneyInteraction;
import rs.telenor.intrep.db.beans.ParameterType;
import rs.telenor.intrep.db.tables.InteractionInstanceManager;
import rs.telenor.intrep.db.tables.InteractionManager;
import rs.telenor.intrep.db.beans.RawParameter;
import rs.telenor.intrep.db.beans.SimpleCondition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		HashMap<Integer, Interaction> interactions = InteractionManager.getInteractionHierarchy();
		/*Interaction i = interactions.get(60);
		System.out.println("Interaction: " + i.getDesc());
		for (JourneyInteraction ji : i.getJourneys()) {
			System.out.println("JourneyInteractionId: " + ji.getId() + ", JourneyId: " + ji.getJourneyId() + ", InteractionClassId: " + ji.getComponentId());
			for (ComplexCondition cc : ji.getConditionSet()) {
				System.out.println("    Complex condition Id: " + cc.getId());
				for (SimpleCondition sc : cc.getSimpleConditions()) {
					System.out.println("      Simple condition Id: " + sc.getId());
					System.out.println("         Simple condition parameter Id: " + sc.getParameterId());
					System.out.println("         Simple condition operator: " + sc.getOperator());
					ParameterType pt = sc.getValueType();
					switch (pt) {
					case ValueString : System.out.println("         Simple condition value: " + sc.getValueString());
					break;
					case ValueInt : System.out.println("         Simple condition value: " + sc.getValueInt());
					break;
					case ValueNumber : System.out.println("         Simple condition value: " + sc.getValueDouble());
					break;
					}
					
				}
			}
		}*/
		InteractionInstance inst = null;
		try {
			inst = InteractionInstanceManager.createInteractionInstance(60, "2017-01-31", 7);
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "basketId", "1234567890");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "sessionId", "9999999999");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "role", "customer");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "channel", "web");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "journeyFacingMsisdn", "38163530360");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "deliveryMethod", "sto");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "paymentMethod", "onDelivery");
			InteractionInstanceManager.addComplexParameter(inst.getInteractionInstanceId(), "basketItems");
			ArrayList<RawParameter> rawSimpleParams = new ArrayList<RawParameter>();
			RawParameter rp = null;
			rp = new RawParameter("msisdn", "38163123456");
			rawSimpleParams.add(rp);
			rp = new RawParameter("existingCustomer", "true");
			rawSimpleParams.add(rp);
			rp = new RawParameter("tmCode", "100");
			rawSimpleParams.add(rp);
			rp = new RawParameter("deviceId", "10");
			rawSimpleParams.add(rp);
			rp = new RawParameter("devicePayment", "cosmos");
			rawSimpleParams.add(rp);
			rp = new RawParameter("additionalTerms", "true");
			rawSimpleParams.add(rp);
			InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
			InteractionInstanceManager.findJourney(inst);
			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
