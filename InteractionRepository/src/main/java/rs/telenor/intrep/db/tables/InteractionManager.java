package rs.telenor.intrep.db.tables;

import rs.telenor.intrep.db.beans.Interaction;
import rs.telenor.intrep.db.beans.Parameter;
import rs.telenor.intrep.db.beans.ParameterType;
import rs.telenor.intrep.db.ConnectionManager;
import java.util.HashMap;
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
		
		try (
				Statement stmtInt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtParam = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				Statement stmtChildParam = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rsInt = stmtInt.executeQuery("SELECT INTERACTION_CLASS_ID, INTERACTION_CLASS_DESC, INTERACTION_TYPE_ID FROM INTERACTION_CLASS");
				ResultSet rsParam = stmtParam.executeQuery("SELECT  ICP.INTERACTION_CLASS_ID," +
															   "ICP.PARAMETER_ID," + 
															   "P.NAME," + 
														       "P.TYPE_ID," +
														       "P.VALUE_DOMAIN," +
														       "P.VALUE_DOMAIN_FIELD " +  
														"FROM INTERACTION_CLASS_PARAMETER icp " +
														"join PARAMETER p " +
														"on ICP.PARAMETER_ID = P.ID " +
														"WHERE p.PARENT_PARAMETER_ID IS NULL ");
				ResultSet rsChildParam = stmtChildParam.executeQuery("SELECT  ICP.INTERACTION_CLASS_ID, " +
															   "ICP.PARAMETER_ID, " + 
															   "P.NAME, " + 
															   "parentP.NAME as PARENTNAME, " +
														       "P.TYPE_ID, " +
														       "P.VALUE_DOMAIN, " +
														       "P.VALUE_DOMAIN_FIELD " +  
														"FROM INTERACTION_CLASS_PARAMETER icp " +
														"join PARAMETER p " +
														"on ICP.PARAMETER_ID = P.ID " +
														"join PARAMETER parentP " + 
														"on p.PARENT_PARAMETER_ID = parentP.ID " +
														"WHERE p.PARENT_PARAMETER_ID IS NOT NULL ");
				) {
			
			Interaction inter;
//			Parameter param;
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
							Parameter parent = inter.getParameters().get(rsChildParam.getString("PARENTNAME")); //trazim Map sa parent parametrima 
							if (parent != null) {
								parent.addChildParam(p); //ako ga nadjem, dodajem parametar u mapu child parametara
							}
						}
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
