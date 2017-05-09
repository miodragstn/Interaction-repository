package rs.telenor.intrep.db.tables;

import rs.telenor.intrep.db.beans.ComplexParameter;
import rs.telenor.intrep.db.beans.ConditionOperator;
import rs.telenor.intrep.db.beans.Interaction;
import rs.telenor.intrep.db.beans.JourneyActionDetail;
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

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class InteractionManager {
	public static HashMap<Integer, Interaction> interactionHierarchy;
//	private static Connection conn = ConnectionManager.getInstance().getConnection();
	private static Connection conn ;
	
	private InteractionManager() {} //to make sure one cannot create instances of this class
	
	public static HashMap<Integer, Interaction> getInteractionHierarchy() {
		if (conn==null){
			conn = ConnectionManager.getInstance().getConnection();
		}
		if (interactionHierarchy == null) createInteractionHierarchy(); 						
		return interactionHierarchy;		
	}
	
	public static HashMap<Integer, Interaction> getInteractionHierarchy(Logger log) {
		if (conn==null){
			conn = ConnectionManager.getInstance().getCEPConnection(log);
		}
		if (interactionHierarchy == null) createInteractionHierarchy(); 						
		return interactionHierarchy;		
	}
	
	private static synchronized void createInteractionHierarchy() {
		interactionHierarchy = new HashMap<Integer, Interaction>() ;
		HashMap<Integer, ArrayList<ConditionSet>> conditionSets = new HashMap<Integer, ArrayList<ConditionSet>>();		
		HashMap<Integer, ComplexCondition> complexConds = new HashMap<Integer, ComplexCondition>();
		HashMap<Integer, SimpleCondition> simpleConds = new HashMap<Integer, SimpleCondition>();
		HashMap<Integer, ArrayList<JourneyActionDetail>> journeyActions = new HashMap<Integer, ArrayList<JourneyActionDetail>>();
		
		try (
				Statement stmtInt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtParam = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtChildParam = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtJourneyInteraction = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtJourneyInteractionSetCond = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtJourneyInteractionComplCond = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtJourneyInteractionSimpleCond = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtJourneyActionDetails = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rsInt = stmtInt.executeQuery("SELECT INTERACTION_CLASS_ID, INTERACTION_CLASS_DESC, INTERACTION_TYPE_ID FROM INTERACTION_CLASS ");
				ResultSet rsParam = stmtParam.executeQuery("SELECT  ICP.INTERACTION_CLASS_ID, " +
																   "ICP.PARAMETER_ID, " + 
																   "P.NAME, " + 
															       "P.TYPE_ID, " +
															       "icp.VALUE_DOMAIN, " +
															       "icp.VALUE_DOMAIN_LOOKUP, " +
															       "icp.VALUE_DOMAIN_VALUE, " +
															       "icp.VALUE_DOMAIN_VALUE_TYPE " +
														"FROM INTERACTION_CLASS_PARAMETER icp " +
														"join PARAMETER p " +
														"on ICP.PARAMETER_ID = P.ID " +
														"WHERE icp.PARENT_PARAMETER_ID IS NULL " +
														"ORDER BY ICP.INTERACTION_CLASS_ID, ICP.PARAMETER_ID");
				ResultSet rsChildParam = stmtChildParam.executeQuery("SELECT  	ICP.INTERACTION_CLASS_ID, " +
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
														"on icp.PARENT_PARAMETER_ID = parentP.ID " +
														"WHERE icp.PARENT_PARAMETER_ID IS NOT NULL " +
														"ORDER BY ICP.INTERACTION_CLASS_ID, ICP.PARAMETER_ID");
				ResultSet rsJourneyInteraction = stmtJourneyInteraction.executeQuery("SELECT JID.ID, " +
																							"JID.JOURNEY_ID, " +
																							 "JID.COMPONENT_ID, " +
																							 "JID.COMPONENT_ORDER, "+ 
																							 "NVL(JID.PREVIOUS_STEP,-1) AS PREVIOUS_STEP,  " +
																							 "NVL(JID.NEXT_STEP, -1) AS NEXT_STEP, " +
																							 "JID.JOURNEY_IDENTIFIER_PARAM_ID, " +
																							 "P.NAME AS JOURNEY_IDENTIFIER_PARAM_NAME, " +
																							 "JID.JOURNEY_EXPIRY_PERIOD, " +
																							 "JID.COMPONENT_NO_OF_REPETITIONS, " +
																							 "JID.JOURNEY_ACTION_ID ," +
																							 "JID.CONDITION_DEF_ID " +
																					 "FROM IR.JOURNEY_INTERACTION_DEFINITION JID " +
																					 "JOIN IR.PARAMETER P " +
																					 "ON JID.JOURNEY_IDENTIFIER_PARAM_ID = P.ID");
				ResultSet rsJourneyInteractionSetCond = stmtJourneyInteractionSetCond.executeQuery("SELECT " +
																										   "JISC.CONDITION_DEF_ID, " +
																					 			   		   "JISC.COND_SET_ID, " +
																					 			   		   "JISC.COMPL_COND_ID " +
																					 			   "FROM JOURNEY_INTERACTION_SET_COND JISC " +
																					 			   "ORDER BY JISC.CONDITION_DEF_ID, JISC.COND_SET_ID, JISC.COMPL_COND_ID");
				ResultSet rsJourneyInteractionComplCond = stmtJourneyInteractionComplCond.executeQuery("SELECT COMPL_COND_ID, " +
																					 			   			   "SIMPLE_COND_ID, " +
																					 			   			   "SCOPE " +
																					 			   	   "FROM JOURNEY_INTERACTION_CMPL_COND");
				ResultSet rsJourneyInteractionSimpleCond = stmtJourneyInteractionSimpleCond.executeQuery("SELECT sc.CONDITION_ID, " +
																					 			   	   			 "sc.PARAMETER_ID, " +
																					 			   	   			 "p.NAME as PARAMETER_NAME, " +
																					 			   	   			 "sc.CONDITION_OPERATOR_CD, " +
																					 			   	   			 "sc.VALUE_TYPE_ID, " + 
																					 			   	   			 "sc.VALUE_STRING, " +
																					 			   	   			 "sc.VALUE_NUMBER, " +
																					 			   	   			 "sc.VALUE_INT, " +
																					 			   	   			 "sc.VALUE_DOMAIN, " +
																					 			   	   			 "sc.VALUE_DOMAIN_LOOKUP, " +
																					 			   	   			 "sc.VALUE_DOMAIN_VALUE, " +
																					 			   	   			 "sc.VALUE_DOMAIN_VALUE_TYPE " +
																					 			   	     "FROM JOURNEY_INTERACTION_SMPL_COND sc " +
																					 			   	   	 "JOIN PARAMETER p " +
																					 			   	     "ON sc.PARAMETER_ID = p.ID");
				
				ResultSet rsJourneyActionDetails = stmtJourneyActionDetails.executeQuery("SELECT " +
																						"JOURNEY_ACTION_DETAILS_ID, " +
																						"JOURNEY_ACTION_ID, " +
																						"CONDITION_DEF_ID, " +
																						"ACTION_PARAM_ID, " +
																						"ACTION_PARAM_VALUE_STRING, " +
																						"ACTION_PARAM_VALUE_INT, " + 
																						"ACTION_PARAM_VALUE_NUMBER, " +
																						"ACTION_PARAM_VALUE_TYPE " +
																						"FROM JOURNEY_ACTION_DETAILS " +
																						"ORDER BY JOURNEY_ACTION_ID, JOURNEY_ACTION_DETAILS_ID, CONDITION_DEF_ID");
				
				) {
			
			if(rsJourneyInteractionSimpleCond.isBeforeFirst()) {
				int condType;
				SimpleCondition sc = null;
				ConditionOperator co = null;
				String cop = "";
				int valueDomainValueType;
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
					case 1: sc = new SimpleCondition(rsJourneyInteractionSimpleCond.getInt("CONDITION_ID"), rsJourneyInteractionSimpleCond.getInt("PARAMETER_ID"), rsJourneyInteractionSimpleCond.getString("PARAMETER_NAME"), co, rsJourneyInteractionSimpleCond.getString("VALUE_STRING"));
					break;
					case 2: sc = new SimpleCondition(rsJourneyInteractionSimpleCond.getInt("CONDITION_ID"), rsJourneyInteractionSimpleCond.getInt("PARAMETER_ID"), rsJourneyInteractionSimpleCond.getString("PARAMETER_NAME"), co, rsJourneyInteractionSimpleCond.getInt("VALUE_INT"));
					break;
					case 3: sc = new SimpleCondition(rsJourneyInteractionSimpleCond.getInt("CONDITION_ID"), rsJourneyInteractionSimpleCond.getInt("PARAMETER_ID"), rsJourneyInteractionSimpleCond.getString("PARAMETER_NAME"), co, rsJourneyInteractionSimpleCond.getDouble("VALUE_NUMBER"));
					break;
					}
					if (rsJourneyInteractionSimpleCond.getString("VALUE_DOMAIN") != "") {
						sc.setValueDomain(rsJourneyInteractionSimpleCond.getString("VALUE_DOMAIN"));
						sc.setValueDomainLookup(rsJourneyInteractionSimpleCond.getString("VALUE_DOMAIN_LOOKUP"));
						sc.setValueDomainValue(rsJourneyInteractionSimpleCond.getString("VALUE_DOMAIN_VALUE"));
						valueDomainValueType = rsJourneyInteractionSimpleCond.getInt("VALUE_DOMAIN_VALUE_TYPE");
						switch(valueDomainValueType) {							
						case 1: sc.setValueDomainValueType(ParameterType.ValueString);
						break;
						case 2: sc.setValueDomainValueType(ParameterType.ValueInt);
						break;
						case 3: sc.setValueDomainValueType(ParameterType.ValueNumber);
						break;
						}
					}
					simpleConds.put(rsJourneyInteractionSimpleCond.getInt("CONDITION_ID"), sc);
				}

			}	
			if(rsJourneyInteractionComplCond.isBeforeFirst()) {
				ComplexCondition cc = null;
				int simplCondId;
				SimpleCondition sc = null;
				String scope;
				while (rsJourneyInteractionComplCond.next()) {
					cc = new ComplexCondition(rsJourneyInteractionComplCond.getInt("COMPL_COND_ID"));
					simplCondId = rsJourneyInteractionComplCond.getInt("SIMPLE_COND_ID");
					scope = rsJourneyInteractionComplCond.getString("SCOPE");
					if (complexConds.get(cc.getId()) == null) complexConds.put(rsJourneyInteractionComplCond.getInt("COMPL_COND_ID"), cc);
					if (simpleConds.get(simplCondId) != null)
					{
						sc = simpleConds.get(simplCondId);						
						ParameterType scValueType = simpleConds.get(simplCondId).getValueType();
						switch (scValueType) {
						case ValueString: sc = new SimpleCondition(simpleConds.get(simplCondId).getId(), simpleConds.get(simplCondId).getParameterId(), simpleConds.get(simplCondId).getParameterName(), simpleConds.get(simplCondId).getOperator(), simpleConds.get(simplCondId).getValueString());
						break;
						case ValueInt: sc = new SimpleCondition(simpleConds.get(simplCondId).getId(), simpleConds.get(simplCondId).getParameterId(), simpleConds.get(simplCondId).getParameterName(), simpleConds.get(simplCondId).getOperator(), simpleConds.get(simplCondId).getValueInt());
						break;
						case ValueNumber: sc = new SimpleCondition(simpleConds.get(simplCondId).getId(), simpleConds.get(simplCondId).getParameterId(), simpleConds.get(simplCondId).getParameterName(), simpleConds.get(simplCondId).getOperator(), simpleConds.get(simplCondId).getValueDouble());
						break;
						}
						sc.setScope(scope);
						if (simpleConds.get(simplCondId).getValueDomain() != null && simpleConds.get(simplCondId).getValueDomain() != "") {
							sc.setValueDomain(simpleConds.get(simplCondId).getValueDomain());
							sc.setValueDomainLookup(simpleConds.get(simplCondId).getValueDomainLookup());
							sc.setValueDomainValue(simpleConds.get(simplCondId).getValueDomainValue());
							sc.setValueDomainValueType(simpleConds.get(simplCondId).getValueDomainValueType());
						}
						complexConds.get(cc.getId()).getSimpleConditions().add(sc);
					}
					
				}
			}
			
			
			if(rsJourneyInteractionSetCond.isBeforeFirst()) {					
				ConditionSet cs = null;
				int conditionDefId = -1;
				int conditionSetId = -1;
				int complexCondId;
				while (rsJourneyInteractionSetCond.next()) {
//					conditionDefId = rsJourneyInteractionSetCond.getInt("CONDITION_DEF_ID");
//					conditionSetId = rsJourneyInteractionSetCond.getInt("COND_SET_ID");
					complexCondId = rsJourneyInteractionSetCond.getInt("COMPL_COND_ID");
					if (conditionDefId != rsJourneyInteractionSetCond.getInt("CONDITION_DEF_ID")) { //Ako je novi JourneyInteractionDef
						conditionSetId = -1;
						conditionDefId = rsJourneyInteractionSetCond.getInt("CONDITION_DEF_ID");
						conditionSets.put(conditionDefId, new ArrayList<ConditionSet>());
					}
					
					if (conditionSetId != rsJourneyInteractionSetCond.getInt("COND_SET_ID")) { //Ako je novi ConditionSet, dodaj ga
						conditionSetId = rsJourneyInteractionSetCond.getInt("COND_SET_ID");
						cs = new ConditionSet(conditionDefId, conditionSetId);
						cs.getComplexConds().add(complexConds.get(complexCondId));						
						conditionSets.get(conditionDefId).add(cs);
					}		
					else {
						cs.getComplexConds().add(complexConds.get(complexCondId));
					}
										
																													
				}
			}
			
			//Punjenje Action strukture
			if(rsJourneyActionDetails.isBeforeFirst()) {					
				JourneyActionDetail jad = null;
				int journeyActionId = -1;
				int journeyActionDetailId = -1;
				int conditionDefId;
				int journeyActionParamValueType;
				while (rsJourneyActionDetails.next()) {
					conditionDefId = rsJourneyActionDetails.getInt("CONDITION_DEF_ID");
					journeyActionId = rsJourneyActionDetails.getInt("JOURNEY_ACTION_ID");					
					//					conditionSetId = rsJourneyInteractionSetCond.getInt("COND_SET_ID");
					//					complexCondId = rsJourneyInteractionSetCond.getInt("COMPL_COND_ID");
					if (!journeyActions.containsKey(journeyActionId)) { //Ako je novi Action, dodaj ga						
						journeyActions.put(journeyActionId, new ArrayList<JourneyActionDetail>());
					}

					journeyActionDetailId = rsJourneyActionDetails.getInt("JOURNEY_ACTION_DETAILS_ID");						
					jad = new JourneyActionDetail(journeyActionDetailId, journeyActionId);
					jad.setActionParamId(rsJourneyActionDetails.getInt("ACTION_PARAM_ID"));
					journeyActionParamValueType = rsJourneyActionDetails.getInt("ACTION_PARAM_VALUE_TYPE");
					switch(journeyActionParamValueType) {
					case 1: jad.setJourneyActionValueType(ParameterType.ValueString);
					jad.setActionParamValueString(rsJourneyActionDetails.getString("ACTION_PARAM_VALUE_STRING"));								
					break;
					case 2: jad.setJourneyActionValueType(ParameterType.ValueInt);
					jad.setActionParamValueInt(rsJourneyActionDetails.getInt("ACTION_PARAM_VALUE_INT"));								
					break;
					case 3: jad.setJourneyActionValueType(ParameterType.ValueNumber);
					jad.setActionParamValueDouble(rsJourneyActionDetails.getDouble("ACTION_PARAM_VALUE_NUMBER"));								
					break;
					}
					conditionDefId = rsJourneyActionDetails.getInt("CONDITION_DEF_ID");
					if (!rsJourneyActionDetails.wasNull()) {	//Ako je za akciju potrebno i da skup nekih condition setova bude ispunjen...
						jad.setConditionDefId(conditionDefId);
						jad.setConditionSets(conditionSets.get(conditionDefId)); //...nadji taj skup i dodaj ga u Action Detail 
					}
					journeyActions.get(journeyActionId).add(jad);

				}																																																	
			}
			
			Interaction inter;

			if (rsInt.isBeforeFirst()) {
				while (rsInt.next()) {
					inter = new Interaction();
					inter.setId(rsInt.getInt("INTERACTION_CLASS_ID"));
					inter.setInteractionTypeId(rsInt.getInt("INTERACTION_TYPE_ID"));
					inter.setDesc(rsInt.getString("INTERACTION_CLASS_DESC"));

					interactionHierarchy.put(inter.getId(), inter);

				} 
				
				if (rsParam.isBeforeFirst()) {
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
							if (rsParam.getString("VALUE_DOMAIN") != "" && rsParam.getString("VALUE_DOMAIN") != null) {
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
				
				if (rsChildParam.isBeforeFirst()) {
					while (rsChildParam.next()) {
						inter = interactionHierarchy.get(rsChildParam.getInt("INTERACTION_CLASS_ID"));
						if (inter != null) {
							Parameter p = new Parameter();
							p.setParamId(rsChildParam.getInt("PARAMETER_ID"));
							p.setParamName(rsChildParam.getString("NAME"));
							p.setParentParamId(rsChildParam.getInt("PARENT_PARAMETER_ID"));
							p.setParentParamName(rsChildParam.getString("PARENT_PARAMETER_NAME"));
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
//							Parameter parent = inter.getParameters().get(rsChildParam.getString("PARENT_PARAMETER_NAME")); //trazim Map sa parent parametrima 
//							if (parent != null) {
//								parent.addChildParam(p); //ako ga nadjem, dodajem parametar u mapu child parametara
//							}
							inter.getParameters().put(p.getParamName(), p);
						}
					}
				}
			}
			if (rsJourneyInteraction.isBeforeFirst()) {
				JourneyInteraction ji = null;
				JourneyActionDetail jad;
				int conditionDefId;
				int journeyActionId;
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
						ji.setConditionDefId(rsJourneyInteraction.getInt("CONDITION_DEF_ID"));
						conditionDefId = rsJourneyInteraction.getInt("CONDITION_DEF_ID");
						//Ubacivanje condition setova u JourneyInteraction
						if (!rsJourneyInteraction.wasNull()) {
							for (ConditionSet cs : conditionSets.get(conditionDefId)) {
								ji.getConditionSet().add(cs);
							}
						}
						//Ubacivanje action-a u JourneyInteraction
						journeyActionId = rsJourneyInteraction.getInt("JOURNEY_ACTION_ID");
						if (!rsJourneyInteraction.wasNull()) {
							for (JourneyActionDetail ad : journeyActions.get(journeyActionId)) {
								ji.getActionDetails().add(ad);
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
