package rs.telenor.intrep.db.tables;

import rs.telenor.intrep.db.beans.Interaction;
import rs.telenor.intrep.db.beans.InteractionInstance;
import rs.telenor.intrep.db.beans.JourneyInteraction;
import rs.telenor.intrep.db.beans.JourneyInstance;
import rs.telenor.intrep.db.beans.Parameter;
import rs.telenor.intrep.db.beans.ParameterType;
import rs.telenor.intrep.db.beans.SimpleParameter;
import rs.telenor.intrep.db.ConnectionManager;
import rs.telenor.intrep.db.beans.ComplexCondition;
import rs.telenor.intrep.db.beans.ComplexParameter;
import rs.telenor.intrep.db.beans.ConditionOperator;
import rs.telenor.intrep.db.beans.ConditionSet;
import rs.telenor.intrep.db.beans.RawParameter;
import rs.telenor.intrep.db.beans.SimpleCondition;

import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;



public class InteractionInstanceManager {
	private static HashMap<Long, InteractionInstance> interactionInstances;
	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	public static synchronized InteractionInstance createInteractionInstance(int componentId, String interactionDT, int interactionSourceId) throws SQLException {
		Long interactionInstanceId = null;
		
		interactionInstanceId =  SurrogateKeyManager.getInstance().getKeyValue("INTERACTION");		
		
		InteractionInstance interactionInstance = new InteractionInstance(interactionInstanceId, componentId, interactionDT, interactionSourceId);
		if (interactionInstances == null) interactionInstances = new HashMap<Long, InteractionInstance>();
		interactionInstances.put(interactionInstanceId, interactionInstance);
		return interactionInstance;
		
	}
	
	public static synchronized void addSimpleParameter(Long interactionInstanceId, String name, String value)
	{
		InteractionInstance inst = interactionInstances.get(interactionInstanceId);
		if (inst != null) {
		Interaction i = InteractionManager.getInteractionHierarchy().get(inst.getComponentId());
		SimpleParameter sp = null;
		if (i != null) {
			
			sp = createSimpleParameter(i, name, value);
				if (sp != null) inst.getSimpleParams().put(name, sp);
			}				
		}
			
	}
	
