package uws.engage.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FeedbackLogController {

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
			public FeedbackLogController( ) throws Exception
			{
				g = new General();
				
				// in debug mode = trace
				if (g.DEBUG) { System.out.println("### FeedbackLogController.FeedbackLogController() ###"); }
				
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
			
			public int logFeedback(JSONObject feedback, int idGamePlay) throws Exception
			{
				if (g.DEBUG)
				{
					System.out.println("*** logFeedback ***");
				}

				PreparedStatement stLogAction = 
						conn.prepareStatement("INSERT INTO "+ g.TABLE_LOG_GP_FEEDBACK + "("+ 
											g.LOG_F_FIELD_ID_GP + ", " + g.LOG_F_FIELD_FEEDBACK +
											", " + g.LOG_F_FIELD_ID_FEEDBACK +
											") VALUES (?, ?, ?)");

				stLogAction.setInt(1, idGamePlay);
				stLogAction.setString(2, feedback.toJSONString());
				stLogAction.setInt(3, Integer.parseInt(feedback.get("id").toString()));
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stLogAction.toString());
				}
				
				stLogAction.executeUpdate();
				
				if (g.DEBUG)
				{
					System.out.println("SUCCESS");
					System.out.println();
				}
				
				return g.CST_RETURN_SUCCESS;
			}
			
			public ArrayList<JSONObject> getFeedbackLog(int idGamePlay)
			{
				if (g.DEBUG)
				{
					System.out.println("*** getFeedbackLog ***");
				}
				try
				{
					ArrayList<JSONObject> logs = new ArrayList<JSONObject>();
					PreparedStatement stLogAction = 
							conn.prepareStatement("SELECT " + g.LOG_F_FIELD_FEEDBACK + ", " + g.LOG_F_FIELD_TIME +
													 " FROM "+ g.TABLE_LOG_GP_FEEDBACK + 
													 " WHERE "+ g.LOG_A_FIELD_ID_GP + " = ?");

					stLogAction.setInt(1, idGamePlay);
					
					if (g.DEBUG_SQL)
					{
						System.out.println(stLogAction.toString());
					}
					
					ResultSet results =  stLogAction.executeQuery();
					
					while (results.next())
					{
						JSONObject log = new JSONObject();
						JSONObject feedback = (JSONObject) new JSONParser().parse(results.getString(1));
						log.put(g.LOG_F_FIELD_FEEDBACK, feedback);
						log.put(g.LOG_F_FIELD_TIME, results.getTimestamp(2).toString());
						
						logs.add(log);
					}
					
					if (g.DEBUG)
					{
						System.out.println("SUCCESS");
						System.out.println(logs);
					}
					
					return logs;
				}
				catch (Exception e)
				{		
					System.err.println("ERROR (getFeedbackLog): " + e.getMessage());
					return null;
				}
			}
	}
