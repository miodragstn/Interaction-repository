package rs.telenor.intrep.db.tables;

import rs.telenor.intrep.db.beans.ComplexParameter;
import rs.telenor.intrep.db.beans.ConditionOperator;
import rs.telenor.intrep.db.beans.Interaction;
import rs.telenor.intrep.db.beans.JourneyInteraction;
import rs.telenor.intrep.db.beans.Parameter;
import rs.telenor.intrep.db.beans.ParameterType;
import rs.telenor.intrep.db.beans.SimpleParameter;
import rs.telenor.intrep.db.beans.SimpleCondition;
import rs.telenor.intrep.db.beans.ComplexCondition;
import rs.telenor.intrep.db.beans.ConditionSet;
import rs.telenor.intrep.db.ConnectionManager;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.script.SimpleScriptContext;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class InteractionManager {
	public static HashMap<Integer, Interaction> interactionHierarchy;
	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	private InteractionManager() {} //to make sure one cannot create instances of this class
	
	public static HashMap<Integer, Interaction> getInteractionHierarchy() {
		if (interactionHierarchy == null) createInteractionHierarchy(); 						
		return interactionHierarchy;		
	}
	
	private static synchronized void createInteractionHierarchy() {
		interactionHierarchy = new HashMap<Integer, Interaction>() ;
		HashMap<Integer, ConditionSet> conditionSets = new HashMap<Integer, ConditionSet>();
		HashMap<Integer, ComplexCondition> complexConds = new HashMap<Integer, ComplexCondition>();
		HashMap<Integer, SimpleCondition> simpleConds = new HashMap<Integer, SimpleCondition>();
		
		try (
				Statement stmtInt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtParam = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtChildParam = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtJourneyInteraction = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtJourneyInteractionSetCond = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtJourneyInteractionComplCond = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtJourneyInteractionSimpleCond = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rsInt = stmtInt.executeQuery("SELECT INTERACTION_CLASS_ID, INTERACTION_CLASS_DESC, INTERACTION_TYPE_ID FROM INTERACTION_CLASS");
				ResultSet rsParam = stmtParam.executeQuery("SELECT  ICP.INTERACTION_CLASS_ID," +
															   "ICP.PARAMETER_ID," + 
															   "P.NAME," + 
														       "P.TYPE_ID," +
														       "icp.VALUE_DOMAIN," +
														       "icp.VALUE_DOMAIN_LOOKUP, " +
														       "icp.VALUE_DOMAIN_VALUE, " +
														       "icp.VALUE_DOMAIN_VALUE_TYPE " +
														"FROM INTERACTION_CLASS_PARAMETER icp " +
														"join PARAMETER p " +
														"on ICP.PARAMETER_ID = P.ID " +
														"WHERE icp.PARENT_PARAMETER_ID IS NULL ");
				ResultSet rsChildParam = stmtChildParam.executeQuery("SELECT  ICP.INTERACTION_CLASS_ID, " +
															   "ICP.PARAMETER_ID, " + 
															   "P.NAME, " + 
															   "icp.PARENT_PARAMETER_ID, " +
															   "parentP.NAME as PARENT_PARAMETER_NAME, " +
														       "P.TYPE_ID, " +
														       "icp.VALUE_DOMAIN, " +
														       "icp.VALUE_DOMAIN_LOOKUP, " +
														       "icp.VALUE_DOMAIN_VALUE, " +
														       "icp.VALUE_DOMAIN_VALUE_TYPE " + 
														"FROM INTERACTION_CLASS_PARAMETER icp " +
														"join PARAMETER p " +
														"on ICP.PARAMETER_ID = P.ID " +
														"join PARAMETER parentP " + 
														"on p.PARENT_PARAMETER_ID = parentP.ID " +
														"WHERE icp.PARENT_PARAMETER_ID IS NOT NULL ");
				ResultSet rsJourneyInteraction = stmtJourneyInteraction.executeQuery("SELECT JID.ID, " +
																							"JID.JOURNEY_ID, " +
																							 "JID.COMPONENT_ID, " +
																							 "JID.COMPONENT_ORDER, "+ 
																							 "JID.PREVIOUS_STEP, " +
																							 "JID.NEXT_STEP, " +
																							 "JID.JOURNEY_IDENTIFIER_PARAM_ID, " +
																							 "P.NAME AS JOURNEY_IDENTIFIER_PARAM_NAME, " +
																							 "JID.JOURNEY_EXPIRY_PERIOD, " +
																							 "JID.COMPONENT_NO_OF_REPETITIONS, " +
																							 "JID.JOURNEY_ACTION_ID " +
																					 "FROM IR.JOURNEY_INTERACTION_DEFINITION JID " +
																					 "JOIN IR.PARAMETER P " +
																					 "ON JID.JOURNEY_IDENTIFIER_PARAM_ID = P.ID");
				ResultSet rsJourneyInteractionSetCond = stmtJourneyInteractionSetCond.executeQuery("SELECT JOURNEY_INT_DEF_ID, " +
																					 			   		   "COND_SET_ID, " +
																					 			   		   "COMPL_COND_ID " +
																					 			   "FROM JOURNEY_INTERACTION_SET_COND");
				ResultSet rsJourneyInteractionComplCond = stmtJourneyInteractionComplCond.executeQuery("SELECT COMPL_COND_ID, " +
																					 			   			   "SIMPLE_COND_ID " +
																					 			   	   "FROM JOURNEY_INTERACTION_CMPL_COND");
				ResultSet rsJourneyInteractionSimpleCond = stmtJourneyInteractionSimpleCond.executeQuery("SELECT CONDITION_ID, " +
																					 			   	   			 "PARAMETER_ID, " +
																					 			   	   			 "CONDITION_OPERATOR_CD, " +
																					 			   	   			 "VALUE_TYPE_ID, " + 
																					 			   	   			 "VALUE_STRING, " +
																					 			   	   			 "VALUE_NUMBER, " +
																					 			   	   			 "VALUE_INT " + 
																					 			   	     "FROM JOURNEY_INTERACTION_SMPL_COND");
				) {
			
			if(!rsJourneyInteractionSimpleCond.isClosed()) {
				int condType;
				SimpleCondition sc = null;
				ConditionOperator co = null;
				String cop = "";
				while (rsJourneyInteractionSimpleCond.next()) {
					cop = rsJourneyInteractionSimpleCond.getString("CONDITION_OPERATOR_CD");					
					switch (cop) {
					case "EQ" : co = ConditionOperator.EQ;
					break;
					case "GT" : co = ConditionOperator.GT;
					break;
					case "GTE" : co = ConditionOperator.GTE;
					break;
					case "LT" : co = ConditionOperator.LT;
					break;
					case "LTE" : co = ConditionOperator.LTE;
					break;
					case "DIF" : co = ConditionOperator.DIF;
					break;
					case "LK" : co = ConditionOperator.LK;
					break;
					case "N" : co = ConditionOperator.N;
					break;
					case "NN" : co = ConditionOperator.NN;
					break;
					}
					condType = rsJourneyInteractionSimpleCond.getInt("VALUE_TYPE_ID");
					switch(condType) {							
					case 1: sc = new SimpleCondition(rsJourneyInteractionSimpleCond.getInt("CONDITION_ID"), rsJourneyInteractionSimpleCond.getInt("PARAMETER_ID"), co, rsJourneyInteractionSimpleCond.getString("VALUE_STRING"));
					break;
					case 2: sc = new SimpleCondition(rsJourneyInteractionSimpleCond.getInt("CONDITION_ID"), rsJourneyInteractionSimpleCond.getInt("PARAMETER_ID"), co, rsJourneyInteractionSimpleCond.getInt("VALUE_INT"));
					break;
					case 3: sc = new SimpleCondition(rsJourneyInteractionSimpleCond.getInt("CONDITION_ID"), rsJourneyInteractionSimpleCond.getInt("PARAMETER_ID"), co, rsJourneyInteractionSimpleCond.getDouble("VALUE_NUMBER"));
					break;
					}
					simpleConds.put(rsJourneyInteractionSimpleCond.getInt("CONDITION_ID"), sc);
				}

			}	
			if(!rsJourneyInteractionComplCond.isClosed()) {
				ComplexCondition cc = null;
				int simplCondId;
				SimpleCondition sc = null;
				while (rsJourneyInteractionComplCond.next()) {
					cc = new ComplexCondition(rsJourneyInteractionComplCond.getInt("COMPL_COND_ID"));
					simplCondId = rsJourneyInteractionComplCond.getInt("SIMPLE_COND_ID");
					
					if (complexConds.get(cc.getId()) == null) complexConds.put(rsJourneyInteractionComplCond.getInt("COMPL_COND_ID"), cc);
					if (simpleConds.get(simplCondId) != null)
					{
						sc = simpleConds.get(simplCondId);
						complexConds.get(cc.getId()).getSimpleConditions().add(sc);
					}
					
				}
			}
			if(!rsJourneyInteractionSetCond.isClosed()) {					
				ConditionSet cs = null;
				int journeyInteractionId;
				int complexCondId;
				while (rsJourneyInteractionSetCond.next()) {
					journeyInteractionId = rsJourneyInteractionSetCond.getInt("JOURNEY_INT_DEF_ID");
					complexCondId = rsJourneyInteractionSetCond.getInt("COMPL_COND_ID");
					cs = new ConditionSet(journeyInteractionId);
					cs.getComplexConds().add(complexConds.get(complexCondId));
					if (conditionSets.get(journeyInteractionId) == null) conditionSets.put(journeyInteractionId, cs);
					
				}
			}
			
			Interaction inter;

			if (!rsInt.isClosed()) {
				while (rsInt.next()) {
					inter = new Interaction();
					inter.setId(rsInt.getInt("INTERACTION_CLASS_ID"));
					inter.setInteractionTypeId(rsInt.getInt("INTERACTION_TYPE_ID"));
					inter.setDesc(rsInt.getString("INTERACTION_CLASS_DESC"));

					interactionHierarchy.put(inter.getId(), inter);

				} 
				
				if (!rsParam.isClosed()) {
					while (rsParam.next()) {
						inter = interactionHierarchy.get(rsParam.getInt("INTERACTION_CLASS_ID"));
						if (inter != null) {
							Parameter p = new Parameter();
							p.setParamId(rsParam.getInt("PARAMETER_ID"));
							p.setParamName(rsParam.getString("NAME"));
							int paramType = (rsParam.getInt("TYPE_ID"));
							switch (paramType) {
							case 1 : p.setParamType(ParameterType.ValueString);
							break;
							case 2 : p.setParamType(ParameterType.ValueInt);
							break;
							case 3 : p.setParamType(ParameterType.ValueNumber);
							break;
							case 4 : p.setParamType(ParameterType.ValueItems);
							break;
							case 5 : p.setParamType(ParameterType.ValueDomain);
							break;
							}
							if (rsParam.getString("VALUE_DOMAIN") != null) {
								p.setValueDomain(rsParam.getString("VALUE_DOMAIN"));
								p.setValueDomainLookup(rsParam.getString("VALUE_DOMAIN_LOOKUP"));
								p.setValueDomainValue(rsParam.getString("VALUE_DOMAIN_VALUE"));
								int valueType = rsParam.getInt("VALUE_DOMAIN_VALUE_TYPE");
								switch (valueType) {
								case 1: p.setValueDomainValueType(ParameterType.ValueString);
								break;
								case 2: p.setValueDomainValueType(ParameterType.ValueInt);
								break;
								case 3: p.setValueDomainValueType(ParameterType.ValueNumber);
								break;
								}
							}
							inter.getParameters().put(p.getParamName(), p);
						}
					}
				}
				
				if (!rsChildParam.isClosed()) {
					while (rsChildParam.next()) {
						inter = interactionHierarchy.get(rsChildParam.getInt("INTERACTION_CLASS_ID"));
						if (inter != null) {
							Parameter p = new Parameter();
							p.setParamId(rsChildParam.getInt("PARAMETER_ID"));
							p.setParamName(rsChildParam.getString("NAME"));
							p.setParentParamId(rsChildParam.getInt("PARENT_PARAMETER_ID"));
							p.setParamName(rsChildParam.getString("PARENT_PARAMETER_NAME"));
							int paramType = (rsChildParam.getInt("TYPE_ID"));
							switch (paramType) {
								case 1 : p.setParamType(ParameterType.ValueString);
										 break;
								case 2 : p.setParamType(ParameterType.ValueInt);
										 break;
								case 3 : p.setParamType(ParameterType.ValueNumber);
										 break;
								case 4 : p.setParamType(ParameterType.ValueItems);
										 break;
								case 5 : p.setParamType(ParameterType.ValueDomain);
										 break;
							}
							if (rsChildParam.getString("VALUE_DOMAIN") != null) {
								p.setValueDomain(rsChildParam.getString("VALUE_DOMAIN"));
								p.setValueDomainLookup(rsChildParam.getString("VALUE_DOMAIN_LOOKUP"));
								p.setValueDomainValue(rsChildParam.getString("VALUE_DOMAIN_VALUE"));
								int valueType = rsChildParam.getInt("VALUE_DOMAIN_VALUE_TYPE");
								switch (valueType) {
								case 1: p.setValueDomainValueType(ParameterType.ValueString);
								break;
								case 2: p.setValueDomainValueType(ParameterType.ValueInt);
								break;
								case 3: p.setValueDomainValueType(ParameterType.ValueNumber);
								break;
								}
							}
							Parameter parent = inter.getParameters().get(rsChildParam.getString("PARENTNAME")); //trazim Map sa parent parametrima 
							if (parent != null) {
								parent.addChildParam(p); //ako ga nadjem, dodajem parametar u mapu child parametara
							}
						}
					}
				}
			}
				if (!rsJourneyInteraction.isClosed()) {
					JourneyInteraction ji = null;
					ConditionSet cs = null;
					while (rsJourneyInteraction.next()) {
						inter = interactionHierarchy.get(rsJourneyInteraction.getInt("COMPONENT_ID"));
						if (inter != null) {
							ji = new JourneyInteraction();
							ji.setId(rsJourneyInteraction.getInt("ID"));
							ji.setJourneyId(rsJourneyInteraction.getInt("JOURNEY_ID"));
							ji.setComponentId(rsJourneyInteraction.getInt("COMPONENT_ID"));
							ji.setComponentOrder(rsJourneyInteraction.getInt("COMPONENT_ORDER"));
							ji.setPreviousStep(rsJourneyInteraction.getInt("PREVIOUS_STEP"));
							ji.setNextStep(rsJourneyInteraction.getInt("NEXT_STEP"));
							ji.setJourneyIdentifierParamId(rsJourneyInteraction.getInt("JOURNEY_IDENTIFIER_PARAM_ID"));
							ji.setJourneyIdentifierParamName(rsJourneyInteraction.getString("JOURNEY_IDENTIFIER_PARAM_NAME"));
							ji.setJourneyExpiryPeriod(rsJourneyInteraction.getInt("JOURNEY_EXPIRY_PERIOD"));
							ji.setComponentNumberOfRepetitions(rsJourneyInteraction.getInt("COMPONENT_NO_OF_REPETITIONS"));
							ji.setJourneyActionId(rsJourneyInteraction.getInt("JOURNEY_ACTION_ID"));
							cs = conditionSets.get(rsJourneyInteraction.getInt("ID"));
							if (cs != null) {
								for (ComplexCondition cc : cs.getComplexConds()) {
									ji.getConditionSet().add(cc);
								}
							}
							
							inter.getJourneys().add(ji);
							
							}
							
						}
				}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
