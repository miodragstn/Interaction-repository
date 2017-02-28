package rs.telenor.intrep.db;



import rs.telenor.intrep.db.tables.InteractionInstanceManager;
import rs.telenor.intrep.db.tables.InteractionManager;
import rs.telenor.intrep.db.beans.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		HashMap<Integer, Interaction> interactions = InteractionManager.getInteractionHierarchy();
		Interaction i = interactions.get(60);
		/*System.out.println("Interaction: " + i.getDesc());
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
		ArrayList<RawParameter> rawSimpleParams = new ArrayList<RawParameter>();
		RawParameter rp = null;
		try {
//			Interakcija 60
			inst = InteractionInstanceManager.createInteractionInstance(60, "2017-02-18", 7);
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "basketId", "444444444");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "sessionId", "9999999999");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "role", "customer");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "channel", "web");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "journeyFacingMsisdn", "38163530360");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "deliveryMethod", "sto");
			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "paymentMethod", "onDelivery");
			InteractionInstanceManager.addComplexParameter(inst.getInteractionInstanceId(), "basketItems");
			rawSimpleParams = new ArrayList<RawParameter>();
			rp = null;
			rp = new RawParameter("msisdn", "38163123456");
			rawSimpleParams.add(rp);
			rp = new RawParameter("existingCustomer", "true");
			rawSimpleParams.add(rp);
			rp = new RawParameter("tmCode", "461");
			rawSimpleParams.add(rp);
			rp = new RawParameter("deviceId", "10");
			rawSimpleParams.add(rp);
			rp = new RawParameter("devicePayment", "cosmos");
			rawSimpleParams.add(rp);
			rp = new RawParameter("additionalTerms", "true");
			rawSimpleParams.add(rp);
			InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
//			rawSimpleParams = new ArrayList<RawParameter>();
//			rp = new RawParameter("msisdn", "3816312555");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("existingCustomer", "true");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("tmCode", "81");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("deviceId", "10");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("devicePayment", "cosmos");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("additionalTerms", "true");
//			rawSimpleParams.add(rp);
//			InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);			
			InteractionInstanceManager.findJourney(inst);
			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());			
			//Interakcija 61
//			inst = InteractionInstanceManager.createInteractionInstance(61, "2017-02-18", 7);
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "basketId", "222222222");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "sessionId", "9999999999");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "role", "customer");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "channel", "web");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "journeyFacingMsisdn", "38163530360");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "deliveryMethod", "sto");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "paymentMethod", "onDelivery");
//			InteractionInstanceManager.addComplexParameter(inst.getInteractionInstanceId(), "basketItems");
//			rawSimpleParams = new ArrayList<RawParameter>();			
//			rp = new RawParameter("msisdn", "38163123456");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("existingCustomer", "true");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("tmCode", "461");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("deviceId", "10");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("devicePayment", "cosmos");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("additionalTerms", "true");
//			rawSimpleParams.add(rp);
//			InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
//			InteractionInstanceManager.findJourney(inst);
//			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());
//			//Interakcija 62
//			inst = InteractionInstanceManager.createInteractionInstance(62, "2017-02-17", 7);
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "basketId", "111111111");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "sessionId", "9999999999");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "role", "customer");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "channel", "web");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "journeyFacingMsisdn", "38163530360");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "deliveryMethod", "sto");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "paymentMethod", "onDelivery");
//			InteractionInstanceManager.addComplexParameter(inst.getInteractionInstanceId(), "basketItems");
//			rawSimpleParams = new ArrayList<RawParameter>();			
//			rp = new RawParameter("msisdn", "38163123456");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("existingCustomer", "true");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("tmCode", "461");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("deviceId", "10");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("devicePayment", "cosmos");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("additionalTerms", "true");
//			rawSimpleParams.add(rp);
//			InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
//			InteractionInstanceManager.findJourney(inst);
//			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());
//			
//			//Interakcija 63
//			inst = InteractionInstanceManager.createInteractionInstance(63, "2017-02-17", 7);
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "basketId", "111111111");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "sessionId", "9999999999");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "role", "customer");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "channel", "web");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "journeyFacingMsisdn", "38163530360");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "deliveryMethod", "sto");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "paymentMethod", "onDelivery");			
//			InteractionInstanceManager.findJourney(inst);
//			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());
//			
//			//Interakcija 67
//			inst = InteractionInstanceManager.createInteractionInstance(67, "2017-02-17", 7);
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "basketId", "111111111");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "sessionId", "9999999999");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "role", "customer");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "channel", "web");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "journeyFacingMsisdn", "38163530360");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "deliveryMethod", "sto");
//			InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), "paymentMethod", "onDelivery");
//			InteractionInstanceManager.addComplexParameter(inst.getInteractionInstanceId(), "basketItems");
//			rawSimpleParams = new ArrayList<RawParameter>();			
//			rp = new RawParameter("msisdn", "38163123456");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("existingCustomer", "true");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("tmCode", "461");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("deviceId", "10");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("devicePayment", "cosmos");
//			rawSimpleParams.add(rp);
//			rp = new RawParameter("additionalTerms", "true");
//			rawSimpleParams.add(rp);
//			InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
//			InteractionInstanceManager.findJourney(inst);
//			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
