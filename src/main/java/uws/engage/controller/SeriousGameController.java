package uws.engage.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Set;


/**
 * This class allows access to the serious game table of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class SeriousGameController {

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
	 * Creates a seriousGame controller => gives access to the useful CRUD actions on table SeriousGame
	 * @throws Exception
	 */
	public SeriousGameController( ) throws Exception
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
	
	public int createSeriousGame(JSONObject sg)
	{
		if (g.DEBUG)
		{
			System.out.println("*** CreateSeriousGame - from JSON ***");
		}
		try
		{
			String sqlQuery;
			int thereIsId = 0;
			if (sg.containsKey("id"))
			{
				sqlQuery = "INSERT INTO "+ g.TABLE_SG + "("+ g.SG_FIELD_ID + ", "+ g.SG_FIELD_VERSION + 						
						", "+ g.SG_FIELD_ID_TEACHER + ", "+ g.SG_FIELD_NAME + ", "+ g.SG_FIELD_DESC + 
						", "+ g.SG_FIELD_AGEMIN +", "+ g.SG_FIELD_AGEMAX + ", "+ g.SG_FIELD_LANG + ", " + 
						g.SG_FIELD_COUNTRY + ", "+ g.SG_FIELD_PUBLIC + ", "+ g.SG_FIELD_COMMENTS +
						", "+ g.SG_FIELD_ID_DEVELOPER + ", " + g.SG_FIELD_NAME_VERSION +
						") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
				thereIsId = 1;
			}
			else 
			{
				sqlQuery = "INSERT INTO "+ g.TABLE_SG + "("+ g.SG_FIELD_VERSION +
						", "+ g.SG_FIELD_ID_TEACHER + ", "+ g.SG_FIELD_NAME + ", "+ g.SG_FIELD_DESC + 
						", "+ g.SG_FIELD_AGEMIN +", "+ g.SG_FIELD_AGEMAX + ", "+ g.SG_FIELD_LANG + ", " + 
						g.SG_FIELD_COUNTRY + ", "+ g.SG_FIELD_PUBLIC + ", "+ g.SG_FIELD_COMMENTS +
						", "+ g.SG_FIELD_ID_DEVELOPER+ ", " + g.SG_FIELD_NAME_VERSION +
						") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
			}
			
			PreparedStatement stCreationGame = conn.prepareStatement(sqlQuery);

			if (sg.containsKey("id")){ stCreationGame.setInt(1, Integer.parseInt(sg.get("id").toString()));}
			
			if (!sg.containsKey("version")){ stCreationGame.setInt(1+thereIsId, 0); } 
				else { stCreationGame.setInt(1+thereIsId, Integer.parseInt(sg.get("version").toString())); }
			if (!sg.containsKey("idTeacher")){ stCreationGame.setNull(2+thereIsId, java.sql.Types.NULL); }
				else { stCreationGame.setInt(2+thereIsId, Integer.parseInt(sg.get("idTeacher").toString())); }
			
			stCreationGame.setString(3+thereIsId, (String) sg.get("name"));	
			if (!sg.containsKey("description")){ stCreationGame.setNull(4+thereIsId, java.sql.Types.NULL); } 
				else { stCreationGame.setString(4+thereIsId, sg.get("description").toString()); }
			if (!sg.containsKey("ageMin")){ stCreationGame.setNull(5+thereIsId, java.sql.Types.NULL); } 
				else { stCreationGame.setInt(5+thereIsId, Integer.parseInt(sg.get("ageMin").toString()));}	
			if (!sg.containsKey("ageMax")){ stCreationGame.setNull(6+thereIsId, java.sql.Types.NULL); } 
				else { stCreationGame.setInt(6+thereIsId, Integer.parseInt(sg.get("ageMax").toString()));}		
			if (!sg.containsKey("lang")){ stCreationGame.setNull(7+thereIsId, java.sql.Types.NULL); } 
				else { stCreationGame.setString(7+thereIsId, sg.get("lang").toString()); }
			if (!sg.containsKey("country")){ stCreationGame.setNull(8+thereIsId, java.sql.Types.NULL); } 
			else { stCreationGame.setString(8+thereIsId, sg.get("country").toString()); }
			if (!sg.containsKey("public")){ stCreationGame.setBoolean(9+thereIsId, false); } 
			else { stCreationGame.setBoolean(9+thereIsId, sg.get("public").toString().equals("true")); }
			if (!sg.containsKey("comments")){ stCreationGame.setNull(10+thereIsId, java.sql.Types.NULL); } 
				else { stCreationGame.setString(10+thereIsId, sg.get("comments").toString()); }
			
			stCreationGame.setInt(11+thereIsId, Integer.parseInt(sg.get("idDeveloper").toString()));
			
			if (!sg.containsKey("nameVersion")){ stCreationGame.setNull(12+thereIsId, java.sql.Types.NULL); } 
				else { stCreationGame.setString(12+thereIsId, sg.get("nameVersion").toString()); }
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stCreationGame.toString());
			}
			
			stCreationGame.executeUpdate();
			
			PreparedStatement stGetLastId = conn.prepareStatement("SELECT @@IDENTITY");
			ResultSet resId = stGetLastId.executeQuery();
			int idToReturn;
			
			if (resId.next())
			{
				idToReturn = resId.getInt(1);
			}
			else 
			{
				idToReturn = g.CST_RETURN_SQL_ERROR;
			}
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS : id=" + idToReturn);
				System.out.println("*** End of CreateSeriousGame - from JSON ***");
			}
			
			return idToReturn;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (CreateSG): " + e.getMessage());
			System.out.println("*** End of CreateSeriousGame - from JSON ***");
			return g.CST_RETURN_SQL_ERROR;
		}
	}

	public int getNewVersion(int idSG) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getNewVersion ***");
		}
		PreparedStatement stGetNewVersion = 
				conn.prepareStatement("SELECT MAX(" + g.SG_FIELD_VERSION + ")+1 as newVersion" +
										" FROM  "+ g.TABLE_SG + " WHERE " + 
									g.SG_FIELD_ID + " = ? ");

		stGetNewVersion.setInt(1, idSG);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stGetNewVersion.toString());
		}
		
		ResultSet results = stGetNewVersion.executeQuery();
		
		if (results.next())
		{
			if (g.DEBUG_SQL)
			{
				System.out.println();
			}
			
			int newVersion = results.getInt(1);
			
			return newVersion;
			
		}
		else
		{
			return -1;
		}
	}
	
	public int saveCF(String configFile, int idSG, int version)
	{
		if (g.DEBUG)
		{
			System.out.println("*** saveCF for SG : "+idSG+" ***");
		}
		try
		{
			// there are 2 fields for the CF:
			// one is of type TEXT for CF under 64 KB
			// the other one is MEDIUMTEXT for CF between 64 KB and 16 MB
			// the database is encoded in  UTF-8 therefore every character can be 1 to 6 bytes, we take 6 for security
			String dataTypeForCF = (configFile.length() < (65500 / 6))? g.SG_FIELD_CONFIG_SHORT : g.SG_FIELD_CONFIG_LONG ;
			
			PreparedStatement stsaveCF = 
					conn.prepareStatement("UPDATE "+ g.TABLE_SG + " SET "+ dataTypeForCF + 
							" = ? WHERE " + g.SG_FIELD_ID + " = " + idSG +
							" AND " + g.SG_FIELD_NAME_VERSION + " = " + version);

			stsaveCF.setString(1, configFile);				
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stsaveCF.toString());
			}
			
			stsaveCF.executeUpdate();
			
			int idToReturn = g.CST_RETURN_SUCCESS;
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println("*** End of saveCF ***");
			}
			
			return idToReturn;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (saveCF): " + e.getMessage());
			System.out.println("*** End of saveCF ***");
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	public JSONObject getConfigFile(int idSG, int version) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getConfigFile ***");
		}
		PreparedStatement stGetSG = 
				conn.prepareStatement("SELECT " + g.SG_FIELD_CONFIG_LONG + ", "+ g.SG_FIELD_CONFIG_SHORT +
										" FROM  "+ g.TABLE_SG + " WHERE " + 
									g.SG_FIELD_ID + " = ? AND " + g.SG_FIELD_VERSION + " = ?");

		stGetSG.setInt(1, idSG);
		stGetSG.setInt(2, version);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stGetSG.toString());
		}
		
		ResultSet results = stGetSG.executeQuery();
		
		if (results.next())
		{
			if (g.DEBUG_SQL)
			{
				System.out.println();
			}
			
			String configFile = (results.getString(1) != null)? results.getString(1) : results.getString(2);
			JSONObject configFileJson = (JSONObject) new JSONParser().parse(configFile);
			
			return configFileJson;
			
		}
		else
		{
			return new JSONObject();
		}
	}

	public JSONObject getSGById(int idSG, int version)
	{
		if (g.DEBUG)
		{
			System.out.println("*** getSGById ***");
		}
		try
		{
			
			PreparedStatement stGetSG = 
					conn.prepareStatement("SELECT " + g.SG_FIELD_ID_TEACHER + ", "+ g.SG_FIELD_NAME + ", "+ g.SG_FIELD_DESC + 
											", "+ g.SG_FIELD_AGEMIN +", "+ g.SG_FIELD_AGEMAX + ", "+ g.SG_FIELD_LANG + ", " + 
											g.SG_FIELD_COUNTRY + ", "+ g.SG_FIELD_PUBLIC + ", "+ g.SG_FIELD_COMMENTS +
											" FROM  "+ g.TABLE_SG + " WHERE " + 
										g.SG_FIELD_ID + " = ? AND " + g.SG_FIELD_VERSION + " = ?");

			stGetSG.setInt(1, idSG);
			stGetSG.setInt(2, version);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetSG.toString());
			}
			
			ResultSet results = stGetSG.executeQuery();
			
			if (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject sg = new JSONObject();
				sg.put(g.SG_FIELD_ID, idSG);
				sg.put(g.SG_FIELD_VERSION, version);
				sg.put(g.SG_FIELD_ID_TEACHER, results.getInt(1));
				sg.put(g.SG_FIELD_NAME, results.getString(2));
				sg.put(g.SG_FIELD_DESC, results.getString(3));
				sg.put(g.SG_FIELD_AGEMIN, results.getInt(4));
				sg.put(g.SG_FIELD_AGEMAX, results.getInt(5));
				sg.put(g.SG_FIELD_LANG, results.getString(6));
				sg.put(g.SG_FIELD_COUNTRY, results.getString(7));
				sg.put(g.SG_FIELD_PUBLIC, results.getBoolean(8));
				sg.put(g.SG_FIELD_COMMENTS, results.getString(9));
				
				return sg;
				
			}
			else
			{
				return new JSONObject();
			}
			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getSGById): " + e.getMessage());
			return null;
		}
	}

	public int createSG (JSONObject configFile) throws Exception
	{
		
		
		// ##################### SERIOUS GAME TABLE ######################		
		// Create a row in the seriousgame table
				
		SeriousGameController sgController = new SeriousGameController();
		JSONObject sg = (JSONObject)configFile.get("seriousGame");
		int idSG = sgController.createSeriousGame(sg);
		int version = 0;
		if (sg.containsKey("id"))
		{
			idSG = Integer.parseInt(sg.get("id").toString());
			version = Integer.parseInt(sg.get("version").toString());
		}
		
		System.out.println(idSG);
		System.out.println(version);
		
		if (idSG > 0)
		{			
			// ##################### PLAYER TABLE ######################		
			// Create a table player based on the SG player's characteristics
			

			PlayerController playerController = new PlayerController();
			if (configFile.get("player") != null)
			{
				playerController.createTablePlayer((ArrayList<JSONObject>)configFile.get("player"), idSG, version);
			}
			else
			{
				playerController.createTablePlayer(new ArrayList<JSONObject>(), idSG, version);
			}
			playerController.finalize();	
			
			// ##################### FEEDBACK MESSAGES TABLE ######################
			// Creates rows in table feedback message linked to this SG
						
			JSONObject feedback = new JSONObject() ;
			if (configFile.get("feedback") != null) { feedback = (JSONObject) configFile.get("feedback"); }
			FeedbackController fdbckController = new FeedbackController();
			
			Set<String> keys = feedback.keySet();
			for (String key : keys)
			{
				fdbckController.createFeedback(key, (JSONObject)feedback.get(key), idSG, version);
			}
			fdbckController.finalize();
			
			
			// ##################### FEEDBACK_TRIGGER TABLE ######################
			// Creates rows in table feedback_trigger describing the feedback model of this SG

			FeedbackTriggerController triggerController = new FeedbackTriggerController();

			ArrayList<JSONObject> triggersInactivity = new ArrayList<JSONObject>() ;
			if (configFile.get("inactivityFeedback") != null) { triggersInactivity = (ArrayList<JSONObject>) configFile.get("inactivityFeedback"); }
			
			for (JSONObject t : triggersInactivity) {
				triggerController.addFeedbackTriggerInactivity(idSG, t, version);
			}
			
			
			// ##################### LEARNING_OUTCOME / FEEDBACK_TRIGGER TABLES ######################
			// Creates rows in table learning outcome linked to the SG
			
			JSONObject los = (JSONObject) configFile.get("learningOutcomes");
			LearningOutcomeController LOController = new LearningOutcomeController();
			
			keys = los.keySet();
			for (String key : keys) 
			{
				JSONObject lo = (JSONObject) los.get(key);
				int idLO = LOController.createLearningOutcome(key, lo, idSG, version);
				
				if (lo.containsKey("feedbackTriggered"))
				{
					// create rows in table feedback_trigger 
					ArrayList<JSONObject> triggers = (ArrayList<JSONObject>) lo.get("feedbackTriggered");
					for (JSONObject t : triggers) {
						triggerController.addFeedbackTriggerOutcome(idSG, idLO, t, version);
					}
				}
			}
			triggerController.finalize();
			LOController.finalize();
			// ##################### SAVE CONFIG FILE ######################
			// Update SG table to save config file
			
			sgController.saveCF(configFile.toJSONString(), idSG, version);
				
		}
		sgController.finalize();
		return idSG;
	}
}
