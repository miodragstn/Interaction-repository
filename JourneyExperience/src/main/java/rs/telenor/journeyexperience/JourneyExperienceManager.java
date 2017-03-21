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
										       "max(AGENT_ID) AGENT_ID "+
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
										       "IP.SIMPLE_PARAM_ORD " +
										"from journeys j " +
										"join journey_interaction_instance jii " +
										"on J.JOURNEY_INSTANCE_ID = JII.JOURNEY_INSTANCE_ID " +
										"and JII.COMPONENT_ID in (67,18) " +
										"join INTERACTION_CLASS ic " +
										"on J.JOURNEY_ID = IC.INTERACTION_CLASS_ID " +
										"left join interaction_parameter ip " +
										"on JII.INTERACTION_ID = IP.INTERACTION_ID " +
										" and IP.PARAMETER_ID in (12, 15, 9, 8, 13, 21) " + 
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
						if (rsJourneyExperience.getInt("COMPONENT_ID") == 67) {
							je.setBrand("telenorSerbia");
							je.setChannel(rsJourneyExperience.getString("CHANNEL"));
							if (rsJourneyExperience.getString("JOURNEY_FACING_MSISDN") != "") je.setMobileNumber(rsJourneyExperience.getString("JOURNEY_FACING_MSISDN"));
							else je.setMobileNumber(rsJourneyExperience.getString("MSISDN"));
							delivery = rsJourneyExperience.getString("DELIVERY_METHOD");
							je.setJourneyName(rsJourneyExperience.getString("JOURNEY_NAME"));
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
								ji.setInteractionDate(rsJourneyExperience.getString("INTERACTION_DATE"));
								if (delivery.equalsIgnoreCase("sto")) ji.setAgentType("Courier");
								else ji.setAgentType("Agent");
								je.getInteractions().add(ji);
							}
					}
					//Kreiranje poruke
					message = "{ \"brand\": \"telenorSerbia\", \n" +
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
							"\"Agent_Type\": \"" + je.getInteractions().get(1).getAgentType() + "\"\n" + 
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
