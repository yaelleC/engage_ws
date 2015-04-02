package uws.engage.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;

import org.json.simple.JSONObject;

import uws.engage.controller.SeriousGameController;
import uws.engage.controller.GamePlayController;
import uws.engage.controller.FeedbackController;
import uws.engage.controller.LearningOutcomeController;


/**
 * This class allows access to the table feedback of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class BadgesController {

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
	public BadgesController( ) throws Exception
	{
		g = new General();
		Class.forName("com.mysql.jdbc.Driver");		
		conn = DriverManager.getConnection(g.DB_NAME, g.DB_USERNAME, g.DB_PASSWD);
	}
	
	// ********************************** Methods ********************************** //
	
	

	public ArrayList<JSONObject> getBadges(int idSG, int version, int idPlayer ) throws Exception
	{
		// Feedback to be returned
		ArrayList<JSONObject> badges = new ArrayList<JSONObject>();

		if (idPlayer < 0)
		{
			return badges;
		}
						
		if (g.DEBUG)
		{
			System.out.println("********* getBadges **************");
		}
		
		// To get feedback data
		FeedbackController feedbackController = new FeedbackController();

		// To get learning outcome data
		LearningOutcomeController loController = new LearningOutcomeController();


		// To get gameplay data
		GamePlayController gpController = new GamePlayController();

		// To get serious game data
		SeriousGameController sgController = new SeriousGameController();
		JSONObject configFile = sgController.getConfigFile(idSG, version);

		ArrayList<JSONObject> badgeModel = (ArrayList<JSONObject>) configFile.get("badgeModel");
			
		
		// if there are badges triggers described
		for (JSONObject badgeTrigger : badgeModel)
		{
			if (g.DEBUG)
			{
				System.out.println("badges found: " + badgeTrigger.toString());
			}
			
			ArrayList<String> badgesNames = (ArrayList<String>) badgeTrigger.get("feedback");
			String badgeName = badgesNames.get(0);
			int idFeedback = Integer.parseInt(feedbackController.getFeedbackByName(badgeName, idSG, version).get("id").toString());

			int idOutcome = -1;
			if (badgeTrigger.get("outcome") != null)
			{
				String outcome = badgeTrigger.get("outcome").toString();	
				idOutcome = loController.getOutcomeIdByName(outcome, idSG, version);
			}

			String function = badgeTrigger.get("function").toString();
			int limit = Integer.parseInt(badgeTrigger.get("limit").toString());
			Boolean inferior = badgeTrigger.get("sign").toString().equals("<");
			
			// time or gameplay badge
			if (idOutcome < 0)
			{
				if (g.DEBUG)
				{
					System.out.println("time or gameplay found: " + function);
				}

				long numberToCompareWith = 0;
				ArrayList<JSONObject> gps;

				switch (function)
				{
					case "numberGameplays":
						 gps = gpController.getGameplaysByGameAndPlayer(idSG, version, idPlayer);
						 numberToCompareWith = gps.size();
						break;
					case "numberWin":
						gps = gpController.getGameplaysWonByGameAndPlayer(idSG, version, idPlayer);
						 numberToCompareWith = gps.size();
						break;
					case "totalTime":
						numberToCompareWith = 0;
						gps = gpController.getGameplaysByGameAndPlayer(idSG, version, idPlayer);
						for (JSONObject gp : gps) {
							String target = gp.get(g.GP_FIELD_CREATED).toString();
			                DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
			                Date start =  df.parse(target); 

			               long timeSpent = 0;

                    		if (gp.get(g.GP_FIELD_ENDED) != null)
			                {
			                    String target2 = gp.get(g.GP_FIELD_ENDED).toString();
			                    Date end =  df.parse(target2);  
			    
                    			timeSpent = (end.getTime() - start.getTime()) / 60000;
			                }
			                else
			                {
			                    String target2 = gp.get(g.GP_FIELD_LASTACTION).toString();
			                    Date end =  df.parse(target2);  
			    
                    			timeSpent = (end.getTime() - start.getTime()) / 60000;
			                }

                    		if (g.DEBUG)
							{
								System.out.println("timeSpent: " + timeSpent);
							}

                    		numberToCompareWith += timeSpent;
						}
						
						
						break;
					case "averageTime":
						long sum = 0;

						gps = gpController.getGameplaysByGameAndPlayer(idSG, version, idPlayer);
						for (JSONObject gp : gps) {
							String target = gp.get(g.GP_FIELD_CREATED).toString();
			                DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
			                Date start =  df.parse(target); 

			                String target2 = gp.get(g.GP_FIELD_ENDED).toString();
                    		Date end =  df.parse(target2);     
                    		long timeSpent = (end.getTime() - start.getTime()) / 60000;

                    		sum += timeSpent;
						}
						numberToCompareWith = (gps.size() > 0)? sum / gps.size() : 0;
						break;
				}

				if (g.DEBUG)
				{
					System.out.println("numberToCompareWith: " + numberToCompareWith);
				}
									
				if ( (numberToCompareWith > limit) && (!inferior) || (numberToCompareWith < limit) && (inferior) )
				{						
					JSONObject f = feedbackController.getFeedbackById(idFeedback);
					
					String message = f.get(g.F_FIELD_MESSAGE).toString();
					message = message.replace("[number]", numberToCompareWith +"");
					
					f.remove(g.F_FIELD_MESSAGE);
					f.put(g.F_FIELD_MESSAGE, message);
											
					badges.add(f);
					
					//fdbkLogController.logFeedback(f, idGamePlay);
				}
				
			}
			// goal related feedback
			else
			{
				if (g.DEBUG)
				{
					System.out.println("outcome badge found: " + function + " - " + idOutcome);
				}

				Float numberToCompareWith = new Float (0);

				ArrayList<Float> scores = getOutcomeListByGamePlayerAndOutcome(idSG, version, idPlayer, idOutcome);
				Boolean noData = scores.isEmpty();

				switch (function)
				{
					case "sumScore":
						numberToCompareWith = new Float (0);
						 for (Float s : scores ) {
						 	numberToCompareWith += s;
						 }
						break;
					case "averageScore":
						Float sum = new Float (0);
						 for (Float s : scores ) {
						 	sum += s;
						 }
						 numberToCompareWith = (scores.size() > 0)? sum / scores.size() : 0;
						break;
					case "maxScore":
						Collections.max(scores);
						break;
					case "minScore":
						Collections.min(scores);
						break;
				}

				if (!noData)
				{
					if ( (numberToCompareWith > limit) && (!inferior) || (numberToCompareWith < limit) && (inferior) )
					{						
						JSONObject f = feedbackController.getFeedbackById(idFeedback);
						
						String message = f.get(g.F_FIELD_MESSAGE).toString();
						message = message.replace("[number]", numberToCompareWith +"");

						if (f.get(g.F_FIELD_MESSAGE).toString().contains("[outcome]"))
						{				
							LearningOutcomeController loC = new LearningOutcomeController();
							JSONObject o = loC.getOutcomeById(idOutcome);
							
							message = message.replace("[outcome]", o.get(g.O_FIELD_NAME)+" (" + o.get(g.O_FIELD_DESC) + ")");
						}
						
						f.remove(g.F_FIELD_MESSAGE);
						f.put(g.F_FIELD_MESSAGE, message);
												
						badges.add(f);
					}
				}
			}				
		}
		if (g.DEBUG)
		{
			System.out.println(badges);
		}
		return badges;			
	}

	public ArrayList<JSONObject> getBadgesSQL(int idSG, int version, int idPlayer ) throws Exception
	{
		// Feedback to be returned
		ArrayList<JSONObject> badges = new ArrayList<JSONObject>();
						
		if (g.DEBUG)
		{
			System.out.println("********* getBadges **************");
		}
		
		// To get feedback data
		FeedbackController feedbackController = new FeedbackController();

		// To get gameplay data
		GamePlayController gpController = new GamePlayController();

		// To get serious game data
		SeriousGameController sgController = new SeriousGameController();
		JSONObject configFile = sgController.getConfigFile(idSG, version);

		ArrayList<JSONObject> badgeModel = (ArrayList<JSONObject>) configFile.get("badgeModel");

		// check table feedback_trigger for inactivity and goal related feedback triggers
		PreparedStatement stGetBadges = 
				conn.prepareStatement("SELECT "+ g.BT_FIELD_ID_FDBK + ", "+ g.BT_FIELD_ID_OUTCOME +
										", "+ g.BT_FIELD_FUNCTION + ", "+ g.BT_FIELD_LIMIT + ", " +
										g.BT_FIELD_INFERIOR +
										" FROM " + g.TABLE_BADGES_TRIGGER +
										" WHERE " + g.BT_FIELD_ID_SG + " = ? AND "+ g.BT_FIELD_SG_VERSION + " = ?");

		stGetBadges.setInt(1, idSG);
		stGetBadges.setInt(2, version);
		
		if (g.DEBUG_SQL)
		{
			System.out.println(stGetBadges.toString());
		}
		
		ResultSet results = stGetBadges.executeQuery();
		
		// if there are feedback described
		while (results.next())
		{
			if (g.DEBUG)
			{
				System.out.println("badges found: " + results.toString());
			}
			
			int idFeedback = results.getInt(1);			
			int idOutcome = results.getInt(2);
			if (results.wasNull()) { idOutcome = -1; }
			String function = results.getString(3);
			int limit = results.getInt(4);
			Boolean inferior = results.getBoolean(5);
			
			// time or gameplay badge
			if (idOutcome < 0)
			{
				if (g.DEBUG)
				{
					System.out.println("time or gameplay found: " + function);
				}

				long numberToCompareWith = 0;
				ArrayList<JSONObject> gps;

				switch (function)
				{
					case "numberGameplays":
						 gps = gpController.getGameplaysByGameAndPlayer(idSG, version, idPlayer);
						 numberToCompareWith = gps.size();
						break;
					case "numberWin":
						gps = gpController.getGameplaysWonByGameAndPlayer(idSG, version, idPlayer);
						 numberToCompareWith = gps.size();
						break;
					case "totalTime":
						numberToCompareWith = 0;
						gps = gpController.getGameplaysByGameAndPlayer(idSG, version, idPlayer);
						for (JSONObject gp : gps) {
							String target = gp.get(g.GP_FIELD_CREATED).toString();
			                DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
			                Date start =  df.parse(target); 

			                String target2 = gp.get(g.GP_FIELD_ENDED).toString();
                    		Date end =  df.parse(target2);     
                    		long timeSpent = (end.getTime() - start.getTime()) / 1000;

                    		numberToCompareWith += timeSpent;
						}
						if (g.DEBUG)
						{
							System.out.println("numberToCompareWith: " + numberToCompareWith);
						}
						
						break;
					case "averageTime":
						long sum = 0;

						gps = gpController.getGameplaysByGameAndPlayer(idSG, version, idPlayer);
						for (JSONObject gp : gps) {
							String target = gp.get(g.GP_FIELD_CREATED).toString();
			                DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
			                Date start =  df.parse(target); 

			                String target2 = gp.get(g.GP_FIELD_ENDED).toString();
                    		Date end =  df.parse(target2);     
                    		long timeSpent = (end.getTime() - start.getTime()) / 1000;

                    		sum += timeSpent;
						}
						numberToCompareWith = (gps.size() > 0)? sum / gps.size() : 0;
						break;
				}

				if (g.DEBUG)
				{
					System.out.println("numberToCompareWith: " + numberToCompareWith);
				}
									
				if ( (numberToCompareWith > limit) && (!inferior) || (numberToCompareWith < limit) && (inferior) )
				{						
					JSONObject f = feedbackController.getFeedbackById(idFeedback);
					
					String message = f.get(g.F_FIELD_MESSAGE).toString();
					message = message.replace("[number]", numberToCompareWith +"");
					
					f.remove(g.F_FIELD_MESSAGE);
					f.put(g.F_FIELD_MESSAGE, message);
											
					badges.add(f);
					
					//fdbkLogController.logFeedback(f, idGamePlay);
				}
				
			}
			// goal related feedback
			else
			{
				if (g.DEBUG)
				{
					System.out.println("outcome badge found: " + function + " - " + idOutcome);
				}

				Float numberToCompareWith = new Float (0);

				ArrayList<Float> scores = getOutcomeListByGamePlayerAndOutcome(idSG, version, idPlayer, idOutcome);
				Boolean noData = scores.isEmpty();

				switch (function)
				{
					case "sumScore":
						numberToCompareWith = new Float (0);
						 for (Float s : scores ) {
						 	numberToCompareWith += s;
						 }
						break;
					case "averageScore":
						Float sum = new Float (0);
						 for (Float s : scores ) {
						 	sum += s;
						 }
						 numberToCompareWith = (scores.size() > 0)? sum / scores.size() : 0;
						break;
					case "maxScore":
						Collections.max(scores);
						break;
					case "minScore":
						Collections.min(scores);
						break;
				}

				if (!noData)
				{
					if ( (numberToCompareWith > limit) && (!inferior) || (numberToCompareWith < limit) && (inferior) )
					{						
						JSONObject f = feedbackController.getFeedbackById(idFeedback);
						
						String message = f.get(g.F_FIELD_MESSAGE).toString();
						message = message.replace("[number]", numberToCompareWith +"");

						if (f.get(g.F_FIELD_MESSAGE).toString().contains("[outcome]"))
						{				
							LearningOutcomeController loC = new LearningOutcomeController();
							JSONObject o = loC.getOutcomeById(idOutcome);
							
							message = message.replace("[outcome]", o.get(g.O_FIELD_NAME)+" (" + o.get(g.O_FIELD_DESC) + ")");
						}
						
						f.remove(g.F_FIELD_MESSAGE);
						f.put(g.F_FIELD_MESSAGE, message);
												
						badges.add(f);
					}
				}
			}				
		}
		if (g.DEBUG)
		{
			System.out.println(badges);
		}
		return badges;			
	}
	
	public int addBadgeTriggerInactivity ( int idSG, JSONObject trigger, int version )
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
			String sqlQueryIdFeedback = "SELECT "+ g.F_FIELD_ID +" FROM "+ g.TABLE_FEEDBACK +
					" WHERE "+ g.F_FIELD_NAME +" = ? AND " + g.F_FIELD_ID_SG + " = ? AND " + g.F_FIELD_VERSION_SG + " = ?" ;
			

			PreparedStatement stInsertTrigger = 
				conn.prepareStatement("INSERT INTO "+ g.TABLE_FEEDBACK_TRIGGER + "("+ g.FT_FIELD_ID_SG + 
										", " + g.FT_FIELD_ID_FDBK + ", " + g.FT_FIELD_SG_VERSION + 
										", " + g.FT_FIELD_ID_OUTCOME + ", " + g.FT_FIELD_INACTIVITY + 
										", " + g.FT_FIELD_LIMIT + ", " + g.FT_FIELD_INFERIOR + ", " + g.FT_FIELD_REPEAT +
										") VALUES (?, ("+ sqlQueryIdFeedback +"), ?, ?, ?, ?, ?, ?); ");

			stInsertTrigger.setInt(1, idSG);
			ArrayList<String> feedback = (ArrayList<String>) trigger.get("feedback");
			stInsertTrigger.setString(2, (String) feedback.get(0) );
			stInsertTrigger.setInt(3, idSG);
			stInsertTrigger.setInt(4, version);
			stInsertTrigger.setInt(5, version);
			stInsertTrigger.setInt(6, idLO);
			stInsertTrigger.setBoolean(7, false);
			stInsertTrigger.setInt(8, Integer.parseInt(trigger.get("limit").toString()));
			Boolean inferior = trigger.get("sign").toString().equals("<");
			stInsertTrigger.setBoolean(9, inferior);
			stInsertTrigger.setBoolean(10, trigger.containsKey("repeat"));
		
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
	

	public ArrayList<Float> getOutcomeListByGamePlayerAndOutcome(int idSG, int version, int idPlayer, int idOutcome)
		{
			if (g.DEBUG)
			{
				System.out.println("*** GetLearningOutcomeByGame ***");
			}
			try
			{
				PreparedStatement stGetOutcomes = 
						conn.prepareStatement("SELECT "+ g.G_O_FIELD_VALUE + " FROM " + g.TABLE_GAMEPLAY_OUTCOME + 
												" WHERE "+ g.G_O_FIELD_ID_O + " = ? AND " + g.G_O_FIELD_ID_GP + " IN (" +
													" SELECT " + g.GP_FIELD_ID +
													" FROM " + g.TABLE_GAMEPLAY +
													" WHERE " + g.GP_FIELD_ID_SG + " = ? AND " + g.GP_FIELD_VERSION + " = ? AND " +
													g.GP_FIELD_ID_PLAYER + " = ? )");

				stGetOutcomes.setInt(1, idOutcome);
				stGetOutcomes.setInt(2, idSG);
				stGetOutcomes.setInt(3, version);
				stGetOutcomes.setInt(4, idPlayer);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stGetOutcomes.toString());
				}
				
				ResultSet results = stGetOutcomes.executeQuery();
				
				ArrayList<Float> list = new ArrayList<Float>();
				
				while (results.next())
				{
					list.add(results.getFloat(1));
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
