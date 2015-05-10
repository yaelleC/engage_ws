package uws.engage.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.json.simple.JSONObject;


/**
 * This class allows access to the table feedback of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 0.1
 *
 */
public class FeedbackTriggerController {

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
	public FeedbackTriggerController( ) throws Exception
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
			
	public int addFeedbackTriggerInactivity ( int idSG, JSONObject trigger, int version )
	{
		if (g.DEBUG)
		{
			System.out.println("*** addFeedbackTriggerInactivity ***");
		}
		try
		{
			String sqlQueryIdFeedback = "SELECT "+ g.F_FIELD_ID +" FROM "+ g.TABLE_FEEDBACK +
					" WHERE "+ g.F_FIELD_NAME +" = ? AND " + g.F_FIELD_ID_SG + " = ? AND " + g.F_FIELD_VERSION_SG + " = ?" ;
			
			PreparedStatement stInsertTrigger = 
				conn.prepareStatement("INSERT INTO "+ g.TABLE_FEEDBACK_TRIGGER + "("+ g.FT_FIELD_ID_SG + 
										", " + g.FT_FIELD_ID_FDBK + ", " + g.FT_FIELD_SG_VERSION + ", " + g.FT_FIELD_INACTIVITY + 
										", " + g.FT_FIELD_LIMIT + ", " + g.FT_FIELD_INFERIOR + ", " + g.FT_FIELD_REPEAT +
										") VALUES (?, ("+ sqlQueryIdFeedback +"), ?, ?, ?, ?, ?); ");

			stInsertTrigger.setInt(1, idSG);
			ArrayList<String> feedback = (ArrayList<String>) trigger.get("feedback");
			stInsertTrigger.setString(2, (String) feedback.get(0) );
			stInsertTrigger.setInt(3, idSG);
			stInsertTrigger.setInt(4, version);
			stInsertTrigger.setInt(5, version);
			stInsertTrigger.setBoolean(6, true);
			stInsertTrigger.setInt(7, Integer.parseInt(trigger.get("limit").toString()));
			Boolean inferior = trigger.get("sign").toString().equals("<");
			stInsertTrigger.setBoolean(8, inferior);
			stInsertTrigger.setBoolean(9, trigger.containsKey("repeat"));
		
			if (g.DEBUG_SQL)
			{
				System.out.println(stInsertTrigger.toString());
			}
			
			stInsertTrigger.executeUpdate();
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR : " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
		
	public int addFeedbackTriggerOutcome ( int idSG, int idLO, JSONObject trigger, int version)
	{
		if (g.DEBUG)
		{
			System.out.println("*** addFeedbackTriggerOutcome ***");
		}
		try
		{

			ArrayList<JSONObject> feedback = (ArrayList<JSONObject>) trigger.get("feedback");

			for (JSONObject f : feedback) {
						
				String sqlQueryIdFeedback = "SELECT "+ g.F_FIELD_ID +" FROM "+ g.TABLE_FEEDBACK +
						" WHERE "+ g.F_FIELD_NAME +" = ? AND " + g.F_FIELD_ID_SG + " = ? AND " + g.F_FIELD_VERSION_SG + " = ?" ;
				

				PreparedStatement stInsertTrigger = 
					conn.prepareStatement("INSERT INTO "+ g.TABLE_FEEDBACK_TRIGGER + "("+ g.FT_FIELD_ID_SG + 
											", " + g.FT_FIELD_ID_FDBK + ", " + g.FT_FIELD_SG_VERSION + 
											", " + g.FT_FIELD_ID_OUTCOME + ", " + g.FT_FIELD_INACTIVITY + 
											", " + g.FT_FIELD_LIMIT + ", " + g.FT_FIELD_INFERIOR + 
											", " + g.FT_FIELD_REPEAT + ", " + g.FT_FIELD_IMMEDIATE +
											") VALUES (?, ("+ sqlQueryIdFeedback +"), ?, ?, ?, ?, ?, ?, ?); ");

				stInsertTrigger.setInt(1, idSG);
				stInsertTrigger.setString(2, f.get("name").toString() );
				stInsertTrigger.setInt(3, idSG);
				stInsertTrigger.setInt(4, version);
				stInsertTrigger.setInt(5, version);
				stInsertTrigger.setInt(6, idLO);
				stInsertTrigger.setBoolean(7, false);
				stInsertTrigger.setInt(8, Integer.parseInt(trigger.get("limit").toString()));
				Boolean inferior = trigger.get("sign").toString().equals("<");
				stInsertTrigger.setBoolean(9, inferior);
				stInsertTrigger.setBoolean(10, trigger.containsKey("repeat"));
				stInsertTrigger.setBoolean(11, (Boolean) f.get("immediate"));
			
				if (g.DEBUG_SQL)
				{
					System.out.println(stInsertTrigger.toString());
				}
				
				stInsertTrigger.executeUpdate();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR : " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}

	public ArrayList<JSONObject> getFeedback(int idGamePlay ) throws Exception
	{
		// Feedback to be returned
		ArrayList<JSONObject> feedback = new ArrayList<JSONObject>();
						
		if (g.DEBUG)
		{
			System.out.println("********* getFeedback **************");
		}

		// To log feedback
		FeedbackLogController fdbkLogController = new FeedbackLogController();
		
		// To get feedback data
		FeedbackController feedbackController = new FeedbackController();
		
		// Get SG's details
		GamePlayController gpController = new GamePlayController();
		JSONObject gameplay = gpController.getGamePlay(idGamePlay);

		int idSG = Integer.parseInt(gameplay.get(g.GP_FIELD_ID_SG).toString());
		int version = Integer.parseInt(gameplay.get(g.GP_FIELD_VERSION).toString());
				
		// "2014-11-22 21:04:09.0"
        String target = gameplay.get(g.GP_FIELD_LASTACTION).toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
        Date lastAction =  df.parse(target); 

		if (g.DEBUG)
		{
			System.out.println("lastAction : " + lastAction);
		}
		
		// check table feedback_trigger for inactivity and goal related feedback triggers
		PreparedStatement stGetFeedback = 
				conn.prepareStatement("SELECT "+ g.FT_FIELD_ID_FDBK + ", "+ g.FT_FIELD_ID_OUTCOME +
										", "+ g.FT_FIELD_INACTIVITY + ", "+ g.FT_FIELD_LIMIT + ", " +
										g.FT_FIELD_INFERIOR + ", "+ g.FT_FIELD_IMMEDIATE +
										" FROM " + g.TABLE_FEEDBACK_TRIGGER +
										" WHERE " + g.FT_FIELD_ID_SG + " = ? AND "+ g.FT_FIELD_SG_VERSION + " = ? AND (" +
										g.FT_FIELD_REPEAT + " OR " + g.FT_FIELD_ID_FDBK + 
										" NOT IN (SELECT " + g.LOG_F_FIELD_ID_FEEDBACK + 
										" FROM " + g.TABLE_LOG_GP_FEEDBACK + 
										" WHERE " + g.LOG_F_FIELD_ID_GP + " = ?))");

		stGetFeedback.setInt(1, idSG);
		stGetFeedback.setInt(2, version);
		stGetFeedback.setInt(3, idGamePlay);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stGetFeedback.toString());
		}
		
		ResultSet results = stGetFeedback.executeQuery();
		
		// if there are feedback described
		while (results.next())
		{
			if (g.DEBUG)
			{
				System.out.println("feedback found");
			}
			
			int idFeedback = results.getInt(1);
			int idOutcome = results.getInt(2);
			Boolean inactivity = results.getBoolean(3);
			int limit = results.getInt(4);
			Boolean inferior = results.getBoolean(5);
			Boolean immediate = results.getBoolean(6);
			
			// inactivity feedback
			if (inactivity)
			{
				if (g.DEBUG)
				{
					System.out.println("inactivity feedback found");
				}
				Date now = new Date();
				long difference = (now.getTime() - lastAction.getTime())/1000;
											
				if (difference > limit)
				{						
					JSONObject f = feedbackController.getFeedbackById(idFeedback);
					
					String message = f.get(g.F_FIELD_MESSAGE).toString();
					message = message.replace("[inactivity]", difference+"");
					
					f.remove(g.F_FIELD_MESSAGE);
					f.put(g.F_FIELD_MESSAGE, message);
											
					feedback.add(f);
					
					fdbkLogController.logFeedback(f, idGamePlay);
				}
				
			}
			// goal related feedback
			else
			{
				if (g.DEBUG)
				{
					System.out.println("outcome feedback found");
				}
				PreparedStatement stGetValueOfOutcome =
						conn.prepareStatement("SELECT " + g.G_O_FIELD_VALUE + " FROM " +
										g.TABLE_GAMEPLAY_OUTCOME + 
										" WHERE " + g.G_O_FIELD_ID_GP + " = ? AND " + g.G_O_FIELD_ID_O + " = ?");

				stGetValueOfOutcome.setInt(1, idGamePlay);
				stGetValueOfOutcome.setInt(2, idOutcome);
				
				ResultSet res = stGetValueOfOutcome.executeQuery();
				
				if (res.next())
				{
					int value = res.getInt(1);
					
					// TODO : deal with value > limit case (new column in DB plus boolean check)
					if ((value < limit && inferior) || (value > limit && !inferior))
					{
						JSONObject f = feedbackController.getFeedbackById(idFeedback);
						String message = f.get(g.F_FIELD_MESSAGE).toString();
						
						if (f.get(g.F_FIELD_MESSAGE).toString().contains("[outcome]"))
						{				
							LearningOutcomeController loC = new LearningOutcomeController();
							JSONObject o = loC.getOutcomeById(idOutcome);
							
							message = message.replace("[outcome]", o.get(g.O_FIELD_NAME)+" (" + o.get(g.O_FIELD_DESC) + ")");
						}
						
						message = message.replace("[value]", value + "");
						
						f.remove(g.F_FIELD_MESSAGE);
						f.put(g.F_FIELD_MESSAGE, message);
						feedback.add(f);
						
						fdbkLogController.logFeedback(f, idGamePlay);
					}
				}
			}				
		}

		fdbkLogController.finalize();
		feedbackController.finalize();
		gpController.finalize();
		if (g.DEBUG)
		{
			System.out.println(feedback);
		}
		return feedback;			
	}
}