	private static SimpleParameter createSimpleParameter(Interaction i, String name, String value) {
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
			if (p.getValueDomain() != null && p.getValueDomain() != "") {
				String valueDomainLookupFieldValue = "";
				ParameterType dpt = p.getValueDomainValueType();
				switch (pt) {
				case ValueString: 
					valueDomainLookupFieldValue =  "'"+sp.getValueString()+"'";					
					break;
				case ValueNumber:
					valueDomainLookupFieldValue = Double.toString(sp.getValueDouble()) ;
					break;
				case ValueInt:
					valueDomainLookupFieldValue = Integer.toString(sp.getValueInt());
				default:
					break;
				}
				String domainTable = p.getValueDomain();
				String valueDomainLookupField = p.getValueDomainLookup();
				String valueDomainValueField = p.getValueDomainValue();
				String valueDomainValue;
				String pDomainSQL = "SELECT " +
												valueDomainValueField + " " +
									"FROM " + domainTable + " " +
									"WHERE " + valueDomainLookupField + "=" + valueDomainLookupFieldValue;
				try (
						PreparedStatement pstValueDomain = conn.prepareStatement(pDomainSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						) {
					ResultSet rs = pstValueDomain.executeQuery();
					if (rs.isBeforeFirst()) {
						rs.first();
						valueDomainValue = rs.getString(valueDomainValueField);
						switch (dpt) {
						case ValueInt: 
							sp.setLookupValueInt(Integer.parseInt(valueDomainValue));					
							break;
						case ValueNumber:
							sp.setLookupValueDouble(Double.parseDouble(valueDomainValue));
							break;
						case ValueString:
							sp.setLookupValueString(valueDomainValue);
						default:
							break;
						}
						sp.setValueDomainValueType(dpt);
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if (sp != null) return sp;
			else return null;
		}
		else return null;
	}
	
	public static synchronized void addComplexParameter(Long interactionInstanceId, String name) {
		InteractionInstance inst = interactionInstances.get(interactionInstanceId);
		if (inst != null) {

			Interaction i = InteractionManager.getInteractionHierarchy().get(inst.getComponentId());
			ComplexParameter cp = null;
			if (i != null) {

				Parameter p = i.getParameters().get(name);
				if (p != null) {
					cp = new ComplexParameter(p.getParamId(), p.getParamName());				
					inst.getComplexParams().put(name, cp);
				}				
			}
		}

	}
	
	public static synchronized void addSimpleParamToComplex(InteractionInstance i, String cpName, ArrayList<RawParameter> rawSimpleParams) {
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
	
	public static synchronized void findJourney(InteractionInstance intInstance) throws SQLException {
		//Nadji interakciju za InteractionInstance objekat
		Interaction inter = InteractionManager.getInteractionHierarchy().get(intInstance.getComponentId());
		//Ispitaj da li je interakcija deo nekog journey-a
		String sqlFindJourney =	"SELECT " +
											"JOURNEY_INSTANCE_ID, " +
											"JOURNEY_ID, " + 
											"JOURNEY_START_DATE, " + 
											"JOURNEY_END_DATE, " + 
											"JOURNEY_CURRENT_STEP, " + 
											"JOURNEY_IDENTIFIER_PARAM_ID, " +
											"JOURNEY_IDENTIFIER_VALUE, " + 
											"CURRENT_INTERACTION_ID, " +
											"CURRENT_INTERACTION_ORDER " +
											"FROM JOURNEYS " +
											"WHERE JOURNEY_IDENTIFIER_PARAM_ID = ? " +
											"AND JOURNEY_IDENTIFIER_VALUE = ?";
		
		ResultSet rsJourneys = null;
		JourneyInstance journeyInstance = null;
		HashMap<Long, Integer> journeysProcessed = new HashMap<Long, Integer>(); 
		for (JourneyInteraction ji : inter.getJourneys()) {
			//Nadji identifikator journey-ja
			int journeyIdentifierParamId = ji.getJourneyIdentifierParamId();
			String journeyIdentifierParamName = ji.getJourneyIdentifierParamName();
			String journeyIndentifierParamValue = "";
			if (intInstance.getSimpleParams().containsKey(journeyIdentifierParamName)) {
				ParameterType paramType = intInstance.getSimpleParams().get(journeyIdentifierParamName).getpType();
				switch (paramType) {
				case ValueString: journeyIndentifierParamValue = intInstance.getSimpleParams().get(journeyIdentifierParamName).getValueString();
				break;
				case ValueInt: journeyIndentifierParamValue = Integer.toString(intInstance.getSimpleParams().get(journeyIdentifierParamName).getValueInt());
				break;
				case ValueNumber: journeyIndentifierParamValue = Double.toString(intInstance.getSimpleParams().get(journeyIdentifierParamName).getValueDouble());
				}
				 
			}
			else continue; //ako parametra koji je identifikator journey-a nema u instanci interakcije, onda nema sta da se razmatra taj journey
			//Sad trazimo da li je journey sa nadjenim identifikatorom vec zapoceo
			try (
					//					Connection conn = ConnectionManager.getInstance().getConnection();
					PreparedStatement pstJourneys = conn.prepareStatement(sqlFindJourney, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);					
					) {
				pstJourneys.setInt(1, journeyIdentifierParamId);
				pstJourneys.setString(2, journeyIndentifierParamValue);
				rsJourneys = pstJourneys.executeQuery();
				if (rsJourneys.isBeforeFirst()) { //Ovaj metod vraca true ako je RS kursor ispred prvog reda ili RS nema redova 
					rsJourneys.first();
					journeyInstance = new JourneyInstance();
					journeyInstance.setJourneyInstanceId(rsJourneys.getLong("JOURNEY_INSTANCE_ID"));
					journeyInstance.setJourneyId(rsJourneys.getInt("JOURNEY_ID"));
					journeyInstance.setJourneyStartDt(rsJourneys.getString("JOURNEY_START_DATE"));
					journeyInstance.setJourneyEndDt(rsJourneys.getString("JOURNEY_END_DATE"));
					journeyInstance.setJourneyCurrentStep(rsJourneys.getInt("JOURNEY_CURRENT_STEP"));
					journeyInstance.setJourneyIdentifierParamId(rsJourneys.getInt("JOURNEY_IDENTIFIER_PARAM_ID"));
					journeyInstance.setJourneyIdentifierValue(rsJourneys.getString("JOURNEY_IDENTIFIER_VALUE"));
					journeyInstance.setCurrentInteractionInstanceId(rsJourneys.getLong("CURRENT_INTERACTION_ID"));
					journeyInstance.setCurrentInteractionOrder(rsJourneys.getInt("CURRENT_INTERACTION_ORDER"));														
				}
				//Sad ispitujemo da li ova interakcija ispunjava uslove da journey pocne, ako nije poceo, ili da nastavi.
				if (journeyInstance == null && ji.getPreviousStep() == 0) { //ako je ovo prva interakcija u journey-ju
					if (ji.getConditionDefId() > 0 && checkConditions(ji, intInstance)) { //Ako postoje uslovi na parmetarskom nivou, proveri ih i tek onda moze journey da se instancira
						journeyInstance = new JourneyInstance();
						journeyInstance.setJourneyInstanceId(-1);
						journeyInstance.setJourneyId(ji.getJourneyId());
						journeyInstance.setJourneyStartDt(intInstance.getInteractionDT());
						journeyInstance.setJourneyEndDt("");
						journeyInstance.setJourneyCurrentStep(1);
						journeyInstance.setJourneyIdentifierParamId(ji.getJourneyIdentifierParamId());
						journeyInstance.setJourneyIdentifierValue(journeyIndentifierParamValue);
						journeyInstance.setCurrentInteractionInstanceId(intInstance.getInteractionInstanceId());
						journeyInstance.setCurrentInteractionOrder(1);
						journeyInstance.setUpdateType(1); //insert journey-ja
						intInstance.getJourneys().add(journeyInstance);
						journeysProcessed.put(new Long(journeyInstance.getJourneyInstanceId()), new Integer(1));
					}
				}
				else
				if (!journeysProcessed.containsKey(journeyInstance.getJourneyInstanceId()) && journeyInstance.getJourneyCurrentStep() == ji.getPreviousStep() && ji.getNextStep() > 0) { 
					if (ji.getConditionDefId() > 0 && checkConditions(ji, intInstance)) {
						journeyInstance.setCurrentInteractionInstanceId(intInstance.getInteractionInstanceId());
						journeyInstance.setJourneyCurrentStep(ji.getNextStep());
						journeyInstance.setCurrentInteractionOrder(1); // TODO: razmisliti kako izracunati broj pojavljivanja interakcije u aktivnom journey-ju
						journeyInstance.setUpdateType(2); //update atributa journey-ja koji je u toku
						intInstance.getJourneys().add(journeyInstance);
						journeysProcessed.put(new Long(journeyInstance.getJourneyInstanceId()), new Integer(1));
					}
				}
				else
					if (!journeysProcessed.containsKey(journeyInstance.getJourneyInstanceId()) && ji.getComponentOrder() == -1) {
						if (ji.getConditionDefId() > 0 && checkConditions(ji, intInstance)) {
							journeyInstance.setUpdateType(3); //samo ubaci interakciju u journey, sam journey koji je u toku ne diraj. Jos nije napravljen mehanizam koji bi "samo ubacio" interakciju u journey 
							intInstance.getJourneys().add(journeyInstance);
						}
					}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (rsJourneys != null) rsJourneys.close();
			}
			
			
			
		}
		
	}
	
	static Boolean checkConditions(JourneyInteraction ji, InteractionInstance inst) {
		Boolean result = true;
		Boolean csResult = false;
		Boolean ccResult = true;
		Boolean tmpResult = true;
		SimpleParameter sp;
		ComplexParameter cp;
		String scope;
		for (ConditionSet cs : ji.getConditionSet()) {
			for (ComplexCondition cc : cs.getComplexConds()) {				
				for (SimpleCondition sc : cc.getSimpleConditions()) {
					Interaction i = InteractionManager.interactionHierarchy.get(inst.getComponentId());
					Parameter p = i.getParameters().get(sc.getParameterName()); //Nadji parametar u kontekstu interakcije
					if (p.getParentParamId() == 0) { //Ako je parametar simple, proveri uslov
						sp = inst.getSimpleParams().get(p.getParamName());
						if (sp != null) ccResult = ccResult & checkSimpleCondition(sp, sc); 
						else ccResult = ccResult & false;
					}
					else {
						cp = inst.getComplexParams().get(p.getParentParamName());
						if (cp != null) {
							tmpResult = true;
							for (HashMap<String, SimpleParameter> simpleParams : cp.getSimpleParams()) {
								sp = simpleParams.get(sc.getParameterName());
								scope = sc.getScope();
								if (scope.equals("ANY")) tmpResult = tmpResult | checkSimpleCondition(sp, sc); //Ako je scope za simple condition ANY, onda bilo koja true vrednost simple condition-a za instance simple parametra unutar kompleksnog parametra daje rezultat true 
								else tmpResult = tmpResult & checkSimpleCondition(sp, sc); //inace svi simple conditioni za sve instance simple parametra unutar complex parametra moraju davati true				
							}
							ccResult = ccResult & tmpResult; //Complex condition je Bool-ov proizvod simple condition-a
						}
						else ccResult = ccResult & false;
					}
				}
				csResult = csResult | ccResult; //Condition set je Bool-ov zbir complex condition-a
			}
			result = result & csResult;	//Konacan rezultat je Bool-ov proizvod condition set-ova
		}
		
		return result;
	}
	
	static Boolean checkSimpleCondition(SimpleParameter sp, SimpleCondition sc) {
		ConditionOperator operator = sc.getOperator(); 
		switch (operator) {
		case EQ: if (sp.getValueDomainValueType() != null) {
			if (sp.getValueDomainValueType() == ParameterType.ValueString) {
				if (sp.getLookupValueString().equalsIgnoreCase(sc.getValueString())) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueInt) {
				if (sp.getLookupValueInt() == sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueNumber) {
				if (sp.getLookupValueDouble() == sc.getValueDouble()) return true;
				else return false;
			}
		}
		else {
			if (sp.getpType() == ParameterType.ValueString) {
				if (sp.getValueString().equalsIgnoreCase(sc.getValueString())) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueInt) {
				if (sp.getValueInt() == sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueNumber) {
				if (sp.getValueDouble() == sc.getValueDouble()) return true;
				else return false;
			}
		}
		break;
		case DIF: if (sp.getValueDomainValueType() != null) {
			if (sp.getValueDomainValueType() == ParameterType.ValueString) {
				if (!sp.getLookupValueString().equalsIgnoreCase(sc.getValueString())) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueInt) {
				if (sp.getLookupValueInt() != sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueNumber) {
				if (sp.getLookupValueDouble() != sc.getValueDouble()) return true;
				else return false;
			}
		}
		else {
			if (sp.getpType() == ParameterType.ValueString) {
				if (!sp.getValueString().equalsIgnoreCase(sc.getValueString())) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueInt) {
				if (sp.getValueInt() != sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueNumber) {
				if (sp.getValueDouble() != sc.getValueDouble()) return true;
				else return false;
			}
		}
		break;
		case GT: if (sp.getValueDomainValueType() != null) {
			if (sp.getValueDomainValueType() == ParameterType.ValueString) {
				if (sp.getLookupValueString().compareToIgnoreCase(sc.getValueString()) < 0) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueInt) {
				if (sp.getLookupValueInt() < sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueNumber) {
				if (sp.getLookupValueDouble() < sc.getValueDouble()) return true;
				else return false;
			}
		}
		else {
			if (sp.getpType() == ParameterType.ValueString) {
				if (sp.getValueString().compareToIgnoreCase(sc.getValueString()) < 0) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueInt) {
				if (sp.getValueInt() < sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueNumber) {
				if (sp.getValueDouble() < sc.getValueDouble()) return true;
				else return false;
			}
		}
		break;
		case GTE: if (sp.getValueDomainValueType() != null) {
			if (sp.getValueDomainValueType() == ParameterType.ValueString) {
				if (sp.getLookupValueString().compareToIgnoreCase(sc.getValueString()) <= 0) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueInt) {
				if (sp.getLookupValueInt() <= sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueNumber) {
				if (sp.getLookupValueDouble() <= sc.getValueDouble()) return true;
				else return false;
			}
		}
		else {
			if (sp.getpType() == ParameterType.ValueString) {
				if (sp.getValueString().compareToIgnoreCase(sc.getValueString()) <= 0) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueInt) {
				if (sp.getValueInt() <= sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueNumber) {
				if (sp.getValueDouble() <= sc.getValueDouble()) return true;
				else return false;
			}
		}
		break;
		case LT: if (sp.getValueDomainValueType() != null) {
			if (sp.getValueDomainValueType() == ParameterType.ValueString) {
				if (sp.getLookupValueString().compareToIgnoreCase(sc.getValueString()) > 0) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueInt) {
				if (sp.getLookupValueInt() > sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueNumber) {
				if (sp.getLookupValueDouble() > sc.getValueDouble()) return true;
				else return false;
			}
		}
		else {
			if (sp.getpType() == ParameterType.ValueString) {
				if (sp.getValueString().compareToIgnoreCase(sc.getValueString()) > 0) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueInt) {
				if (sp.getValueInt() > sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueNumber) {
				if (sp.getValueDouble() > sc.getValueDouble()) return true;
				else return false;
			}
		}
		break;
		case LTE: if (sp.getValueDomainValueType() != null) {
			if (sp.getValueDomainValueType() == ParameterType.ValueString) {
				if (sp.getLookupValueString().compareToIgnoreCase(sc.getValueString()) >= 0) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueInt) {
				if (sp.getLookupValueInt() >= sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getValueDomainValueType() == ParameterType.ValueNumber) {
				if (sp.getLookupValueDouble() >= sc.getValueDouble()) return true;
				else return false;
			}
		}
		else {
			if (sp.getpType() == ParameterType.ValueString) {
				if (sp.getValueString().compareToIgnoreCase(sc.getValueString()) < 0) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueInt) {
				if (sp.getValueInt() >= sc.getValueInt()) return true;
				else return false;
			}
			if (sp.getpType() == ParameterType.ValueNumber) {
				if (sp.getValueDouble() >= sc.getValueDouble()) return true;
				else return false;
			}
		}
		break;
		}
		return true;
	}
	
	public static synchronized void writeInteraction2DB(Long interactionInstanceId) throws SQLException {
		InteractionInstance inst = interactionInstances.get(interactionInstanceId);
		if (inst != null) {
			String sqlInt = "INSERT INTO IR.INTERACTIONS "+
					"(" + 
					"INTERACTION_ID, " +
					"INTERACTION_DATE," +
					"COMPONENT_ID, " +
					"COMPONENT_REPETITION, " +
					"JOURNEY_ID, " +
					"COMPONENT_ORDER, " +
					"PREV_INTERACTION_ID, " +
					"JOURNEY_INSTANCE_ID, " +
					"CO_ID, " +
					"CUSTOMER_ID, " +
					"CUSTCODE, " +
					"MSISDN, " +							
					"INTERACTION_SOURCE_ID) " +
					"VALUES " +
					"(" +
					"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? " +
					")";
			String sqlIntParam = "INSERT INTO INTERACTION_PARAMETER " +
					"( " +
					"INTERACTION_ID, " +
					"PARAMETER_ID, " +
					"PARAMETER_NAME, " +
					"PARAMETER_VALUE_ID," + 
					"PARAMETER_VALUE_STRING, " +
					"PARAMETER_VALUE_NUMBER, " + 
					"PARAMETER_VALUE_DATE, " +
					"PARAMETER_VALUE_INT, " +
					"INTERACTION_DATE, " +
					"PARENT_PARAM_ID, " +
					"SIMPLE_PARAM_ORD, " +
					"LOOKUP_VALUE_FIELD, " +
					"LOOKUP_VALUE_INT, " +
					"LOOKUP_VALUE_STRING, " +
					"LOOKUP_VALUE_NUMBER " +
					") " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			String sqlJourneysInsert = "INSERT INTO JOURNEYS " +
										 "( " +
										 "JOURNEY_INSTANCE_ID, " +
										 "JOURNEY_ID, " +
										 "JOURNEY_START_DATE, " +										 										 
										 "JOURNEY_CURRENT_STEP, " +
										 "JOURNEY_IDENTIFIER_PARAM_ID, " +
										 "JOURNEY_IDENTIFIER_VALUE, " +
										 "CURRENT_INTERACTION_ID, " +
										 "CURRENT_INTERACTION_ORDER" +										 
										 ")" +
										 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			String sqlJourneysUpdate = "UPDATE JOURNEYS " +
									 "SET CURRENT_INTERACTION_ID = ?, CURRENT_INTERACTION_ORDER = ?, JOURNEY_CURRENT_STEP = ? " +
									 "WHERE JOURNEY_INSTANCE_ID = ?";
			//			ResultSet rsInt = null;
			try (
					//					Connection conn = ConnectionManager.getInstance().getConnection();
					PreparedStatement pstInt = conn.prepareStatement(sqlInt);
					PreparedStatement pstSP = conn.prepareStatement(sqlIntParam);
					PreparedStatement pstJourneysInsert = conn.prepareStatement(sqlJourneysInsert);
					PreparedStatement pstJourneysUpdate = conn.prepareCall(sqlJourneysUpdate);
					)	{
				pstInt.setLong(1, inst.getInteractionInstanceId());
				pstInt.setString(2, inst.getInteractionDT());
				pstInt.setInt(3, inst.getComponentId());
				pstInt.setNull(4, Types.NULL);
				pstInt.setNull(5, Types.NULL);
				pstInt.setNull(6, Types.NULL);
				pstInt.setNull(7, Types.NULL);
				pstInt.setNull(8, Types.NULL);
				pstInt.setNull(9, Types.NULL);
				pstInt.setNull(10, Types.NULL);
				pstInt.setNull(11, Types.NULL);
				pstInt.setNull(12, Types.NULL);
				pstInt.setLong(13, inst.getInteractionSourceId());
				pstInt.execute();
				//Upis simple parametara
				for (Map.Entry<String, SimpleParameter> meSP: inst.getSimpleParams().entrySet()) {
					pstSP.setLong(1, inst.getInteractionInstanceId());
					pstSP.setInt(2,  meSP.getValue().getParamId());
					pstSP.setString(3, meSP.getValue().getParamName());
					pstSP.setNull(4, Types.NULL); 
					pstSP.setNull(5, Types.NULL);
					pstSP.setNull(6, Types.NULL);
					pstSP.setNull(7, Types.NULL);
					pstSP.setNull(8, Types.NULL);
					pstSP.setString(9, inst.getInteractionDT());
					pstSP.setNull(10, Types.NULL);
					pstSP.setNull(11, Types.NULL);
					pstSP.setNull(12, Types.NULL);
					pstSP.setNull(13, Types.NULL);
					pstSP.setNull(14, Types.NULL);
					pstSP.setNull(15, Types.NULL);
					ParameterType pt = meSP.getValue().getpType();
					switch (pt) {
					case ValueInt: 
						pstSP.setInt(8, meSP.getValue().getValueInt());					
						break;
					case ValueNumber:
						pstSP.setDouble(6, meSP.getValue().getValueDouble());
						break;
					case ValueString:
						pstSP.setString(5, meSP.getValue().getValueString());
					default:
						break;
					}
					ParameterType dpt = meSP.getValue().getValueDomainValueType();
					if (dpt != null) {
//						pstSP.setString(12, meSP.getValue().getLookupValueString());
						switch (dpt) {
						case ValueInt: 
							pstSP.setInt(13, meSP.getValue().getLookupValueInt());					
							break;
						case ValueNumber:
							pstSP.setDouble(15, meSP.getValue().getLookupValueDouble());
							break;
						case ValueString:
							pstSP.setString(14, meSP.getValue().getLookupValueString());
						default:
							break;
						}
					}
					pstSP.execute();

				}

				//Upis complex parametara
				for (Map.Entry<String, ComplexParameter> meCP: inst.getComplexParams().entrySet()) {
					int counter = 0;
					for (HashMap<String, SimpleParameter> simpleParams : meCP.getValue().getSimpleParams()) {
						counter++;
						for (Map.Entry<String, SimpleParameter>  spEntry : simpleParams.entrySet()) {
							SimpleParameter sp = spEntry.getValue();
							pstSP.setLong(1, inst.getInteractionInstanceId());
							pstSP.setInt(2,  sp.getParamId());
							pstSP.setString(3, sp.getParamName());
							pstSP.setNull(4, Types.NULL); 
							pstSP.setNull(5, Types.NULL);
							pstSP.setNull(6, Types.NULL);
							pstSP.setNull(7, Types.NULL);
							pstSP.setNull(8, Types.NULL);
							pstSP.setString(9, inst.getInteractionDT());
							pstSP.setInt(10, meCP.getValue().getParamId());
							pstSP.setInt(11, counter);
							pstSP.setNull(12, Types.NULL);
							pstSP.setNull(13, Types.NULL);
							pstSP.setNull(14, Types.NULL);
							pstSP.setNull(15, Types.NULL);
							ParameterType pt = sp.getpType();
							switch (pt) {
							case ValueInt: 
								pstSP.setInt(8, sp.getValueInt());					
								break;
							case ValueNumber:
								pstSP.setDouble(6, sp.getValueDouble());
								break;
							case ValueString:
								pstSP.setString(5, sp.getValueString());
							default:
								break;
							}
							ParameterType dpt = sp.getValueDomainValueType();
							if (dpt != null) {
//								pstSP.setString(12, sp.get);
								switch (dpt) {
								case ValueInt: 
									pstSP.setInt(13, sp.getLookupValueInt());					
									break;
								case ValueNumber:
									pstSP.setDouble(15, sp.getLookupValueDouble());
									break;
								case ValueString:
									pstSP.setString(14, sp.getLookupValueString());
								default:
									break;
								}
							}
							pstSP.execute();
						}
					}
				}
			//Upis journey-ja
				for (JourneyInstance ji : inst.getJourneys()) {
					if (ji.getUpdateType() == 1) {
						pstJourneysInsert.setLong(1, ji.getJourneyInstanceId());
						pstJourneysInsert.setInt(2, ji.getJourneyId());
						pstJourneysInsert.setString(3, ji.getJourneyStartDt());
						pstJourneysInsert.setInt(4, ji.getJourneyCurrentStep());
						pstJourneysInsert.setInt(5, ji.getJourneyIdentifierParamId());
						pstJourneysInsert.setString(6, ji.getJourneyIdentifierValue());
						pstJourneysInsert.setLong(7, ji.getCurrentInteractionInstanceId());
						pstJourneysInsert.setInt(8, ji.getCurrentInteractionOrder());
						pstJourneysInsert.execute();
					}
					if (ji.getUpdateType() == 2) {
//						"UPDATE JOURNEYS " +
//								 "SET CURRENT_INTERACTION_ID = ?, CURRENT_INTERACTION_ORDER = ?, JOURNEY_CURRENT_STEP = ? " +
//								 "WHERE JOURNEY_INSTANCE_ID = ?";
						pstJourneysUpdate.setLong(1, ji.getCurrentInteractionInstanceId());
						pstJourneysUpdate.setInt(2, ji.getCurrentInteractionOrder());
						pstJourneysUpdate.setInt(3, ji.getJourneyCurrentStep());
						pstJourneysUpdate.setLong(4, ji.getJourneyInstanceId());
						pstJourneysUpdate.execute();
						
					}
				}
			}
			interactionInstances.remove(interactionInstanceId);
		}


	}
}