package rs.telenor.intrep.db;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;

import rs.telenor.intrep.db.beans.Interaction;
import rs.telenor.intrep.db.beans.InteractionInstance;
import rs.telenor.intrep.db.beans.RawParameter;
import rs.telenor.intrep.db.tables.InteractionInstanceManager;
import rs.telenor.intrep.db.tables.InteractionManager;

public class JsonManager {

	private static final Logger serviceLog = Logger
            .getLogger(JsonManager.class);

	//  static JasonData jsonData = new JasonData();

	public static void jsonWriteToDb(String JSONData) throws SQLException{
		HashMap<Integer, Interaction> interactions = InteractionManager.getInteractionHierarchy();
		InteractionInstance inst = null;

//		JasonData jsonData = new JasonData();
		//	   jsonWrite();

		try {
			// 60, 61, 62, 63, 67
			inst = InteractionInstanceManager.createInteractionInstance(67, "2017-02-22", 7);
			
			JsonFactory jasonFactory = new JsonFactory();

			// read JSON by nodes
//			JsonParser jsonParser = jasonFactory.createJsonParser(new File("example.json"));
			JsonParser jsonParser = jasonFactory.createJsonParser(JSONData);

			System.out.println("looping\n");
			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {

				//get the current token
				String fieldname = jsonParser.getCurrentName();

				// move
				if (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME)	{
					jsonParser.nextToken();  
					}

				//    		  System.out.println(jsonParser.getCurrentName()+"-"+jsonParser.getText());
				String nodeType = (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() );
				System.out.println( nodeType+"-"+jsonParser.getText());

				//				interaction
				InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), jsonParser.getCurrentName(),jsonParser.getText());

				// basketItems
				if("basketItems".equals(fieldname)){

					boolean endOfLoop = false;
					
					InteractionInstanceManager.addComplexParameter(inst.getInteractionInstanceId(), "basketItems");
					
					
					//move to [ and loop till token equal to "]"
					while (jsonParser.nextToken() != JsonToken.END_ARRAY ) {

//						"START_OBJECT"
//			        	System.out.println( (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() ) );						

			        	ArrayList<RawParameter> rawSimpleParams = new ArrayList<RawParameter>();
						RawParameter rp = null;

						while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
//						while (jsonParser.nextToken() != null ) {

							nodeType = (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() );
							            
							if (nodeType.equals("START_OBJECT")){ 
//								empty list
								rp = null;
							}
							
							// move one token
							if (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME)
							{   
								jsonParser.nextToken(); 
								nodeType = (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() );
							} 
							
							// ignore { and [
							if (jsonParser.getText().equals("]")) {
								endOfLoop = true;
							}
							
							if ( nodeType.equals("END_OBJECT") )
							{	
//								adding params at the end of object
								InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
//								clear lists
								rp = null;
								rawSimpleParams.clear();
							}

							System.out.println("\tinner: "+/*jsonParser.getCurrentToken().name() +" : "+*/nodeType+" - "+jsonParser.getText());
							rp = new RawParameter(jsonParser.getCurrentName(),jsonParser.getText());
							rawSimpleParams.add(rp);

						}

						InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
					}
				}
			}
			
			InteractionInstanceManager.findJourney(inst);
			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// write      
		System.out.println("\n======================================================\n");
	}

	public static void jsonWriteToDb(String JSONData, InteractionInstance inst) throws SQLException{
		
    	serviceLog.info("jsonWriteToDb_MX"+JSONData);
		HashMap<Integer, Interaction> interactions = InteractionManager.getInteractionHierarchy(serviceLog);
		String dataStr = "{"+JSONData+"}";
		try {
			JsonFactory jasonFactory = new JsonFactory();

			// read JSON by nodes
//			JsonParser jsonParser = jasonFactory.createJsonParser(new File("example.json"));
			JsonParser jsonParser = jasonFactory.createJsonParser(dataStr);

//			System.out.println("looping\n");
			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {

				//get the current token
				String fieldname = jsonParser.getCurrentName();

				// move
				if (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME)	{
					jsonParser.nextToken();  
					}

				//    		  System.out.println(jsonParser.getCurrentName()+"-"+jsonParser.getText());
				String nodeType = (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() );
				System.out.println( nodeType+"-"+jsonParser.getText());
				serviceLog.info("jsonWriteToDb_MX "+nodeType+"-"+jsonParser.getText());
				//				interaction
				InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), jsonParser.getCurrentName(),jsonParser.getText());

				// basketItems
				if("basketItems".equals(fieldname)){

					boolean endOfLoop = false;
					
					InteractionInstanceManager.addComplexParameter(inst.getInteractionInstanceId(), "basketItems");
					
					
					//move to [ and loop till token equal to "]"
					while (jsonParser.nextToken() != JsonToken.END_ARRAY ) {

//						"START_OBJECT"
//			        	System.out.println( (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() ) );						

			        	ArrayList<RawParameter> rawSimpleParams = new ArrayList<RawParameter>();
						RawParameter rp = null;

						while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
//						while (jsonParser.nextToken() != null ) {

							nodeType = (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() );
							            
							if (nodeType.equals("START_OBJECT")){ 
//								empty list
								rp = null;
							}
							
							// move one token
							if (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME)
							{   
								jsonParser.nextToken(); 
								nodeType = (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() );
							} 
							
							// ignore { and [
							if (jsonParser.getText().equals("]")) {
								endOfLoop = true;
							}
							
							if ( nodeType.equals("END_OBJECT") )
							{	
//								adding params at the end of object
								InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
//								clear lists
								rp = null;
								rawSimpleParams.clear();
							}
							serviceLog.info("jsonWriteToDb_MX "+nodeType+" - "+jsonParser.getText()) ;
//							System.out.println("\tinner: "+/*jsonParser.getCurrentToken().name() +" : "+*/nodeType+" - "+jsonParser.getText());
							rp = new RawParameter(jsonParser.getCurrentName(),jsonParser.getText());
							rawSimpleParams.add(rp);

						}

						InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
					}
				}
			}
			
			InteractionInstanceManager.findJourney(inst);
			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());

		} catch (JsonParseException e) {
			serviceLog.error("JSONManager_MX "+e.getMessage(),e) ;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			serviceLog.error("JSONManager_MX "+e.getMessage(),e) ;
		} catch (IOException e) {
			e.printStackTrace();
			serviceLog.error("JSONManager_MX "+e.getMessage(),e) ;
		}

		// write      
		System.out.println("\n======================================================\n");
	}

	public static void jsonWriteToDb() throws SQLException{
		HashMap<Integer, Interaction> interactions = InteractionManager.getInteractionHierarchy();
		InteractionInstance inst = null;
		
		try {
			JsonFactory jasonFactory = new JsonFactory();

			// read JSON by nodes
			JsonParser jsonParser = jasonFactory.createJsonParser(new File("example.json"));
//			JsonParser jsonParser = jasonFactory.createJsonParser(JSONData);

			System.out.println("looping\n");
			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {

				//get the current token
				String fieldname = jsonParser.getCurrentName();

				// move
				if (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME)	{
					jsonParser.nextToken();  
					}

				//    		  System.out.println(jsonParser.getCurrentName()+"-"+jsonParser.getText());
				String nodeType = (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() );
				System.out.println( nodeType+"-"+jsonParser.getText());

				//				interaction
				InteractionInstanceManager.addSimpleParameter(inst.getInteractionInstanceId(), jsonParser.getCurrentName(),jsonParser.getText());

				// basketItems
				if("basketItems".equals(fieldname)){

					boolean endOfLoop = false;
					
					InteractionInstanceManager.addComplexParameter(inst.getInteractionInstanceId(), "basketItems");
					
					
					//move to [ and loop till token equal to "]"
					while (jsonParser.nextToken() != JsonToken.END_ARRAY ) {

//						"START_OBJECT"
//			        	System.out.println( (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() ) );						

			        	ArrayList<RawParameter> rawSimpleParams = new ArrayList<RawParameter>();
						RawParameter rp = null;

						while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
//						while (jsonParser.nextToken() != null ) {

							nodeType = (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() );
							            
							if (nodeType.equals("START_OBJECT")){ 
//								empty list
								rp = null;
							}
							
							// move one token
							if (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME)
							{   
								jsonParser.nextToken(); 
								nodeType = (jsonParser.getCurrentName() == null ? jsonParser.getCurrentToken().toString() : jsonParser.getCurrentName() );
							} 
							
							// ignore { and [
							if (jsonParser.getText().equals("]")) {
								endOfLoop = true;
							}
							
							if ( nodeType.equals("END_OBJECT") )
							{	
//								adding params at the end of object
								InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
//								clear lists
								rp = null;
								rawSimpleParams.clear();
							}

							System.out.println("\tinner: "+/*jsonParser.getCurrentToken().name() +" : "+*/nodeType+" - "+jsonParser.getText());
							rp = new RawParameter(jsonParser.getCurrentName(),jsonParser.getText());
							rawSimpleParams.add(rp);

						}

						InteractionInstanceManager.addSimpleParamToComplex(inst, "basketItems", rawSimpleParams);
					}
				}
			}
			
			InteractionInstanceManager.findJourney(inst);
			InteractionInstanceManager.writeInteraction2DB(inst.getInteractionInstanceId());

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// write      
		System.out.println("\n======================================================\n");
	}
	public static void jsonWrite(){
		try {
			System.out.println("generate JSON");
			//// generate JSON 
			JsonFactory jasonFactory = new JsonFactory();

			JsonGenerator jsonGenerator = jasonFactory.createJsonGenerator(new File("example.json"), JsonEncoding.UTF8);
			//	         jsonGenerator.writeStartObject();
			//	         
			//	         jsonGenerator.writeNumberField("interactionTypeID", 100);
			//	         jsonGenerator.writeStringField("interactionDateTime", "2016-12-07 10:00:00");
			//	         jsonGenerator.writeNumberField("interactionSystem", 7);
			//	         jsonGenerator.writeNumberField("basketId", 1000);
			//	         jsonGenerator.writeStringField("sessionId", "abcdef1234567890");
			//	         
			//	         jsonGenerator.writeFieldName("paramList"); 
			jsonGenerator.writeStartObject();

			jsonGenerator.writeStringField("role", "customer");
			jsonGenerator.writeStringField("channel", "web");
			jsonGenerator.writeStringField("journeyFacingMsisdn", "38163230238");
			jsonGenerator.writeStringField("deliveryMethod", "sto");
			jsonGenerator.writeStringField("paymentMethod", "online");

			jsonGenerator.writeFieldName("basketItems");

			jsonGenerator.writeStartArray(); // [
			jsonGenerator.writeStartObject();// {

			jsonGenerator.writeStringField("msisdn", "38163230000");
			jsonGenerator.writeBooleanField("existingCustomer", true); 
			jsonGenerator.writeNumberField("tmCode", 100);
			jsonGenerator.writeNumberField("deviceId", 10);
			jsonGenerator.writeStringField("devicePayment", "subsidy");
			jsonGenerator.writeBooleanField("additionalTerms", true); 

			jsonGenerator.writeEndObject();

			jsonGenerator.writeStartObject();

			jsonGenerator.writeStringField("msisdn", "38163230001");
			jsonGenerator.writeBooleanField("existingCustomer", false); 
			jsonGenerator.writeNumberField("tmCode", 200);
			jsonGenerator.writeNumberField("deviceId", 15);
			jsonGenerator.writeStringField("devicePayment", "cosmos");
			jsonGenerator.writeBooleanField("additionalTerms", false); 

			jsonGenerator.writeEndObject();
			jsonGenerator.writeEndArray(); 

			jsonGenerator.writeEndObject(); 

			jsonGenerator.close();    

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void writeHM(LinkedHashMap prmCollection) {

		// write      
		System.out.println("size: "+prmCollection.size());
		System.out.println("\n======================================================\n");

		for(Object key : prmCollection.keySet()) {
			Object value = prmCollection.get(key);
			System.out.println(key+"-"+prmCollection.get(key));

		}
	}


	public static void writeLA(ArrayList prmCollection) {

		// write      
		System.out.println("size: "+prmCollection.size());
		System.out.println("\n======================================================\n");

		for(Object key : prmCollection) {
			System.out.println("-"+key);

		}
	}


}