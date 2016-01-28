package uws.engage.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.simple.JSONObject;

/**
 * This class allows access to the teacher table of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 14.06
 *
 */
public class TeacherController {
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
	public TeacherController( ) throws Exception
	{
		g = new General();
		
		// in debug mode = trace
		if (g.DEBUG) { System.out.println("### TeacherController.TeacherController() ###"); }
		
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
	
	public int createTeacher(JSONObject teacher)
	{
		if (g.DEBUG)
		{
			System.out.println("*** createTeacher ***");
		}
		try
		{
			PreparedStatement stCreateTeacher = 
					conn.prepareStatement("INSERT INTO "+ g.TABLE_TEACHER + "("+ 
										g.T_FIELD_NAME + ", " + g.T_FIELD_SURNAME + ", " +g.T_FIELD_ID_SCHOOL + ", " +
										g.T_FIELD_EMAIL + ", " + g.T_FIELD_PASSWORD + ") VALUES (?, ?, ?, ?, ?)");

			stCreateTeacher.setString(1, teacher.get(g.T_FIELD_NAME).toString());
			stCreateTeacher.setString(2, teacher.get(g.T_FIELD_SURNAME).toString());
			stCreateTeacher.setInt(3, Integer.parseInt(teacher.get(g.T_FIELD_ID_SCHOOL).toString()));
			stCreateTeacher.setString(4, teacher.get(g.T_FIELD_EMAIL).toString());
			stCreateTeacher.setString(5, teacher.get(g.T_FIELD_PASSWORD).toString());
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stCreateTeacher.toString());
			}
			
			stCreateTeacher.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (createTeacher): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	public int updateTeacher(JSONObject teacher)
	{
		if (g.DEBUG)
		{
			System.out.println("*** updateTeacher ***");
		}
		try
		{
			
			PreparedStatement stUpdateTeacher = 
					conn.prepareStatement("UPDATE  "+ g.TABLE_TEACHER + " SET " + 
										g.T_FIELD_NAME + " = ?, " + g.T_FIELD_SURNAME + " = ?, " +
										g.T_FIELD_EMAIL + " = ?, " + g.T_FIELD_PASSWORD + " = ?, " +
										g.T_FIELD_ID_SCHOOL + " = ? WHERE " + g.T_FIELD_ID + " = ?");

			stUpdateTeacher.setString(1, teacher.get(g.T_FIELD_NAME).toString());
			stUpdateTeacher.setString(2, teacher.get(g.T_FIELD_SURNAME).toString());
			stUpdateTeacher.setString(3, teacher.get(g.T_FIELD_EMAIL).toString());
			stUpdateTeacher.setString(4, teacher.get(g.T_FIELD_PASSWORD).toString());
			stUpdateTeacher.setInt(5, Integer.parseInt(teacher.get(g.T_FIELD_ID_SCHOOL).toString()));
			stUpdateTeacher.setInt(6, Integer.parseInt(teacher.get(g.T_FIELD_ID).toString()));
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stUpdateTeacher.toString());
			}
			
			stUpdateTeacher.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (updateTeacher): " + e.getMessage() + e.toString());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	public int deleteTeacher(int idTeacher)
	{
		if (g.DEBUG)
		{
			System.out.println("*** deleteTeacher ***");
		}
		try
		{
			
			PreparedStatement stDeleteTeacher = 
					conn.prepareStatement("DELETE FROM  "+ g.TABLE_TEACHER + " WHERE " + 
										g.T_FIELD_ID + " = ?");

			stDeleteTeacher.setInt(1, idTeacher);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stDeleteTeacher.toString());
			}
			
			stDeleteTeacher.executeUpdate();
			
			if (g.DEBUG)
			{
				System.out.println("SUCCESS");
				System.out.println();
			}
			
			return g.CST_RETURN_SUCCESS;
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (deleteTeacher): " + e.getMessage());
			return g.CST_RETURN_SQL_ERROR;
		}
	}
	
	
	
	
	
	public JSONObject checkTeacherEmailAndPassword(String email, String password)
	{
		if (g.DEBUG)
		{
			System.out.println("*** checkTeacherEmailAndPassword ***");
		}
		try
		{
			PreparedStatement stGetTeacher = 
					conn.prepareStatement("SELECT "+ g.T_FIELD_ID + ", " + g.T_FIELD_ID_SCHOOL + ", "+ g.T_FIELD_NAME + ", " +
											g.T_FIELD_SURNAME + " FROM " + g.TABLE_TEACHER + 
											" WHERE " + g.T_FIELD_EMAIL + " = ? AND " + g.T_FIELD_PASSWORD + " = ?");

			stGetTeacher.setString(1, email);
			stGetTeacher.setString(2, password);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetTeacher.toString());
			}
			
			ResultSet results = stGetTeacher.executeQuery();
			
			if (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}
				
				JSONObject teacher = new JSONObject();
				teacher.put(g.T_FIELD_ID, results.getInt(1));
				teacher.put(g.T_FIELD_ID_SCHOOL, results.getInt(2));
				teacher.put(g.T_FIELD_NAME, results.getString(3));
				teacher.put(g.T_FIELD_SURNAME, results.getString(4));
				teacher.put(g.T_FIELD_EMAIL, email);
				teacher.put(g.T_FIELD_PASSWORD, password);
				
				return teacher;
			}
			return null;			
		}
		catch (Exception e)
		{		
			System.err.println("ERROR (Get Teacher): " + e.getMessage());
			return null;
		}
	}
}
