package uws.engage.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONObject;



/**
 * This class allows access to the table feedback of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 0.1
 *
 */
public class FeedbackController {

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
	 * Creates a feedback controller => gives access to the useful CRUD actions on table Feedback
	 * @throws Exception
	 */
	public FeedbackController( ) throws Exception
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
	 * Creates a new feedback in the database
	 * @param feedback JSON of the feedback
	 * @param idSG id of the game this feedback is linked to
	 * @return CST_RETURN_WRONG_ID if the idSG is invalid, CST_RETURN_SQL_ERROR if the query didn't work
	 */
	public int createFeedback(String name, JSONObject feedback, int idSG, int version)
	{
		if (g.DEBUG)
		{
			System.out.println("*** CreateFeedback - from JSON ***");
		}
		try
		{
			PreparedStatement stGetSGAssociated = 
					conn.prepareStatement("SELECT * FROM " + g.TABLE_SG + " WHERE " + 
											g.SG_FIELD_ID + " = ? AND " + g.SG_FIELD_VERSION + " = ?");

			stGetSGAssociated.setInt(1, idSG);
			stGetSGAssociated.setInt(2, version);
			
			ResultSet res = stGetSGAssociated.executeQuery();
			
			if (res.first())
			{
				PreparedStatement stInsertFeedback = 
					conn.prepareStatement("INSERT INTO "+ g.TABLE_FEEDBACK + "("+ g.F_FIELD_NAME + ", "+ g.F_FIELD_MESSAGE + 
								", "+ g.F_FIELD_TYPE + ", "+ g.F_FIELD_FINAL + ", "+ g.F_FIELD_WIN + ", " + g.F_FIELD_ID_SG + ", " +
								g.F_FIELD_VERSION_SG + ") VALUES (?, ?, ?, ?, ?, ?, ? )");

				stInsertFeedback.setString(1, name);
				stInsertFeedback.setString(2, (String) feedback.get("message"));
				if (!feedback.containsKey("type")){ stInsertFeedback.setNull(3, java.sql.Types.NULL); } 
					else { stInsertFeedback.setString(3, feedback.get("type").toString().toUpperCase()); }

				stInsertFeedback.setBoolean(4, feedback.containsKey("final"));
				if (feedback.containsKey("final"))
				{
					if (feedback.get("final").toString().equals("win")) {stInsertFeedback.setBoolean(5, true );}
					else if (feedback.get("final").toString().equals("lose")) {stInsertFeedback.setBoolean(5, false );}
					else  {stInsertFeedback.setNull(5, Types.NULL);}
				}
				else  {
						stInsertFeedback.setNull(5, Types.NULL);
				}
				stInsertFeedback.setInt(6, idSG);
				stInsertFeedback.setInt(7, version);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stInsertFeedback.toString());
				}
				
				stInsertFeedback.executeUpdate();
				
				return g.CST_RETURN_SUCCESS;
			}
			else
			{
				return g.CST_RETURN_WRONG_ID;
			}
		}
		catch (Exception e)
		{		
			System.out.println(e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	public JSONObject getFeedbackByName(String name, int idSG, int version)
	{
		if (g.DEBUG)
		{
			System.out.println("*** getFeedbackByName ***");
		}
		try
		{
			PreparedStatement stGetFeedback = 
					conn.prepareStatement("SELECT "+ g.F_FIELD_ID + ", " + g.F_FIELD_MESSAGE + ", "+ g.F_FIELD_TYPE +
											", " + g.F_FIELD_FINAL + ", " + g.F_FIELD_WIN +
											" FROM " + g.TABLE_FEEDBACK +
											" WHERE " + g.F_FIELD_NAME + " = ? AND " + g.F_FIELD_ID_SG + " = ? AND " 
											+ g.F_FIELD_VERSION_SG + " = ? ");
			
			stGetFeedback.setString(1, name);
			stGetFeedback.setInt(2, idSG);
			stGetFeedback.setInt(3, version);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetFeedback.toString());
			}
			
			ResultSet results = stGetFeedback.executeQuery();
			
			if (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject feedback = new JSONObject();
				feedback.put(g.F_FIELD_ID, results.getInt(1));
				feedback.put(g.F_FIELD_NAME, name);
				feedback.put(g.F_FIELD_MESSAGE, results.getString(2));
				feedback.put(g.F_FIELD_TYPE, results.getString(3));
				if (results.getBoolean(4))
				{
					String finalString = (results.getString(5) != null)? ( (results.getBoolean(5))? "win" : "lose") : "true";
					feedback.put(g.F_FIELD_FINAL, finalString);
				}
				return feedback;
			}
			return null;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getFeedbackByName): " + e.getMessage());
			return null;
		}
	}
	
	public JSONObject getFeedbackById(int idFeedback)
	{
		if (g.DEBUG)
		{
			System.out.println("*** getFeedbackById ***");
		}
		try
		{
			PreparedStatement stGetFeedback = 
					conn.prepareStatement("SELECT "+ g.F_FIELD_MESSAGE + ", "+ g.F_FIELD_TYPE + ", " + g.F_FIELD_NAME + 
											", " + g.F_FIELD_FINAL + ", " + g.F_FIELD_WIN +
											" FROM " + g.TABLE_FEEDBACK +
											" WHERE " + g.F_FIELD_ID + " = ? ");
			
			stGetFeedback.setInt(1, idFeedback);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetFeedback.toString());
			}
			
			ResultSet results = stGetFeedback.executeQuery();
			
			if (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject feedback = new JSONObject();
				feedback.put(g.F_FIELD_ID, idFeedback);
				feedback.put(g.F_FIELD_MESSAGE, results.getString(1));
				feedback.put(g.F_FIELD_TYPE, results.getString(2));
				feedback.put(g.F_FIELD_NAME, results.getString(3));
				if (results.getString(4) != null)
				{
					String finalString = (results.getString(5) != null)? ( (results.getBoolean(5))? "win" : "lose") : "true";
					feedback.put(g.F_FIELD_FINAL, finalString);
				}
				
				return feedback;
			}
			return null;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getFeedbackById): " + e.getMessage());
			return null;
		}
	}
}
