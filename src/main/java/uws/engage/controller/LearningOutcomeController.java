package uws.engage.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.simple.JSONObject;



/**
 * This class allows access to the learning outcomes table of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class LearningOutcomeController {
	
	// ********************************** Parameters ********************************** //
	
		/**
		 * Connection to the database
		 */
		private Connection conn;
		
		/**
		 * General useful variables (names of databases, fields...)
		 */
		private General g;
		
		// ********************************** Builders ********************************** //
		
		/**
		 * Creates an outcome controller => gives access to the useful CRUD actions on table learningoutcome
		 * @throws Exception
		 */
		public LearningOutcomeController( ) throws Exception
		{
			g = new General();
			Class.forName("com.mysql.jdbc.Driver");		
			conn = DriverManager.getConnection(g.DB_NAME, g.DB_USERNAME, g.DB_PASSWD);
		}
		public void finalize() throws Exception
	    {
	    	conn.close();
	    	if (g.DEBUG)
			{
				System.out.println("*** connection closed ***");
			}  
	    }
		
		// ********************************** Methods ********************************** //
		
		/**
		 * Creates a new outcome in the database
		 * @param name name of the outcome
		 * @param lo json of the outcome
		 * @param idSG id of the SG the outcome is associated to
		 * @return id of the outcome if the creation was successful, 
		 * g.CST_RETURN_WRONG_ID if the idSG is invalid, g.CST_RETURN_SQL_ERROR if the query didn't work
		 */
		public int createLearningOutcome(String name, JSONObject lo, int idSG, int version)
		{
			if (g.DEBUG)
			{
				System.out.println("*** CreateLearningOutcome - from JSON ***");
			}
			try
			{
				PreparedStatement stCreateOutcome = 
						conn.prepareStatement("INSERT INTO "+ g.TABLE_OUTCOME +
									"("+ g.O_FIELD_NAME + ", "+ g.O_FIELD_DESC + ", "+ g.O_FIELD_TYPE + ", "+ 
									g.O_FIELD_VALUE+ ", "+ g.O_FIELD_ID_SG+ ", "+ g.O_FIELD_VERSION_SG +
									") VALUES ( ?, ?, ?, ?, ?, ? )");

				stCreateOutcome.setString(1, name);
				if (!lo.containsKey("desc")){ stCreateOutcome.setNull(2, java.sql.Types.NULL); } 
					else { stCreateOutcome.setString(2, (String) lo.get("desc")); }
				
				if (!lo.containsKey("type")) 
				{
					stCreateOutcome.setNull(3, java.sql.Types.NULL);
				}
				else
				{
					stCreateOutcome.setString(3, lo.get("type").toString().toUpperCase());
				}			
				
				stCreateOutcome.setInt(4, Integer.parseInt(lo.get("value").toString()));
				stCreateOutcome.setInt(5, idSG);
				stCreateOutcome.setInt(6, version);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stCreateOutcome.toString());
				}
				
				stCreateOutcome.executeUpdate();
				
				int idToReturn = getOutcomeIdByName(name, idSG, version);
				
				if (g.DEBUG)
				{
					System.out.println("SUCCESS : id=" + idToReturn);
					System.out.println();
				}
				
				return idToReturn;
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (CreateLO): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
		/**
		 * Returns the id of an outcome by name
		 * @param idSG id of the current game 
		 * @return appropriate learning outcome, null if none found
		 */
		public int getOutcomeIdByName(String name, int idSG, int version)
		{
			if (g.DEBUG)
			{
				System.out.println("*** GetLearningOutcomeByName ***");
			}
			try
			{
				PreparedStatement stGetOutcome = 
						conn.prepareStatement("SELECT "+ g.O_FIELD_ID + ", " + g.O_FIELD_NAME + ", "+ g.O_FIELD_DESC +
												", "+ g.O_FIELD_TYPE + ", "+ g.O_FIELD_VALUE + " FROM " + g.TABLE_OUTCOME + 
												" WHERE " + g.O_FIELD_NAME + " = ? AND " + g.O_FIELD_ID_SG + " = ? AND " 
												+ g.O_FIELD_VERSION_SG + " = ? ");
				
				stGetOutcome.setString(1, name);
				stGetOutcome.setInt(2, idSG);
				stGetOutcome.setInt(3, version);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stGetOutcome.toString());
				}
				
				ResultSet results = stGetOutcome.executeQuery();
				
				if (results.next())
				{
					if (g.DEBUG_SQL)
					{
						System.out.println();
					}
					
					return results.getInt(1);
				}
				return g.CST_RETURN_SQL_ERROR;			
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (Get LO by name): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
		public JSONObject getOutcomeById(int idOutcome)
		{
			if (g.DEBUG)
			{
				System.out.println("*** getOutcomeById ***");
			}
			try
			{
				PreparedStatement stGetOutcome = 
						conn.prepareStatement("SELECT "+ g.O_FIELD_NAME + ", "+ g.O_FIELD_DESC +
												", "+ g.O_FIELD_TYPE + ", "+ g.O_FIELD_VALUE + " FROM " + g.TABLE_OUTCOME + 
												" WHERE " + g.O_FIELD_ID + " = ?");
				
				stGetOutcome.setInt(1, idOutcome);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stGetOutcome.toString());
				}
				
				ResultSet results = stGetOutcome.executeQuery();
				
				if (results.next())
				{
					if (g.DEBUG_SQL)
					{
						System.out.println();
					}
					
					JSONObject outcome = new JSONObject();
					outcome.put(g.O_FIELD_ID, idOutcome);
					outcome.put(g.O_FIELD_NAME, results.getString(1));
					outcome.put(g.O_FIELD_DESC, results.getString(2));
					outcome.put(g.O_FIELD_TYPE, results.getString(3));
					outcome.put(g.O_FIELD_VALUE, results.getFloat(4));
					
					return outcome;
				}
				return null;			
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (getOutcomeById): " + e.getMessage());
				return null;
			}
		}

		public ArrayList<JSONObject> getOutcomeListByGame(int idSG, int version)
		{
			if (g.DEBUG)
			{
				System.out.println("*** GetLearningOutcomeByGame ***");
			}
			try
			{
				PreparedStatement stGetOutcomes = 
						conn.prepareStatement("SELECT "+ g.O_FIELD_ID + ", " + g.O_FIELD_NAME + ", "+ g.O_FIELD_DESC +
												", "+ g.O_FIELD_TYPE + ", "+ g.O_FIELD_VALUE + " FROM " + g.TABLE_OUTCOME + 
												" WHERE " + g.O_FIELD_ID_SG + " = ? AND " + g.O_FIELD_VERSION_SG + " = ?");

				stGetOutcomes.setInt(1, idSG);
				stGetOutcomes.setInt(2, version);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stGetOutcomes.toString());
				}
				
				ResultSet results = stGetOutcomes.executeQuery();
				
				ArrayList<JSONObject> list = new ArrayList<JSONObject>();
				
				while (results.next())
				{
					JSONObject outcome = new JSONObject();
					outcome.put(g.O_FIELD_ID, results.getInt(1));
					outcome.put(g.O_FIELD_NAME, results.getString(2));
					outcome.put(g.O_FIELD_DESC, results.getString(3));
					outcome.put(g.O_FIELD_TYPE, results.getString(4));
					outcome.put(g.O_FIELD_VALUE, results.getFloat(5));

					list.add(outcome);
				}
				return list;			
			}
			catch (Exception e)
			{		
				System.err.println("ERROR: " + e.getMessage());
				return null;
			}
		}

		
}
