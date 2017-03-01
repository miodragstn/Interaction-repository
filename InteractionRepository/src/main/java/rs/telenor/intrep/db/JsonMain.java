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

public class JsonMain {

	public static void main(String[] args) {
		
		
		// write JSON
//				try {
//					System.out.println("start");
//					JsonManager.jsonWrite();
//					System.out.println("end");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				
//			write JSONtoDB
				try {
					JsonManager.jsonWriteToDb();
				} catch (SQLException e) {
					e.printStackTrace();
				}

	}

}
