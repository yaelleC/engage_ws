package uws.engage.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.simple.JSONObject;

/**
 * This class allows access to the developer table of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 14.06
 *
 */
public class DeveloperController {
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
	public DeveloperController( ) throws Exception
	{
		g = new General();
		
		// in debug mode = trace
		if (g.DEBUG) { System.out.println("### DeveloperController.DeveloperController() ###"); }
		
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
	
	/**
	 * Creates a developer in the database
	 * @param dev = JSON describing the developer
	 * @return g.CST_RETURN_SUCCESS is the creation was successful, g.CST_RETURN_SQL_ERROR otherwise
	 */
	public int createDeveloper(JSONObject dev)
	{
		if (g.DEBUG)
		{
			System.out.println("*** createDeveloper ***");
		}
		try
		{
			PreparedStatement stCreateDev = 
					conn.prepareStatement("INSERT INTO "+ g.TABLE_DEVELOPER + "("+ 
										g.DEV_FIELD_NAME + ", " + g.DEV_FIELD_SURNAME + ", " +
										g.DEV_FIELD_EMAIL + ", " + g.DEV_FIELD_PASSWORD + ") VALUES (?, ?, ?, ?)");

			stCreateDev.setString(1, dev.get(g.DEV_FIELD_NAME).toString());
			stCreateDev.setString(2, dev.get(g.DEV_FIELD_SURNAME).toString());
			stCreateDev.setString(3, dev.get(g.DEV_FIELD_EMAIL).toString());
			stCreateDev.setString(4, dev.get(g.DEV_FIELD_PASSWORD).toString());
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stCreateDev.toString());
			}
			
			stCreateDev.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (createDeveloper): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	/**
	 * Updates a developer in the database
	 * @param dev = JSON describing the developer
	 * @return g.CST_RETURN_SUCCESS is the update was successful, g.CST_RETURN_SQL_ERROR otherwise
	 */
	public int updateDeveloper(JSONObject dev)
	{
		if (g.DEBUG)
		{
			System.out.println("*** updateDeveloper ***");
		}
		try
		{
			
			PreparedStatement stUpdateDev = 
					conn.prepareStatement("UPDATE "+ g.TABLE_DEVELOPER + " SET " + 
										g.DEV_FIELD_NAME + " = ?, " + g.DEV_FIELD_SURNAME + " = ?, " +
										g.DEV_FIELD_EMAIL + " = ?, " + g.DEV_FIELD_PASSWORD + " = ? WHERE " +
										g.DEV_FIELD_ID + " = ?");

			stUpdateDev.setString(1, dev.get(g.DEV_FIELD_NAME).toString());
			stUpdateDev.setString(2, dev.get(g.DEV_FIELD_SURNAME).toString());
			stUpdateDev.setString(3, dev.get(g.DEV_FIELD_EMAIL).toString());
			stUpdateDev.setString(4, dev.get(g.DEV_FIELD_PASSWORD).toString());
			stUpdateDev.setInt(5, Integer.parseInt(dev.get(g.DEV_FIELD_ID).toString()));
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stUpdateDev.toString());
			}
			
			stUpdateDev.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (updateDeveloper): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	/**
	 * Deletes a developer in the database
	 * @param idDev id of the developer to delete
	 * @return g.CST_RETURN_SUCCESS is the deletion was successful, g.CST_RETURN_SQL_ERROR otherwise
	 */
	public int deleteDeveloper(int idDev)
	{
		if (g.DEBUG)
		{
			System.out.println("*** deleteDeveloper ***");
		}
		try
		{
			
			PreparedStatement stCreateDev = 
					conn.prepareStatement("DELETE FROM  "+ g.TABLE_DEVELOPER + " WHERE " + 
										g.DEV_FIELD_ID + " = ?");

			stCreateDev.setInt(1, idDev);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stCreateDev.toString());
			}
			
			stCreateDev.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (deleteDeveloper): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	/**
	 * Checks if the login data provided are correct
	 * @param email - developer's email
	 * @param password - developer's password
	 * @return a JSON describing the developer if the data is correct, null otherwise
	 */
	public JSONObject checkDeveloperEmailAndPassword(String email, String password)
	{
		if (g.DEBUG)
		{
			System.out.println("*** checkDeveloperEmailAndPassword ***");
		}
		try
		{
			PreparedStatement stGetDeveloper = 
					conn.prepareStatement("SELECT "+ g.DEV_FIELD_ID + ", " + g.DEV_FIELD_NAME + ", "+ g.DEV_FIELD_SURNAME +
											" FROM " + g.TABLE_DEVELOPER + 
											" WHERE " + g.DEV_FIELD_EMAIL + " = ? AND " + g.DEV_FIELD_PASSWORD + " = ?");

			stGetDeveloper.setString(1, email);
			stGetDeveloper.setString(2, password);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetDeveloper.toString());
			}
			
			ResultSet results = stGetDeveloper.executeQuery();
			
			if (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject developer = new JSONObject();
				developer.put(g.DEV_FIELD_ID, results.getInt(1));
				developer.put(g.DEV_FIELD_NAME, results.getString(2));
				developer.put(g.DEV_FIELD_SURNAME, results.getString(3));
				developer.put(g.DEV_FIELD_EMAIL, email);
				developer.put(g.DEV_FIELD_PASSWORD, password);
				
				return developer;
			}
			return null;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (Get Developer): " + e.getMessage());
			return null;
		}
	}

	
	public int addSGtoDeveloper(int idSG, int idDeveloper)
	{
		if (g.DEBUG)
		{
			System.out.println("*** addSGtoDeveloper ***");
		}
		try
		{
			PreparedStatement stAddSGtoDev = 
					conn.prepareStatement("INSERT INTO "+ g.TABLE_SG_DEVELOPER +
								"("+ g.SGDEV_FIELD_ID_DEV + ", "+ g.SGDEV_FIELD_ID_SG + ") VALUES ( ?, ?)");

			stAddSGtoDev.setInt(1, idSG);
			stAddSGtoDev.setInt(2, idDeveloper);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stAddSGtoDev.toString());
			}
			
			stAddSGtoDev.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (Add dev): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
}
