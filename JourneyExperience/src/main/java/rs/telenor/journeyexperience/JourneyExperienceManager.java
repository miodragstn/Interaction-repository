package rs.telenor.journeyexperience;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import rs.telenor.intrep.db.ConnectionManager;
import rs.telenor.journeyexperience.beans.JourneyExperience;
import rs.telenor.journeyexperience.beans.JourneyInteraction;


public class JourneyExperienceManager {
	
	private static Connection conn;
	
	public static String getJourneyExperienceMessageCEP(Long interactionInstance, Logger log) throws SQLException {
		log.info("JourneyExperienceMessage_MX"+interactionInstance);
		return getJourneyExperienceMessage(interactionInstance);
		
	}
	public static String getJourneyExperienceMessage(Long interactionInstance) throws SQLException {
		if (conn == null) conn = ConnectionManager.getInstance().getConnection();
		
		String sqlJourneyExperience = 
										"select JOURNEY_INSTANCE_ID," +
											   "JOURNEY_NAME, " +
										       "JOURNEY_START_DATE,"+
										       "JOURNEY_END_DATE,"+
										       "INTERACTION_ID,"   +    
										       "COMPONENT_ID,"+
										       "INTERACTION_DATE,"+
										       "max(JOURNEY_FACING_MSISDN) JOURNEY_FACING_MSISDN,"+
										       "max(MSISDN) MSISDN,"+
										       "max(CHANNEL) CHANNEL,"+
										       "max(ROLE) ROLE,"+
										       "max(DELIVERY_METHOD) DELIVERY_METHOD,"+
										       "max(AGENT_ID) AGENT_ID, "+
										       "max(DEVICE_PAYMENT) DEVICE_PAYMENT " +
										"from "+
										"( "    +   
										"select J.JOURNEY_INSTANCE_ID," +
											   "IC.INTERACTION_CLASS_DESC JOURNEY_NAME, " +
										       "J.JOURNEY_START_DATE," +
										       "J.JOURNEY_END_DATE," +
										       "JII.INTERACTION_ID," +
										       "JII.COMPONENT_ID," +
										       "JII.INTERACTION_DATE," +
										       "case "  +
										       "when IP.PARAMETER_ID = 12 then IP.PARAMETER_VALUE_STRING " +
										       "else '' " +
										       "end JOURNEY_FACING_MSISDN, " +
										       "case " +
										       "when IP.PARAMETER_ID = 15 then IP.PARAMETER_VALUE_STRING " +
										       "else '' " +
										       "end MSISDN, " +
										       "case " +
										       "when IP.PARAMETER_ID = 9 then IP.PARAMETER_VALUE_STRING " +
										       "else '' " +
										       "end CHANNEL, " +
										       "case " +
										       " when IP.PARAMETER_ID = 8 then IP.PARAMETER_VALUE_STRING "  +
										       " else '' " +
										       "end ROLE, " +
										       "case " +
										       "when IP.PARAMETER_ID = 13 then IP.PARAMETER_VALUE_STRING " +
										       "else '' " +
										       "end DELIVERY_METHOD, " +
										       "case " +
										       "when IP.PARAMETER_ID= 21 then IP.PARAMETER_VALUE_INT " +
										       "else NULL " +
										       " end AGENT_ID, " +
											   " case "+ 
											   " when IP.PARAMETER_ID= 19 then IP.PARAMETER_VALUE_STRING " +
											   " else NULL" +
											   " end DEVICE_PAYMENT, "+
										       "IP.SIMPLE_PARAM_ORD " +
										"from journeys j " +
										"join journey_interaction_instance jii " +
										"on J.JOURNEY_INSTANCE_ID = JII.JOURNEY_INSTANCE_ID " +
										"and JII.COMPONENT_ID in (64,18) " +
										"join INTERACTION_CLASS ic " +
										"on J.JOURNEY_ID = IC.INTERACTION_CLASS_ID " +
										"left join interaction_parameter ip " +
										"on JII.INTERACTION_ID = IP.INTERACTION_ID " +
										" and IP.PARAMETER_ID in (12, 15, 9, 8, 13, 21, 19) " + 
										"where J.CURRENT_INTERACTION_ID = ? " + 
										" and J.JOURNEY_ID in (20, 21) " +
										"and J.JOURNEY_STATUS_ID = 3 " +
										"and nvl(IP.SIMPLE_PARAM_ORD,1) = 1 " +
										") " +
										"group by JOURNEY_INSTANCE_ID, " +
											   "JOURNEY_NAME, " +
										       "JOURNEY_START_DATE, " +
										       " INTERACTION_ID," +
										       "JOURNEY_END_DATE, " +
										       "COMPONENT_ID, " +
										       " INTERACTION_DATE " +
										"order by JOURNEY_INSTANCE_ID, " +
										         "INTERACTION_DATE";
		ResultSet rsJourneyExperience = null;
		JourneyExperience je = null;
		String delivery = "";
		String devicePayment = "";
		String message = "";
		JourneyInteraction ji;
		try(
				PreparedStatement pstJourneyExperience = conn.prepareStatement(sqlJourneyExperience, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			pstJourneyExperience.setLong(1, interactionInstance);
			if (pstJourneyExperience.execute()) {
				rsJourneyExperience = pstJourneyExperience.getResultSet();
				if (rsJourneyExperience.isBeforeFirst()) {
					//Priprema objekta iz kojeg æe se napraviti poruka
					while (rsJourneyExperience.next()) {
						if (je == null) {
							je = new JourneyExperience();						
						}
						if (rsJourneyExperience.getInt("COMPONENT_ID") == 64) {
							je.setBrand("telenorrs");
							je.setChannel(rsJourneyExperience.getString("CHANNEL"));
							if (rsJourneyExperience.getString("JOURNEY_FACING_MSISDN") != "" && rsJourneyExperience.getString("JOURNEY_FACING_MSISDN") != null) je.setMobileNumber(rsJourneyExperience.getString("JOURNEY_FACING_MSISDN"));
							else je.setMobileNumber(rsJourneyExperience.getString("MSISDN"));
							delivery = rsJourneyExperience.getString("DELIVERY_METHOD");
							devicePayment = rsJourneyExperience.getString("DEVICE_PAYMENT");
							if (rsJourneyExperience.getString("JOURNEY_NAME").equalsIgnoreCase("JOURNEY: Renewal Voice"))
								je.setJourneyName("RS_JO_POSTPAID_RENEWAL_W_MOBILE");
							else je.setJourneyName("RS_JO_POSTPAID_AQUISITION_W_MOBILE");
							ji = new JourneyInteraction();
							ji.setInteractionTypeId(rsJourneyExperience.getInt("COMPONENT_ID"));
							ji.setAgentUsername("NULL");
							ji.setInteractionDate(rsJourneyExperience.getString("INTERACTION_DATE"));
							je.getInteractions().add(ji);
						}
						else
							if (rsJourneyExperience.getInt("COMPONENT_ID") == 18) {																					
								ji = new JourneyInteraction();
								ji.setInteractionTypeId(rsJourneyExperience.getInt("COMPONENT_ID"));
								ji.setAgentUsername("NULL");
								ji.setAgentID(rsJourneyExperience.getInt("AGENT_ID"));
								ji.setInteractionDate(rsJourneyExperience.getString("INTERACTION_DATE"));
								if (delivery.equalsIgnoreCase("sto") || delivery.equalsIgnoreCase("sto")) { 
									ji.setAgentType("Courier");
									ji.setDeliveryMethod("STO");
								}
								else { 
									ji.setAgentType("Agent");
									ji.setDeliveryMethod("PIS");
								}
								ji.setDevicePayment(rsJourneyExperience.getString("DEVICE_PAYMENT"));
								
								je.getInteractions().add(ji);
							}
					}
					//Kreiranje poruke
					message = "{ \"brand\": \"telenorrs\", \n" +
							"\"contact\": { " +
							"\"mobileNumber\": " + je.getMobileNumber() + ", \n" +
							"\"embeddedData\": {\n" +
							"\"Journey\": \"" + je.getJourneyName() + "\",\n" +
							// "\"Channel\": \"" + je.getChannel() + "\"\n" +
							"}, \n" +
							"\"interactions\": {\n" +
							"\"" + je.getInteractions().get(0).getInteractionTypeId() + "\": {\n" +
							"\"Date_Time\": \"" + je.getInteractions().get(0).getInteractionDate() + "\",\n" +
							"\"Channel\": \"" + je.getChannel() + "\"\n" +
							"\"Agent_Username\": \"\",\n" +
							"}, \n" +
							"\"" + je.getInteractions().get(1).getInteractionTypeId() + "\": {\n" +
							"\"Date_Time\": \"" + je.getInteractions().get(1).getInteractionDate() + "\",\n" +
							"\"Channel\": \"retail\"\n" +
							"\"Agent_Username\": \"\",\n" +
							"\"Agent_ID\": \"" + je.getInteractions().get(1).getAgentID() + "\"\n" +
							"\"Agent_Type\": \"" + je.getInteractions().get(1).getAgentType() + "\"\n" +
							"\"Payment_Method\": \"" + devicePayment + "\"\n" +
							"\"Service_Request_Delivery\": \"" + je.getInteractions().get(1).getDeliveryMethod() + "\"\n" +
							"}\n" +
							"}\n" +
							"}\n" +
							"}\n" ;
					return message;  
				}		  
				else return "";
			}

			else return "";
		}
		finally {
			if (rsJourneyExperience != null) rsJourneyExperience.close();
		}
		
		
	}
	
	private JourneyExperienceManager() {}
}
