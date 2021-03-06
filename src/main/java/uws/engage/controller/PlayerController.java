package uws.engage.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.json.simple.JSONObject;

/**
 * This class allows access to the player tables of the database, it is able to create a new player table
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class PlayerController {
	
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
	public PlayerController( ) throws Exception
	{
		g = new General();
		conn = DataSource.getInstance().getConnection();
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
	 * Creates a new table player_<idSG>_<version> with columns : id - <param1> - ... - <paramN>
	 * @param player JSON characteristics of the player
	 * @param idSG id of the SG played by the player
	 * @return CST_RETURN_SUCCESS or CST_RETURN_SQL_ERROR 
	 */
	public int createTablePlayer(ArrayList<JSONObject>  player, int idSG, int version)
	{
		if (g.DEBUG)
		{
			System.out.println("*** CreateTablePlayer for SG of id "+ idSG +" - from JSON ***");
		}
		try
		{
			String sqlQuery = "CREATE TABLE "+ g.TABLE_PLAYER + idSG + "_" + version +
									"("+ g.P_FIELD_ID + " INT NOT NULL AUTO_INCREMENT, " + 
										g.P_FIELD_ID_STUDENT + " INT " ;
			
			for (JSONObject characteristic: player) {
				
				String typeInDB = "";
				
				String type = characteristic.get("type").toString().trim();
				
				if (type.trim().startsWith("Enum"))
				{
					String[] values = type.split("\\(")[1].split("\\)")[0].split(",");
					typeInDB = "ENUM(";

					for (int i = 0; i<values.length ; i++) {
						if (i != 0) { typeInDB += ", "; }
						typeInDB += "'" + values[i].trim() + "'";
					}
					
					typeInDB += ")";
				}
				else
				{
					switch (type) {
					case "Int":
						typeInDB = "INT";
						break;
					case "Float":
						typeInDB = "FLOAT";
						break;
					case "Bool":
						typeInDB = "BOOLEAN";
						break;
					case "Char":
						typeInDB = "CHAR";
						break;
					case "Text":
						typeInDB = "TEXT";
						break;
					default:
						typeInDB = "VARCHAR(200)";
						break;
					}
				}
				
				sqlQuery += ", " + characteristic.get("name") + " " + typeInDB;
			}

			
			sqlQuery += ", PRIMARY KEY ("+g.P_FIELD_ID+") )";
			
			System.out.println(sqlQuery);
			
			PreparedStatement stCreateTable = conn.prepareStatement(sqlQuery);			
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stCreateTable.toString());
			}
			
			stCreateTable.executeUpdate();
			
			return g.CST_RETURN_SUCCESS;
			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR : " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	public int createPlayer(int idSG, int version, int idStudent, ArrayList<JSONObject> params)
	{
		if (g.DEBUG)
		{
			System.out.println("*** createPlayer ***");
		}
		try
		{
			String sql = "INSERT INTO "+ g.TABLE_PLAYER + idSG + "_" + version + "("+ g.P_FIELD_ID_STUDENT;
			
			for (JSONObject characteristic: params) {	
				sql += ", " + characteristic.get("name") ;
			}
			
			sql += ") VALUES (?";
			
			for (int i=0 ; i<params.size() ; i++)
			{
				sql += ", ?";
			}
			
			sql += ")";

			PreparedStatement stCreatePlayer = conn.prepareStatement(sql);
			stCreatePlayer.setInt(1, idStudent);
			
			for (int i=0 ; i<params.size() ; i++) {	
				JSONObject characteristic = params.get(i);
				String type = characteristic.get("type").toString();
				String value = characteristic.get("value").toString();

				// checking type of param, and adding to player table
				switch (type) {
					case "Int":
						try {
							int intValue = Integer.parseInt(value);
							stCreatePlayer.setInt(i+2, intValue);
						}
						catch(Exception e)
						{
							stCreatePlayer.setNull(i+2, java.sql.Types.INTEGER); 
						}
						break;
					case "Float":
						try {
							float floatValue = Float.parseFloat(value);
							stCreatePlayer.setFloat(i+2, floatValue);
						}
						catch(Exception e)
						{
							stCreatePlayer.setNull(i+2, java.sql.Types.FLOAT); 
						}
						break;
					case "Bool":
						try {
							Boolean boolValue = Boolean.parseBoolean(value);
							stCreatePlayer.setBoolean(i+2, boolValue);
						}
						catch(Exception e)
						{
							stCreatePlayer.setNull(i+2, java.sql.Types.BOOLEAN); 
						}
						break;
					case "Char":
						try {
							if (value.length() > 1) { value = value.substring(0, 1);}
							if (value == "") { value = null; }
							stCreatePlayer.setString(i+2, value);
						}
						catch(Exception e)
						{
							stCreatePlayer.setNull(i+2, java.sql.Types.CHAR); 
						}
						break;
					default:
						stCreatePlayer.setString(i+2, value);
						break;
				}		
			}
					
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stCreatePlayer.toString());
			}
			
			stCreatePlayer.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return getPlayerFromIdStudent(idStudent, idSG, version);
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (createPlayer): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	/*
	public int updatePlayer(int idSG, int version, int idPlayer, ArrayList<JSONObject> params)
	{
		if (g.DEBUG)
		{
			System.out.println("*** updatePlayer ***");
		}
		try
		{
			String sql = "UPDATE "+ g.TABLE_PLAYER + idSG + "_" + version + " SET " ;
			
			int cpt = 0;
			for (JSONObject characteristic: params) {	
				if (cpt > 0)
				{
					sql += " , ";
				}
				sql += characteristic.get("name") + " = ? " ;
				cpt ++;
			}
			
			sql += " WHERE "+ g.P_FIELD_ID + " = ?" ;

			PreparedStatement stUpdatePlayer = conn.prepareStatement(sql);
			
			for (int i=0 ; i<params.size() ; i++) {	
				JSONObject characteristic = params.get(i);
				String type = characteristic.get("type").toString();
				String value = characteristic.get("value").toString();

				// checking type of param, and adding to player table
				switch (type) {
					case "Int":
						try {
							int intValue = Integer.parseInt(value);
							stUpdatePlayer.setInt(i+1, intValue);
						}
						catch(Exception e)
						{
							stUpdatePlayer.setNull(i+1, java.sql.Types.INTEGER); 
						}
						break;
					case "Float":
						try {
							float floatValue = Float.parseFloat(value);
							stUpdatePlayer.setFloat(i+1, floatValue);
						}
						catch(Exception e)
						{
							stUpdatePlayer.setNull(i+1, java.sql.Types.FLOAT); 
						}
						break;
					case "Bool":
						try {
							Boolean boolValue = Boolean.parseBoolean(value);
							stUpdatePlayer.setBoolean(i+1, boolValue);
						}
						catch(Exception e)
						{
							stUpdatePlayer.setNull(i+1, java.sql.Types.BOOLEAN); 
						}
						break;
					case "Char":
						try {
							if (value.length() > 1) { value = value.substring(0, 1);}
							if (value == "") { value = null; }
							stUpdatePlayer.setString(i+1, value);
						}
						catch(Exception e)
						{
							stUpdatePlayer.setNull(i+1, java.sql.Types.CHAR); 
						}
						break;
					default:
						stUpdatePlayer.setString(i+1, value);
						break;
				}		
			}

			stUpdatePlayer.setInt(params.size()+1, idPlayer);
					
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stUpdatePlayer.toString());
			}
			
			stUpdatePlayer.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return getPlayerFromIdStudent(idStudent, idSG, version);
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (updatePlayer): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
*/

	/**
	 * get the player from its id 
	 * @param idPlayer
	 * @param idSG
	 * @param version
	 * @return the player if there is one
	 */
	public JSONObject getPlayerFromId(int idPlayer, int idSG, int version) throws Exception
	{
		PreparedStatement stGetStudent = 
				conn.prepareStatement("SELECT * FROM " + g.TABLE_PLAYER + idSG + "_" + version + 
										" WHERE "+ g.P_FIELD_ID +" = ?" );

		stGetStudent.setInt(1, idPlayer);
		
		ResultSet results = stGetStudent.executeQuery();

		ResultSetMetaData rsmd = results.getMetaData();
		int columnCount = rsmd.getColumnCount();
		ArrayList<String> columnNames = new ArrayList<String>();
		
		if (results.next())
		{
			JSONObject player = new JSONObject();

			player.put(g.P_FIELD_ID, results.getInt(1));
			player.put(g.P_FIELD_ID_STUDENT, results.getInt(2));

			for (int i = 3; i < columnCount + 1; i++ ) {
			  String name = rsmd.getColumnName(i);
			  player.put(name, results.getString(i));
			}
			return player;
		}
		else
		{
			return null;
		}
	}

	/**
	 * get the id of the player associated to the student for an SG and a version
	 * @param idStudent
	 * @param idSG
	 * @param version
	 * @return the id of the player if there is one, 0 if there is no player associated, -1 in case of error
	 */
	public int getPlayerFromIdStudent(int idStudent, int idSG, int version) throws Exception
	{
		PreparedStatement stGetStudentParams = 
				conn.prepareStatement("SELECT "+ g.P_FIELD_ID +" FROM " + g.TABLE_PLAYER + idSG + "_" + version + 
										" WHERE "+ g.P_FIELD_ID_STUDENT +" = ?" );

		stGetStudentParams.setInt(1, idStudent);
		
		ResultSet results = stGetStudentParams.executeQuery();
		int idPlayer = 0;

		// if idStudent is '0', there might be more than one player, return the last one.
		while (results.next())
		{
			idPlayer = results.getInt(1);
		}
		return idPlayer;
	}

	public ArrayList<JSONObject> getParametersRequired(int idStudent, int idSG, int version) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getParametersRequired ***");
		}
		
		ArrayList<JSONObject> params = new ArrayList<JSONObject>();
		
		if (getPlayerFromIdStudent(idStudent, idSG, version) > 0)
		{
			return params;
		}
		
		// else

	
		PreparedStatement stGetParams = 
				conn.prepareStatement("SELECT column_name, data_type, column_type"+ 
										" FROM information_schema.columns WHERE table_name = ?" );

		stGetParams.setString(1, g.TABLE_PLAYER + idSG + "_" + version);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stGetParams.toString());
		}
		
		ResultSet results = stGetParams.executeQuery();
		
		while (results.next())
		{
			if (g.DEBUG_SQL)
			{
				System.out.println();
			}
			
			if (!results.getString(1).equals(g.P_FIELD_ID) && !results.getString(1).equals(g.P_FIELD_ID_STUDENT))
			{
				JSONObject param = new JSONObject();
				param.put("name", results.getString(1));
				
				String type;
				switch (results.getString(2).toUpperCase()) {
				case "INT":
					type = "Int";
					break;
				case "FLOAT":
					type = "Float";
					break;
				case "BOOLEAN":
					type = "Bool";
					break;
				case "CHAR":
					type = "Char";
					break;
				case "TEXT":
					type = "Text";
					break;
				case "VARCHAR":
					type = "String";
					break;
				default:
					type = results.getString(2);
					break;
				}
				
				param.put("type", type);
				
				params.add(param);
			}
		}
		
		if (g.DEBUG_SQL)
		{
			System.out.println(params.toString());
		}
		return params;			
	}

	public ArrayList<JSONObject> getParametersRequired(int idSG, int version) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getParametersRequired ***");
		}
		
		ArrayList<JSONObject> params = new ArrayList<JSONObject>();
		
		
		PreparedStatement stGetParams = 
				conn.prepareStatement("SELECT column_name, data_type, column_type"+ 
										" FROM information_schema.columns WHERE table_name = ?" );

		stGetParams.setString(1, g.TABLE_PLAYER + idSG + "_" + version);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stGetParams.toString());
		}
		
		ResultSet results = stGetParams.executeQuery();
		
		while (results.next())
		{
			if (g.DEBUG_SQL)
			{
				System.out.println();
			}
			
			if (!results.getString(1).equals(g.P_FIELD_ID) && !results.getString(1).equals(g.P_FIELD_ID_STUDENT))
			{
				JSONObject param = new JSONObject();
				param.put("name", results.getString(1));
				
				String type;
				switch (results.getString(2).toUpperCase()) {
				case "INT":
					type = "Int";
					break;
				case "FLOAT":
					type = "Float";
					break;
				case "BOOLEAN":
					type = "Bool";
					break;
				case "CHAR":
					type = "Char";
					break;
				case "TEXT":
					type = "Text";
					break;
				case "VARCHAR":
					type = "String";
					break;
				default:
					type = results.getString(2);
					break;
				}
				
				param.put("type", type);
				
				params.add(param);
			}
		}
		
		if (g.DEBUG_SQL)
		{
			System.out.println(params.toString());
		}
		return params;			
	}
}

