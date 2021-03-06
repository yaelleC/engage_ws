package uws.engage.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.json.simple.JSONObject;

/**
 * This class allows access to the gameplay tables of the database, it is able to create a new player table
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class GamePlayController {
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
	public GamePlayController( ) throws Exception
	{
		g = new General();

		if (g.DEBUG)
		{
			System.out.println("*** create GamePlayController ***");
		}
		
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

	public int startGamePlay(int idPlayer, int idSG, int version) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** startGamePlay ***");
		}
		PreparedStatement stStartGamePlay = 
				conn.prepareStatement("INSERT INTO "+ g.TABLE_GAMEPLAY + "("+ 
									g.GP_FIELD_ID_SG + ", " + g.GP_FIELD_VERSION + ", " +
									g.GP_FIELD_LASTACTION + ", " + g.GP_FIELD_ID_PLAYER + ") VALUES (?, ?, ?, ?)");

		stStartGamePlay.setInt(1, idSG);
		stStartGamePlay.setInt(2, version);
		
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		
		stStartGamePlay.setTimestamp(3, currentTimestamp);
		stStartGamePlay.setInt(4, idPlayer);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stStartGamePlay.toString());
		}
		
		stStartGamePlay.executeUpdate();
		
		PreparedStatement stGetLastId = conn.prepareStatement("SELECT @@IDENTITY");
		ResultSet resId = stGetLastId.executeQuery();
		int idGamePlay;
		
		if (resId.next())
		{
			idGamePlay = resId.getInt(1);
		}
		else 
		{
			return g.CST_RETURN_SQL_ERROR;
		}
		
		
		LearningOutcomeController outcomeController = new LearningOutcomeController();					
		ArrayList<JSONObject> outcomes = outcomeController.getOutcomeListByGame(idSG, version);
		
		// Learning outcomes values initialised for this particular gameplay, all set to their startingValue
		for (int i = 0 ; i < outcomes.size() ; i++)
		{
			JSONObject outcome = outcomes.get(i);
			
			PreparedStatement stCreateValueOutcome = 
					conn.prepareStatement("INSERT INTO "+ g.TABLE_GAMEPLAY_OUTCOME + "("+ g.G_O_FIELD_ID_GP + 
											", " + g.G_O_FIELD_ID_O + ", " + g.G_O_FIELD_VALUE +
												") VALUES (?, ?, ?)");

			stCreateValueOutcome.setInt(1, idGamePlay);
			stCreateValueOutcome.setInt(2, Integer.parseInt(outcome.get("id").toString()));
			stCreateValueOutcome.setFloat(3, Float.parseFloat(outcome.get("startingValue").toString()));
			
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stCreateValueOutcome.toString());
			}
			stCreateValueOutcome.executeUpdate();
		}
		
		if (g.DEBUG)
		{
			System.out.println("SUCCESS");
			System.out.println();
		}
		
		outcomeController.finalize();
		return idGamePlay;
	}
	
	public ArrayList<JSONObject> getScores(int idGamePlay) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getScores ***");
		}
		PreparedStatement stGetOutcomes = 
				conn.prepareStatement(
					"SELECT "+ g.O_FIELD_NAME + ", "+ g.O_FIELD_DESC + ", "+ g.O_FIELD_TYPE + ", "+ g.O_FIELD_VALUE + 
					", "+ g.G_O_FIELD_ID_O + ", " + g.G_O_FIELD_VALUE +
					" FROM " + g.TABLE_OUTCOME +
					" JOIN " +  g.TABLE_GAMEPLAY_OUTCOME +
					" ON " + g.TABLE_GAMEPLAY_OUTCOME  + "." + g.G_O_FIELD_ID_O + " = " + g.TABLE_OUTCOME + "." + g.O_FIELD_ID +
					" WHERE " +  g.G_O_FIELD_ID_GP + " = ?"
				);

		stGetOutcomes.setInt(1, idGamePlay);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stGetOutcomes.toString());
		}
		
		ResultSet results = stGetOutcomes.executeQuery();
		
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		
		LearningOutcomeController loController = new LearningOutcomeController();
		while (results.next())
		{
			JSONObject outcome = new JSONObject();
			outcome.put(g.O_FIELD_NAME, results.getString(1));
			outcome.put(g.O_FIELD_DESC, results.getString(2));
			outcome.put(g.O_FIELD_TYPE, results.getString(3));
			outcome.put(g.O_FIELD_VALUE, results.getFloat(4));
			outcome.put(g.O_FIELD_ID, results.getInt(5));
			outcome.put(g.G_O_FIELD_VALUE, results.getFloat(6));

			list.add(outcome);
		}
		loController.finalize();
		return list;			
	}
	
	public float getScore(int idGamePlay, int idOutcome) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getScore ***");
		}
		PreparedStatement stGetOutcomeScore = 
				conn.prepareStatement("SELECT "+ g.G_O_FIELD_VALUE + " FROM " + g.TABLE_GAMEPLAY_OUTCOME + 
										" WHERE " + g.G_O_FIELD_ID_GP + " = ? AND " + g.G_O_FIELD_ID_O + " = ?");

		stGetOutcomeScore.setInt(1, idGamePlay);
		stGetOutcomeScore.setInt(2, idOutcome);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stGetOutcomeScore.toString());
		}
		
		ResultSet results = stGetOutcomeScore.executeQuery();
		
		if (results.next())
		{
			if (g.DEBUG_SQL)
			{
				System.out.println();
			}
			
			return results.getFloat(1);
		}
		return g.CST_RETURN_SQL_ERROR;			
	}
	
	public float updateScore(int idGamePlay, int idOutcome, float mark)
	{
		if (g.DEBUG)
		{
			System.out.println("*** updateScore ***");
		}
		try
		{
			String plus = (mark >= 0)? "+" : "";
			PreparedStatement stUpdateOutcomeScore = 
					conn.prepareStatement("UPDATE  "+ g.TABLE_GAMEPLAY_OUTCOME + " SET " +  g.G_O_FIELD_VALUE + " = " + 
											g.G_O_FIELD_VALUE + plus + " ?" + 
											" WHERE " + g.G_O_FIELD_ID_GP + " = ? AND " + g.G_O_FIELD_ID_O + " = ?");

			stUpdateOutcomeScore.setFloat(1, mark);
			stUpdateOutcomeScore.setInt(2, idGamePlay);
			stUpdateOutcomeScore.setInt(3, idOutcome);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stUpdateOutcomeScore.toString());
			}
			
			stUpdateOutcomeScore.executeUpdate();
						
			return g.CST_RETURN_SUCCESS;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (updateScore): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}

	public float resetScore(int idGamePlay, int idOutcome, float value)
	{
		if (g.DEBUG)
		{
			System.out.println("*** resetScore ***");
		}
		try
		{
			PreparedStatement stUpdateOutcomeScore = 
					conn.prepareStatement("UPDATE  "+ g.TABLE_GAMEPLAY_OUTCOME + " SET " +  g.G_O_FIELD_VALUE + " = ?" + 
											" WHERE " + g.G_O_FIELD_ID_GP + " = ? AND " + g.G_O_FIELD_ID_O + " = ?");

			stUpdateOutcomeScore.setFloat(1, value);
			stUpdateOutcomeScore.setInt(2, idGamePlay);
			stUpdateOutcomeScore.setInt(3, idOutcome);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stUpdateOutcomeScore.toString());
			}
			
			stUpdateOutcomeScore.executeUpdate();
						
			return g.CST_RETURN_SUCCESS;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (updateScore): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}

	public ArrayList<JSONObject> assess (int idGamePlay, JSONObject action) throws Exception
	{
		ArrayList<JSONObject> errors = new ArrayList<JSONObject>();
		Boolean valuesMatchFound = false;
		if (g.DEBUG)
		{
			System.out.println("*** assess ***");
		}

		ActionLogController actionLogController = new ActionLogController();		
		FeedbackLogController feedbackLogController = new FeedbackLogController();		
		ArrayList<JSONObject> feedbackTriggered = new ArrayList<JSONObject>();

		JSONObject gameplay = getGamePlay(idGamePlay);
		
		if (g.DEBUG)
		{
			System.out.println("GamePlay : " + gameplay.toJSONString());
		}
		
		int idSG = Integer.parseInt(gameplay.get(g.GP_FIELD_ID_SG).toString());
		int version = Integer.parseInt(gameplay.get(g.GP_FIELD_VERSION).toString());
		
		SeriousGameController sgController = new SeriousGameController();
		JSONObject configFile = sgController.getConfigFile(idSG, version);

		if (action.get("values") == null)
		{
			JSONObject errorValuesNull = new JSONObject();
			errorValuesNull.put("error", "'values' were not found in json sent");
			errorValuesNull.put("json", action);
			errors.add(errorValuesNull);

			actionLogController.finalize();
			feedbackLogController.finalize();
			sgController.finalize();
			return errors;
		}

		if (action.get("action") == null)
		{
			JSONObject errorActionNull = new JSONObject();
			errorActionNull.put("error", "'action' was not found in json sent");
			errorActionNull.put("json", action);
			errors.add(errorActionNull);
			actionLogController.finalize();
			feedbackLogController.finalize();
			sgController.finalize();
			return errors;
		}

		String actionName = action.get("action").toString();
		JSONObject values = (JSONObject) action.get("values");
		
		if (g.DEBUG)
		{
			System.out.println("Config file : " + configFile.toJSONString());
		}
		
		JSONObject evidenceModel = (JSONObject) configFile.get("evidenceModel");
		
		if (evidenceModel.get(actionName) == null)
		{
			JSONObject errorActionName = new JSONObject();
			errorActionName.put("error", "name of action '"+ actionName +"' was not found in the database");
			errors.add(errorActionName);
			actionLogController.finalize();
			feedbackLogController.finalize();
			sgController.finalize();
			return errors;
		}

		JSONObject assessment = (JSONObject) evidenceModel.get(actionName);
		ArrayList<JSONObject> reactions = (ArrayList<JSONObject>) assessment.get("reactions");

		if (g.DEBUG)
		{
			System.out.println("Action found : " + actionName);
			System.out.println("In evidence : " + assessment.toJSONString());
		}
		
		// check if there is a set of values supported in the config file		
		for (JSONObject reaction : reactions) {
			ArrayList<JSONObject> valuesSupported = new ArrayList<JSONObject>();
			if (reaction.containsKey("values")) { valuesSupported = (ArrayList<JSONObject>) reaction.get("values"); }
			for (JSONObject v : valuesSupported) {
				if (v.equals(values))
				{
					valuesMatchFound = true;
					if (g.DEBUG)
					{
						System.out.println("Values match : " + values.toJSONString());
					}
					// get learning outcomes to update
					ArrayList<JSONObject> updateScores = (ArrayList<JSONObject>) reaction.get("marks");

					// foreach - update score
					LearningOutcomeController loController = new LearningOutcomeController();
					for (JSONObject updateScore : updateScores)
					{
						String outcome = updateScore.get("learningOutcome").toString();
						//System.out.println("Reset: " + updateScore.get("reset"));
						Boolean reset = (updateScore.get("reset") != null);
						
						// get mark to be added / reset in LO 
		                float mark=0;
		                if (updateScore.get("markVar") != null)
		                {
		                    String markVar = values.get(updateScore.get("markVar").toString()).toString();
		                    mark = Float.parseFloat(markVar);
		                }
		                else if (updateScore.get("mark") != null)
		                {
		                    mark = Float.parseFloat(updateScore.get("mark").toString());
		                }

						
						int idOutcome = loController.getOutcomeIdByName(outcome, idSG, version);

						if (reset) { resetScore(idGamePlay, idOutcome, mark); }
						else { updateScore(idGamePlay, idOutcome, mark); }

						actionLogController.logAction(action, idGamePlay, idOutcome, mark);
						updateLastActionTimestamp(idGamePlay);
					}
					loController.finalize();
					
					// get feedback to trigger
					ArrayList<JSONObject> feedbackList = new  ArrayList<JSONObject>() ;
					if (reaction.get("feedback") != null) {feedbackList=(ArrayList<JSONObject>) reaction.get("feedback");}

					FeedbackController feedbackController = new FeedbackController();
					for (JSONObject f : feedbackList) {
						JSONObject feedback = feedbackController.getFeedbackByName(f.get("name").toString(), idSG, version);

						Boolean immediteTrigger = (Boolean)f.get("immediate");
						
						// modify message to parameters selected
						String message = feedback.get(g.F_FIELD_MESSAGE).toString();
						Set<String> keys = values.keySet();
						for (String key : keys) 
						{
							message = message.replace("["+ key +"]", values.get(key).toString());
						}
						
						if (g.DEBUG)
						{
							System.out.println("Message : " + message);
						}
						
						feedback.remove(g.F_FIELD_MESSAGE);
						feedback.put(g.F_FIELD_MESSAGE, message);

						feedbackLogController.logFeedback(feedback, idGamePlay);
						
						// if need to trigger feedback (immediate)
						if (immediteTrigger)
						{
							feedbackTriggered.add(feedback);
						}
					}
					feedbackController.finalize();
					actionLogController.finalize();
					feedbackLogController.finalize();
					sgController.finalize();
					return feedbackTriggered;
				}
			}
		}
		
		// if nothing was returned, check for a 'else' case
		for (JSONObject reaction : reactions) {
			if (reaction.containsKey("else")) { 

				valuesMatchFound = true;
				if (g.DEBUG)
				{
					System.out.println("Else case");
				}
				
				ArrayList<JSONObject> updateScores = (ArrayList<JSONObject>) reaction.get("marks");

				// foreach - update score
				for (JSONObject updateScore : updateScores)
				{
					// get learning outcome to update
					//System.out.println("Reset: " + updateScore.get("reset"));
					Boolean reset = (updateScore.get("reset") != null);
					String outcome = updateScore.get("learningOutcome").toString();

					//float mark = Float.parseFloat(updateScore.get("mark").toString());

				    // get mark to be added / reset in LO 
	                float mark=0;
	                if (updateScore.get("markVar") != null)
	                {
	                    String markVar = values.get(updateScore.get("markVar").toString()).toString();
	                    mark = Float.parseFloat(markVar);
	                }
	                else if (updateScore.get("mark") != null)
	                {
	                    mark = Float.parseFloat(updateScore.get("mark").toString());
	                }
				
					LearningOutcomeController loController = new LearningOutcomeController();
					int idOutcome = loController.getOutcomeIdByName(outcome, idSG, version);

					if (reset) { resetScore(idGamePlay, idOutcome, mark); }
					else { updateScore(idGamePlay, idOutcome, mark); }

					actionLogController.logAction(action, idGamePlay, idOutcome, mark);
					updateLastActionTimestamp(idGamePlay);
				}
				
				// get feedback to trigger
				ArrayList<JSONObject> feedbackList = new ArrayList<JSONObject>();
				if ( reaction.get("feedback") != null ) { feedbackList = (ArrayList<JSONObject>) reaction.get("feedback"); }
				
				for (JSONObject f : feedbackList) {
					FeedbackController feedbackController = new FeedbackController();
					JSONObject feedback = feedbackController.getFeedbackByName(f.get("name").toString(), idSG, version);
					
					// modify message to parameters selected
					String message = feedback.get(g.F_FIELD_MESSAGE).toString();
					Set<String> keys = values.keySet();
					for (String key : keys) 
					{
						message = message.replace("["+ key +"]", values.get(key).toString());
					}
					
					if (g.DEBUG)
					{
						System.out.println("Message : " + message);
					}
					
					feedback.remove(g.F_FIELD_MESSAGE);
					feedback.put(g.F_FIELD_MESSAGE, message);

					feedbackLogController.logFeedback(feedback, idGamePlay);
					
					if ((Boolean) f.get("immediate"))
					feedbackTriggered.add(feedback);
				}

				actionLogController.finalize();
				feedbackLogController.finalize();
				sgController.finalize();
				return feedbackTriggered;
			}
		}
		
		if (!valuesMatchFound)
		{
			JSONObject errorValuesMatch = new JSONObject();
			errorValuesMatch.put("values", values);
			errorValuesMatch.put("error", "no match was found in the database for the values received");
			errors.add(errorValuesMatch);
			return errors;
		}
		// return empty list
		
		actionLogController.finalize();
		feedbackLogController.finalize();
		sgController.finalize();
		return feedbackTriggered;
	}
	
	public JSONObject getGamePlay(int idGamePlay)
	{
		if (g.DEBUG)
		{
			System.out.println("*** getGamePlay ***");
		}
		try
		{
			PreparedStatement stGetOutcomeScore = 
					conn.prepareStatement("SELECT "+ g.GP_FIELD_ID_SG + ", " + g.GP_FIELD_VERSION + ", " + 
											g.GP_FIELD_CREATED + ", " + g.GP_FIELD_LASTACTION + ", " + 
											g.GP_FIELD_ENDED + ", " + g.GP_FIELD_ID_PLAYER + " FROM " + g.TABLE_GAMEPLAY + 
											" WHERE " + g.GP_FIELD_ID + " = ?");

			stGetOutcomeScore.setInt(1, idGamePlay);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetOutcomeScore.toString());
			}
			
			ResultSet results = stGetOutcomeScore.executeQuery();
			
			if (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject gameplay = new JSONObject();
				gameplay.put(g.GP_FIELD_ID, idGamePlay);
				gameplay.put(g.GP_FIELD_ID_SG, results.getInt(1));
				gameplay.put(g.GP_FIELD_VERSION, results.getInt(2));
				gameplay.put(g.GP_FIELD_CREATED, results.getTimestamp(3).toString());
				gameplay.put(g.GP_FIELD_LASTACTION, results.getTimestamp(4).toString());
				if (results.getTimestamp(5) != null) { gameplay.put(g.GP_FIELD_ENDED, results.getTimestamp(5).toString()); }
				gameplay.put(g.GP_FIELD_ID_PLAYER, results.getInt(6));
				
				if (g.DEBUG)
				{
					System.out.println(gameplay.toJSONString());
				}
				
				return gameplay;
			}
			return null;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getGamePlay): " + e.getMessage());
			return null;
		}
	}
	
	public int updateLastActionTimestamp(int idGamePlay)
	{
		if (g.DEBUG)
		{
			System.out.println("*** updateLastActionTimestamp ***");
		}
		try
		{
			
			PreparedStatement stEndGamePlay = 
					conn.prepareStatement("UPDATE  "+ g.TABLE_GAMEPLAY + " SET " + 
										g.GP_FIELD_LASTACTION + " = ? WHERE " + g.GP_FIELD_ID + " = ?");

			// 1) create a java calendar instance
			Calendar calendar = Calendar.getInstance();
			 
			// 2) get a java.util.Date from the calendar instance.
			// this date will represent the current instant, or "now".
			java.util.Date now = calendar.getTime();
			 
			// 3) a java current time (now) instance
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
			
			stEndGamePlay.setTimestamp(1, currentTimestamp);
			stEndGamePlay.setInt(2, idGamePlay);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stEndGamePlay.toString());
			}
			
			stEndGamePlay.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (updateLastActionTimestamp): " + e.getMessage() + e.toString());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
 	public int endGamePlay(int idGamePlay) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** endGamePlay ***");
		}

		// check gameplay exists
		JSONObject gp = getGamePlay(idGamePlay);
		if (gp == null)
		{
			return g.CST_RETURN_WRONG_ID;
		}
		// check if gameplay already ended
		else if (gp.get(g.GP_FIELD_ENDED) != null)
		{
			return g.CST_RETURN_ALREADY_ENDED;
		}

		PreparedStatement stEndGamePlay = 
				conn.prepareStatement("UPDATE  "+ g.TABLE_GAMEPLAY + " SET " + 
									g.GP_FIELD_ENDED + " = ? WHERE " + g.GP_FIELD_ID + " = ?");

		// 1) create a java calendar instance
		Calendar calendar = Calendar.getInstance();
		 
		// 2) get a java.util.Date from the calendar instance.
		// this date will represent the current instant, or "now".
		java.util.Date now = calendar.getTime();
		 
		// 3) a java current time (now) instance
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		
		stEndGamePlay.setTimestamp(1, currentTimestamp);
		stEndGamePlay.setInt(2, idGamePlay);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stEndGamePlay.toString());
		}
		
		stEndGamePlay.executeUpdate();
		
		if (g.DEBUG)
		{
			System.out.println("SUCCESS");
			System.out.println();
		}
		
		return g.CST_RETURN_SUCCESS;
	}


	public int endGamePlay(int idGamePlay, Boolean gameWon) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** endGamePlay ***");
		}

		// check gameplay exists
		JSONObject gp = getGamePlay(idGamePlay);
		if (gp == null)
		{
			return g.CST_RETURN_WRONG_ID;
		}
		// check if gameplay already ended
		else if (gp.get(g.GP_FIELD_ENDED) != null)
		{
			return g.CST_RETURN_ALREADY_ENDED;
		}

		PreparedStatement stEndGamePlay = 
				conn.prepareStatement("UPDATE  "+ g.TABLE_GAMEPLAY + " SET " + 
									g.GP_FIELD_ENDED + " = ? , " + g.GP_FIELD_WIN + " = ?"+
									" WHERE " + g.GP_FIELD_ID + " = ?");

		// 1) create a java calendar instance
		Calendar calendar = Calendar.getInstance();
		 
		// 2) get a java.util.Date from the calendar instance.
		// this date will represent the current instant, or "now".
		java.util.Date now = calendar.getTime();
		 
		// 3) a java current time (now) instance
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		
		stEndGamePlay.setTimestamp(1, currentTimestamp);
		stEndGamePlay.setBoolean(2, gameWon);
		stEndGamePlay.setInt(3, idGamePlay);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stEndGamePlay.toString());
		}
		
		stEndGamePlay.executeUpdate();
		
		if (g.DEBUG)
		{
			System.out.println("SUCCESS");
			System.out.println();
		}
		
		return g.CST_RETURN_SUCCESS;
	}

	public ArrayList<JSONObject> getBestGameplaysByGameAndOutcome (int idSG, int version, int numResults, 
		String scoreName, Boolean descendantOrder)
	{
		if (g.DEBUG)
		{
			System.out.println("*** getBestGameplaysByGame ***");
		}
		try
		{
			
			String order = (descendantOrder)? "DESC" : "ASC";

			String getIdScore = "(SELECT "+ g.O_FIELD_ID +" FROM  "+ g.TABLE_OUTCOME +
								" WHERE "+g.O_FIELD_ID_SG+"= ? AND "+g.O_FIELD_VERSION_SG+"= ? AND "+g.O_FIELD_NAME+"= ?)";

			PreparedStatement stGetGameplays = 
					conn.prepareStatement("SELECT "+ g.GP_FIELD_ID + ", " + g.GP_FIELD_CREATED + ", " + 
											g.GP_FIELD_LASTACTION + ", " + g.GP_FIELD_ENDED + ", " + g.GP_FIELD_ID_PLAYER + 
											 ", " + g.GP_FIELD_WIN + ", " + g.G_O_FIELD_ID_O + ", " + g.G_O_FIELD_VALUE + 
											" FROM " + g.TABLE_GAMEPLAY_OUTCOME + ", " + g.TABLE_GAMEPLAY +
											" WHERE " + g.TABLE_GAMEPLAY_OUTCOME + "." + g.G_O_FIELD_ID_GP + 
															" = " + g.TABLE_GAMEPLAY + "." + g.GP_FIELD_ID + 
											" AND " + g.G_O_FIELD_ID_O + "=" + getIdScore +
											" ORDER BY " + g.G_O_FIELD_VALUE + " " + order + " LIMIT ?");

			stGetGameplays.setInt(1, idSG);
			stGetGameplays.setInt(2, version);
			stGetGameplays.setString(3, scoreName);
			//stGetGameplays.setString(4, order);
			stGetGameplays.setInt(4, numResults);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetGameplays.toString());
			}
			
			ResultSet results = stGetGameplays.executeQuery();

			ArrayList<JSONObject> gps = new ArrayList<JSONObject>();
			
			while (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject gameplay = new JSONObject();

				gameplay.put(g.GP_FIELD_ID, results.getInt(1));
				gameplay.put(g.GP_FIELD_ID_SG, idSG);
				gameplay.put(g.GP_FIELD_VERSION, version);
				gameplay.put(g.GP_FIELD_CREATED, results.getTimestamp(2).toString());
				gameplay.put(g.GP_FIELD_LASTACTION, results.getTimestamp(3).toString());
				if (results.getTimestamp(4) != null) { gameplay.put(g.GP_FIELD_ENDED, results.getTimestamp(4).toString()); }
				gameplay.put(g.GP_FIELD_ID_PLAYER, results.getInt(5));
				if (results.getString(6) != null) { gameplay.put(g.GP_FIELD_WIN, results.getBoolean(6)); }
				gameplay.put(g.G_O_FIELD_ID_O, results.getInt(7));
				gameplay.put(g.G_O_FIELD_VALUE, results.getInt(8));
				
				if (g.DEBUG)
				{
					System.out.println(gameplay.toJSONString());
				}
				
				gps.add(gameplay);
			}
			return gps;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getBestGamePlaysByGame): " + e.getMessage());
			return null;
		}
	}

	public ArrayList<JSONObject> getBestTimeGameplaysByGame (int idSG, int version, int numResults, Boolean descendantOrder)
	{
		if (g.DEBUG)
		{
			System.out.println("*** getBestTimeGameplaysByGame ***");
		}
		try
		{
			
			String order = (descendantOrder)? "DESC" : "ASC";

			PreparedStatement stGetGameplays = 
					conn.prepareStatement("SELECT "+ g.GP_FIELD_ID + ", " + g.GP_FIELD_CREATED + ", " + g.GP_FIELD_LASTACTION + ", " + 
											g.GP_FIELD_ENDED + ", " + g.GP_FIELD_ID_PLAYER +  ", " + g.GP_FIELD_WIN + ", " +
											"UNIX_TIMESTAMP("+g.GP_FIELD_ENDED+") - UNIX_TIMESTAMP("+ g.GP_FIELD_CREATED +") AS value "+
											", UNIX_TIMESTAMP("+g.GP_FIELD_LASTACTION+") - UNIX_TIMESTAMP("+ g.GP_FIELD_CREATED +") AS valueBis" +
											" FROM " + g.TABLE_GAMEPLAY + 
											" WHERE " + g.GP_FIELD_ID_SG + " = ? AND " + g.GP_FIELD_VERSION + " = ?"+ 
											" ORDER BY valueBis " + order + " LIMIT ?");

			stGetGameplays.setInt(1, idSG);
			stGetGameplays.setInt(2, version);
			stGetGameplays.setInt(3, numResults);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetGameplays.toString());
			}
			
			ResultSet results = stGetGameplays.executeQuery();

			ArrayList<JSONObject> gps = new ArrayList<JSONObject>();
			
			while (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject gameplay = new JSONObject();

				gameplay.put(g.GP_FIELD_ID, results.getInt(1));
				gameplay.put(g.GP_FIELD_ID_SG, idSG);
				gameplay.put(g.GP_FIELD_VERSION, version);
				gameplay.put(g.GP_FIELD_CREATED, results.getTimestamp(2).toString());
				gameplay.put(g.GP_FIELD_LASTACTION, results.getTimestamp(3).toString());
				if (results.getTimestamp(4) != null) { gameplay.put(g.GP_FIELD_ENDED, results.getTimestamp(4).toString()); }
				gameplay.put(g.GP_FIELD_ID_PLAYER, results.getInt(5));
				if (results.getString(6) != null) { gameplay.put(g.GP_FIELD_WIN, results.getBoolean(6)); }
				if (results.getString(7) != null) { gameplay.put(g.G_O_FIELD_VALUE, results.getInt(7)); }
				else { gameplay.put(g.G_O_FIELD_VALUE, results.getInt(8)); }
				
				if (g.DEBUG)
				{
					System.out.println(gameplay.toJSONString());
				}
				
				gps.add(gameplay);
			}
			return gps;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getBestGamePlaysByGame): " + e.getMessage());
			return null;
		}
	}

	public ArrayList<JSONObject> getGameplaysByGame (int idSG, int version) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getGameplaysByGame ***");
		}
		try
		{
			PreparedStatement stGetGameplays = 
					conn.prepareStatement("SELECT "+ g.GP_FIELD_ID + ", " + g.GP_FIELD_CREATED + ", " + 
											g.GP_FIELD_LASTACTION + ", " + g.GP_FIELD_ENDED + ", " + g.GP_FIELD_ID_PLAYER + 
											 ", " + g.GP_FIELD_WIN +
											" FROM " + g.TABLE_GAMEPLAY + 
											" WHERE " + g.GP_FIELD_ID_SG + " = ? AND " + g.GP_FIELD_VERSION + " = ?");

			stGetGameplays.setInt(1, idSG);
			stGetGameplays.setInt(2, version);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetGameplays.toString());
			}
			
			ResultSet results = stGetGameplays.executeQuery();

			ArrayList<JSONObject> gps = new ArrayList<JSONObject>();
			
			while (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject gameplay = new JSONObject();

				gameplay.put(g.GP_FIELD_ID, results.getInt(1));
				gameplay.put(g.GP_FIELD_ID_SG, idSG);
				gameplay.put(g.GP_FIELD_VERSION, version);
				gameplay.put(g.GP_FIELD_CREATED, results.getTimestamp(2).toString());
				gameplay.put(g.GP_FIELD_LASTACTION, results.getTimestamp(3).toString());
				if (results.getTimestamp(4) != null) { gameplay.put(g.GP_FIELD_ENDED, results.getTimestamp(4).toString()); }
				gameplay.put(g.GP_FIELD_ID_PLAYER, results.getInt(5));
				if (results.getString(6) != null) { gameplay.put(g.GP_FIELD_WIN, results.getBoolean(6)); }
				
				if (g.DEBUG)
				{
					System.out.println(gameplay.toJSONString());
				}
				
				gps.add(gameplay);
			}
			return gps;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getGamePlaysByGame): " + e.getMessage());
			return null;
		}
	}

	public ArrayList<JSONObject> getGameplaysByGameAndPlayer (int idSG, int version, int idPlayer) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getGameplaysByGameAndPlayer ***");
		}
		try
		{
			PreparedStatement stGetGameplays = 
					conn.prepareStatement("SELECT "+ g.GP_FIELD_ID + ", " + g.GP_FIELD_CREATED + ", " + 
											g.GP_FIELD_LASTACTION + ", " + g.GP_FIELD_ENDED + ", " + g.GP_FIELD_WIN + 
											" FROM " + g.TABLE_GAMEPLAY + 
											" WHERE " + g.GP_FIELD_ID_SG + " = ? AND " + g.GP_FIELD_VERSION + 
											" = ?	AND " + g.GP_FIELD_ID_PLAYER + " = ?");

			stGetGameplays.setInt(1, idSG);
			stGetGameplays.setInt(2, version);
			stGetGameplays.setInt(3, idPlayer);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetGameplays.toString());
			}
			
			ResultSet results = stGetGameplays.executeQuery();

			ArrayList<JSONObject> gps = new ArrayList<JSONObject>();
			
			while (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject gameplay = new JSONObject();

				gameplay.put(g.GP_FIELD_ID, results.getInt(1));
				gameplay.put(g.GP_FIELD_ID_SG, idSG);
				gameplay.put(g.GP_FIELD_VERSION, version);
				gameplay.put(g.GP_FIELD_CREATED, results.getTimestamp(2).toString());
				gameplay.put(g.GP_FIELD_LASTACTION, results.getTimestamp(3).toString());
				if (results.getTimestamp(4) != null) { gameplay.put(g.GP_FIELD_ENDED, results.getTimestamp(4).toString()); }
				if (results.getString(5) != null) { gameplay.put(g.GP_FIELD_WIN, results.getBoolean(5)); }
				gameplay.put(g.GP_FIELD_ID_PLAYER, idPlayer);
				
				if (g.DEBUG)
				{
					System.out.println(gameplay.toJSONString());
				}
				
				gps.add(gameplay);
			}
			return gps;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getGamePlaysByGameAndPlayer): " + e.getMessage());
			return null;
		}
	}

	public ArrayList<JSONObject> getGameplaysWonByGameAndPlayer (int idSG, int version, int idPlayer) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getGameplaysByGameAndPlayer ***");
		}
		try
		{
			PreparedStatement stGetGameplays = 
					conn.prepareStatement("SELECT "+ g.GP_FIELD_ID + ", " + g.GP_FIELD_CREATED + ", " + 
											g.GP_FIELD_LASTACTION + ", " + g.GP_FIELD_ENDED +
											" FROM " + g.TABLE_GAMEPLAY + 
											" WHERE " + g.GP_FIELD_ID_SG + " = ? AND " + g.GP_FIELD_VERSION + 
											" = ? AND " + g.GP_FIELD_ID_PLAYER + " = ? AND " + g.GP_FIELD_WIN + " = true");

			stGetGameplays.setInt(1, idSG);
			stGetGameplays.setInt(2, version);
			stGetGameplays.setInt(3, idPlayer);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetGameplays.toString());
			}
			
			ResultSet results = stGetGameplays.executeQuery();

			ArrayList<JSONObject> gps = new ArrayList<JSONObject>();
			
			while (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject gameplay = new JSONObject();

				gameplay.put(g.GP_FIELD_ID, results.getInt(1));
				gameplay.put(g.GP_FIELD_ID_SG, idSG);
				gameplay.put(g.GP_FIELD_VERSION, version);
				gameplay.put(g.GP_FIELD_CREATED, results.getTimestamp(2).toString());
				gameplay.put(g.GP_FIELD_LASTACTION, results.getTimestamp(3).toString());
				if (results.getTimestamp(4) != null) { gameplay.put(g.GP_FIELD_ENDED, results.getTimestamp(4).toString()); }
				gameplay.put(g.GP_FIELD_WIN, true);
				gameplay.put(g.GP_FIELD_ID_PLAYER, idPlayer);
				
				if (g.DEBUG)
				{
					System.out.println(gameplay.toJSONString());
				}
				
				gps.add(gameplay);
			}
			return gps;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (getGamePlaysByGameAndPlayer): " + e.getMessage());
			return null;
		}
	}
}
