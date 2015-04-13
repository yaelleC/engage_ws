package uws.engage.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ActionLogController {
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
		public ActionLogController( ) throws Exception
		{
			g = new General();
			
			// in debug mode = trace
			if (g.DEBUG) { System.out.println("### ActionLogController.ActionLogController() ###"); }
			
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
		
		public int logAction(JSONObject action, int idGamePlay, int idOutcome, float mark)
		{
			if (g.DEBUG)
			{
				System.out.println("*** logAction ***");
			}
			try
			{
				PreparedStatement stLogAction = 
						conn.prepareStatement("INSERT INTO "+ g.TABLE_LOG_GP_ACTION + "("+ 
											g.LOG_A_FIELD_ID_GP + ", " + g.LOG_A_FIELD_ID_OUTCOME + ", " +
											g.LOG_A_FIELD_MARK + ", " + g.LOG_A_FIELD_ACTION + ") VALUES (?, ?, ?, ?)");

				stLogAction.setInt(1, idGamePlay);
				stLogAction.setInt(2, idOutcome);
				stLogAction.setFloat(3, mark);
				stLogAction.setString(4, action.toJSONString());
				
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
			catch (Exception e)
			{		
				System.err.println("ERROR (logAction): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
	public ArrayList<JSONObject> getActionLog(int idGamePlay) throws Exception
	{
		if (g.DEBUG)
		{
			System.out.println("*** getActionLog ***");
		}

		ArrayList<JSONObject> logs = new ArrayList<JSONObject>();
		PreparedStatement stLogAction = 
				conn.prepareStatement("SELECT " + g.LOG_A_FIELD_ACTION + ", " + g.LOG_A_FIELD_ID_OUTCOME +
										 ", " + g.LOG_A_FIELD_MARK + ", " + g.LOG_A_FIELD_TIME +
										 " FROM "+ g.TABLE_LOG_GP_ACTION + 
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
			JSONObject action = (JSONObject) new JSONParser().parse(results.getString(1));
			log.put(g.LOG_A_FIELD_ACTION, action);
			log.put(g.LOG_A_FIELD_ID_OUTCOME, results.getInt(2));
			log.put(g.LOG_A_FIELD_MARK, results.getFloat(3));
			log.put(g.LOG_A_FIELD_TIME, results.getTimestamp(4).toString());
			
			logs.add(log);
		}
		
		if (g.DEBUG)
		{
			System.out.println("SUCCESS");
			System.out.println(logs);
		}
		
		return logs;
	}
}
