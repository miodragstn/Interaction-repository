package rs.telenor.intrep.db.tables;

import rs.telenor.intrep.db.beans.Interaction;
import rs.telenor.intrep.db.beans.InteractionInstance;
import rs.telenor.intrep.db.beans.Parameter;
import rs.telenor.intrep.db.beans.ParameterType;
import rs.telenor.intrep.db.beans.SimpleParameter;
import rs.telenor.intrep.db.ConnectionManager;
import rs.telenor.intrep.db.beans.ComplexParameter;
import rs.telenor.intrep.db.beans.RawParameter;
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
					"SIMPLE_PARAM_ORD" +
					") " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			//			ResultSet rsInt = null;
			try (
					//					Connection conn = ConnectionManager.getInstance().getConnection();
					PreparedStatement pstInt = conn.prepareStatement(sqlInt);
					PreparedStatement pstSP = conn.prepareStatement(sqlIntParam)
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
							pstSP.execute();
						}
					}
				}
			}
			interactionInstances.remove(interactionInstanceId);
		}


	}
}