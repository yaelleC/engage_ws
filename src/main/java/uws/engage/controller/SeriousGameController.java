package uws.engage.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * This class allows access to the serious game table of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 14.06
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
		
		// in debug mode = trace
		if (g.DEBUG) { System.out.println("### SeriousGameController.SeriousGameController() ###"); }
		
		Class.forName("com.mysql.jdbc.Driver");				
		conn = DriverManager.getConnection(g.DB_NAME, g.DB_USERNAME, g.DB_PASSWD);
		
		if (g.DEBUG) { System.out.println("### Access to DB ok ###"); }
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
						", "+ g.SG_FIELD_ID_DEVELOPER + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
				thereIsId = 1;
			}
			else 
			{
				sqlQuery = "INSERT INTO "+ g.TABLE_SG + "("+ g.SG_FIELD_VERSION +
						", "+ g.SG_FIELD_ID_TEACHER + ", "+ g.SG_FIELD_NAME + ", "+ g.SG_FIELD_DESC + 
						", "+ g.SG_FIELD_AGEMIN +", "+ g.SG_FIELD_AGEMAX + ", "+ g.SG_FIELD_LANG + ", " + 
						g.SG_FIELD_COUNTRY + ", "+ g.SG_FIELD_PUBLIC + ", "+ g.SG_FIELD_COMMENTS +
						", "+ g.SG_FIELD_ID_DEVELOPER +") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
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
	
	public int saveCF(String configFile, int idSG)
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
							" = ? WHERE " + g.SG_FIELD_ID + " = " + idSG);

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
				sg.put(g.SG_FIELD_PUBLIC, results.getString(8));
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

	
}
