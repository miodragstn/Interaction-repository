package rs.telenor.intrep.db.tables;

import rs.telenor.intrep.db.beans.Interaction;
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
		interactionHierarchy = null;
		try (
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);				
				ResultSet rs = stmt.executeQuery("SELECT INTERACTION_CLASS_ID, INTERACTION_CLASS_DESC, INTERACTION_TYPE_ID FROM INTERACTION_CLASS");
				) {
			
			Interaction inter;
			while (rs.next()) {
				inter = new Interaction();
				inter.setId(rs.getInt("INTERACTION_CLASS_ID"));
				inter.setInteractionTypeId(rs.getInt("INTERACTION_TYPE_ID"));
				inter.setDesc(rs.getString("INTERACTION_CLASS_DESC"));				
				interactionHierarchy.put(inter.getId(), inter);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
