package uws.engage.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.simple.JSONObject;

/**
 * This class allows access to the school table of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 14.06
 *
 */
public class SchoolController {
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
	public SchoolController( ) throws Exception
	{
		g = new General();
		
		// in debug mode = trace
		if (g.DEBUG) { System.out.println("### SchoolController.SchoolController() ###"); }
		
		conn = DataSource.getInstance().getConnection();
		
		if (g.DEBUG) { System.out.println("### Access to DB ok ###"); }
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
	
	public int createSchool(JSONObject school)
	{
		if (g.DEBUG)
		{
			System.out.println("*** createSchool ***");
		}
		try
		{
			PreparedStatement stCreateSchool = 
					conn.prepareStatement("INSERT INTO "+ g.TABLE_SCHOOL + "("+ 
										g.S_FIELD_NAME + ", " + g.S_FIELD_COUNTRY + ", " +
										g.S_FIELD_CITY +") VALUES (?, ?, ?)");

			stCreateSchool.setString(1, school.get(g.S_FIELD_NAME).toString());
			stCreateSchool.setString(2, school.get(g.S_FIELD_COUNTRY).toString());
			stCreateSchool.setString(3, school.get(g.S_FIELD_CITY).toString());
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stCreateSchool.toString());
			}
			
			stCreateSchool.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (createSchool): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	public int updateSchool(JSONObject school)
	{
		if (g.DEBUG)
		{
			System.out.println("*** updateSchool ***");
		}
		try
		{
			
			PreparedStatement stUpdateSchool = 
					conn.prepareStatement("UPDATE  "+ g.TABLE_SCHOOL + " SET " + 
										g.S_FIELD_NAME + " = ?, " + g.S_FIELD_COUNTRY + " = ?, " +
										g.S_FIELD_CITY + " = ? WHERE " + g.S_FIELD_ID + " = ?");

			stUpdateSchool.setString(1, school.get(g.S_FIELD_NAME).toString());
			stUpdateSchool.setString(2, school.get(g.S_FIELD_COUNTRY).toString());
			stUpdateSchool.setString(3, school.get(g.S_FIELD_CITY).toString());
			stUpdateSchool.setInt(4, Integer.parseInt(school.get(g.S_FIELD_ID).toString()));
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stUpdateSchool.toString());
			}
			
			stUpdateSchool.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (updateSchool): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	public int deleteSchool(int idSchool)
	{
		if (g.DEBUG)
		{
			System.out.println("*** deleteSchool ***");
		}
		try
		{
			
			PreparedStatement stDeleteSchool = 
					conn.prepareStatement("DELETE FROM  "+ g.TABLE_SCHOOL + " WHERE " + 
										g.S_FIELD_ID + " = ?");

			stDeleteSchool.setInt(1, idSchool);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stDeleteSchool.toString());
			}
			
			stDeleteSchool.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (deleteSchool): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	public JSONObject getSchoolById(int idSchool)
	{
		if (g.DEBUG)
		{
			System.out.println("*** getSchoolById ***");
		}
		try
		{
			
			PreparedStatement stGetSchool = 
					conn.prepareStatement("SELECT " + g.S_FIELD_ID + ", " + g.S_FIELD_NAME + ", " + 
											g.S_FIELD_COUNTRY + ", " + g.S_FIELD_CITY + 
											" FROM  "+ g.TABLE_SCHOOL + " WHERE " + 
										g.S_FIELD_ID + " = ?");

			stGetSchool.setInt(1, idSchool);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetSchool.toString());
			}
			
			ResultSet results = stGetSchool.executeQuery();
			
			if (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject school = new JSONObject();
				school.put(g.S_FIELD_ID, results.getInt(1));
				school.put(g.S_FIELD_NAME, results.getString(2));
				school.put(g.S_FIELD_COUNTRY, results.getString(3));
				school.put(g.S_FIELD_CITY, results.getString(4));
				
				return school;
				
			}
			else
			{
				return new JSONObject();
			}
			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getSchoolById): " + e.getMessage());
			return null;
		}
	}
	
	public int giveSGaccessToSchool(int idSG, int idSchool)
	{
		if (g.DEBUG)
		{
			System.out.println("*** giveSGaccessToSchool ***");
		}
		try
		{
			
			PreparedStatement stGiveAccessToSchool = 
					conn.prepareStatement("INSERT INTO  "+ g.TABLE_SG_SCHOOL + "(" + g.SGS_FIELD_ID_SG + ", " + 
							g.SGS_FIELD_ID_SCHOOL + ") VALUES (?, ?)");

			stGiveAccessToSchool.setInt(1, idSG);
			stGiveAccessToSchool.setInt(2, idSchool);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGiveAccessToSchool.toString());
			}
			
			stGiveAccessToSchool.executeUpdate();
			
			return g.CST_RETURN_SUCCESS;
			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (giveSGaccessToSchool): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
}
