package uws.engage.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.StdErrAcceptor;
import org.json.simple.JSONObject;

/**
 * This class allows access to the student table of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 14.06
 *
 */
public class StudentController {
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
		public StudentController( ) throws Exception
		{
			g = new General();
			
			// in debug mode = trace
			if (g.DEBUG) { System.out.println("### StudentController.StudentController() ###"); }
			
			Class.forName("com.mysql.jdbc.Driver");				
			conn = DriverManager.getConnection(g.DB_NAME, g.DB_USERNAME, g.DB_PASSWD);
			
			if (g.DEBUG) { System.out.println("### Access to DB ok ###"); }
		}
		
		// ********************************** Methods ********************************** //
		
		public int createStudent(JSONObject student)
		{
			if (g.DEBUG)
			{
				System.out.println("*** createStudent ***");
			}
			try
			{
				PreparedStatement stCreateStudent = 
						conn.prepareStatement("INSERT INTO "+ g.TABLE_STUDENT + "("+ g.STDT_FIELD_USERNAME + ", " +
											g.STDT_FIELD_PASSWORD + ", " + g.STDT_FIELD_AGE + ", " + g.STDT_FIELD_GENDER + 
											", " + g.STDT_FIELD_ID_SCHOOL +") VALUES (?, ?, ?, ?, ?)");

				stCreateStudent.setString(1, student.get(g.STDT_FIELD_USERNAME).toString());
				stCreateStudent.setString(2, student.get(g.STDT_FIELD_PASSWORD).toString());
				
				String studentBirthDate = student.get(g.STDT_FIELD_AGE).toString();
				java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(studentBirthDate);
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				stCreateStudent.setDate(3, sqlDate);
				
				stCreateStudent.setString(4, student.get(g.STDT_FIELD_GENDER).toString());
				stCreateStudent.setInt(5, Integer.parseInt(student.get(g.STDT_FIELD_ID_SCHOOL).toString()));
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stCreateStudent.toString());
				}
				
				stCreateStudent.executeUpdate();
				
				if (g.DEBUG)
				{
					System.out.println("SUCCESS");
					System.out.println();
				}
				
				return g.CST_RETURN_SUCCESS;
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (createStudent): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
		public int updateStudent(JSONObject student)
		{
			if (g.DEBUG)
			{
				System.out.println("*** updateStudent ***");
			}
			try
			{
				
				PreparedStatement stUpdateStudent = 
						conn.prepareStatement("UPDATE  "+ g.TABLE_STUDENT + " SET " + 
											g.STDT_FIELD_USERNAME + " = ?, " + g.STDT_FIELD_PASSWORD + " = ?, " +
											g.STDT_FIELD_AGE + " = ?, " + g.STDT_FIELD_GENDER + " = ?, " +
											g.STDT_FIELD_ID_SCHOOL + " = ? WHERE " + g.STDT_FIELD_ID + " = ?");

				stUpdateStudent.setString(1, student.get(g.STDT_FIELD_USERNAME).toString());
				stUpdateStudent.setString(2, student.get(g.STDT_FIELD_PASSWORD).toString());
				
				String studentBirthDate = student.get(g.STDT_FIELD_AGE).toString();
				java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(studentBirthDate);
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				stUpdateStudent.setDate(3, sqlDate);
				
				stUpdateStudent.setString(4, student.get(g.STDT_FIELD_GENDER).toString());
				stUpdateStudent.setInt(5, Integer.parseInt(student.get(g.STDT_FIELD_ID_SCHOOL).toString()));
				stUpdateStudent.setInt(6, Integer.parseInt(student.get(g.STDT_FIELD_ID).toString()));
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stUpdateStudent.toString());
				}
				
				stUpdateStudent.executeUpdate();
				
				if (g.DEBUG)
				{
					System.out.println("SUCCESS");
					System.out.println();
				}
				
				return g.CST_RETURN_SUCCESS;
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (updateStudent): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
		public int deleteStudent(int idStudent)
		{
			if (g.DEBUG)
			{
				System.out.println("*** deleteStudent ***");
			}
			try
			{
				
				PreparedStatement stDeleteStudent = 
						conn.prepareStatement("DELETE FROM  "+ g.TABLE_STUDENT + " WHERE " + 
											g.STDT_FIELD_ID + " = ?");

				stDeleteStudent.setInt(1, idStudent);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stDeleteStudent.toString());
				}
				
				stDeleteStudent.executeUpdate();
				
				if (g.DEBUG)
				{
					System.out.println("SUCCESS");
					System.out.println();
				}
				
				return g.CST_RETURN_SUCCESS;
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (deleteStudent): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
		public JSONObject getStudentsByID(int idStudent)
		{
			if (g.DEBUG)
			{
				System.out.println("*** getStudentsByID ***");
			}
			try
			{
				PreparedStatement stGetStudent = 
						conn.prepareStatement("SELECT "+ g.STDT_FIELD_USERNAME + ", " + g.STDT_FIELD_AGE + ", "+ g.STDT_FIELD_GENDER + ", " +
												g.STDT_FIELD_ID_SCHOOL + ", " + g.STDT_FIELD_PASSWORD + " FROM " + g.TABLE_STUDENT + 
												" WHERE " + g.STDT_FIELD_ID + " = ?");

				stGetStudent.setInt(1, idStudent);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stGetStudent.toString());
				}
				
				ResultSet results = stGetStudent.executeQuery();
				
				if (results.next())
				{
					if (g.DEBUG_SQL)
					{
						System.out.println();
					}
					
					JSONObject student = new JSONObject();
					student.put(g.STDT_FIELD_ID, idStudent);
					student.put(g.STDT_FIELD_USERNAME, results.getString(1));
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					//student.put(g.STDT_FIELD_AGE, formatter.parse(results.getString(2).toString()));
					student.put(g.STDT_FIELD_AGE, results.getString(2));
					student.put(g.STDT_FIELD_GENDER, results.getString(3));
					student.put(g.STDT_FIELD_ID_SCHOOL, results.getInt(4));
					student.put(g.STDT_FIELD_PASSWORD, results.getString(5));
					
					if (g.DEBUG_SQL)
					{
						System.out.println(student.toJSONString());
					}
					
					return student;
				}
				return null;			
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (getStudentsByID): " + e.getMessage());
				return null;
			}
		}
		
		public int addStudentToTeacher(int idStudent, int idTeacher)
		{
			if (g.DEBUG)
			{
				System.out.println("*** addStudentToTeacher ***");
			}
			try
			{
				PreparedStatement stAddStdtToTeacher = 
						conn.prepareStatement("INSERT INTO "+ g.TABLE_STDT_TEACHER + "("+ 
											g.ST_FIELD_ID_STDT + ", " + g.ST_FIELD_ID_TEACHER + 
											") VALUES (?, ?)");

				stAddStdtToTeacher.setInt(1, idStudent);
				stAddStdtToTeacher.setInt(2, idTeacher);

				
				if (g.DEBUG_SQL)
				{
					System.out.println(stAddStdtToTeacher.toString());
				}
				
				stAddStdtToTeacher.executeUpdate();
				
				if (g.DEBUG)
				{
					System.out.println("SUCCESS");
					System.out.println();
				}
				
				return g.CST_RETURN_SUCCESS;
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (addStudentToTeacher): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
		public int addStudentToTeacher(int idStudent, int idTeacher, String className)
		{
			if (g.DEBUG)
			{
				System.out.println("*** addStudentToTeacher ***");
			}
			try
			{
				PreparedStatement stAddStdtToTeacher = 
						conn.prepareStatement("INSERT INTO "+ g.TABLE_STDT_TEACHER + "("+ 
											g.ST_FIELD_ID_STDT + ", " + g.ST_FIELD_ID_TEACHER + ", " + g.ST_FIELD_CLASS + 
											") VALUES (?, ?, ?)");

				stAddStdtToTeacher.setInt(1, idStudent);
				stAddStdtToTeacher.setInt(2, idTeacher);
				stAddStdtToTeacher.setString(3, className);

				
				if (g.DEBUG_SQL)
				{
					System.out.println(stAddStdtToTeacher.toString());
				}
				
				stAddStdtToTeacher.executeUpdate();
				
				if (g.DEBUG)
				{
					System.out.println("SUCCESS");
					System.out.println();
				}
				
				return g.CST_RETURN_SUCCESS;
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (addStudentToTeacher): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
		public ArrayList<JSONObject> getStudentsOfTeacher(int idTeacher)
		{
			if (g.DEBUG)
			{
				System.out.println("*** getStudentsOfTeacher ***");
			}
			try
			{
				PreparedStatement stGetStudentsOfTeacher = 
						conn.prepareStatement("SELECT "+ g.ST_FIELD_ID_STDT + ", " + g.ST_FIELD_CLASS + " FROM " + g.TABLE_STDT_TEACHER + 
												" WHERE " + g.ST_FIELD_ID_TEACHER + " = ?");

				stGetStudentsOfTeacher.setInt(1, idTeacher);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stGetStudentsOfTeacher.toString());
				}
				
				ResultSet results = stGetStudentsOfTeacher.executeQuery();
				
				ArrayList<JSONObject> students = new ArrayList<JSONObject>();
				
				while (results.next())
				{
					if (g.DEBUG_SQL)
					{
						System.out.println();
					}
					
					JSONObject student = getStudentsByID(results.getInt(1));
					
					student.put(g.ST_FIELD_CLASS, results.getString(2));
					
					students.add(student);
				}
				return students;			
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (getStudentsOfTeacher): " + e.getMessage());
				return null;
			}
		}
		
		public ArrayList<JSONObject> getStudentsOfTeacherByClass(int idTeacher, String group)
		{
			if (g.DEBUG)
			{
				System.out.println("*** getStudentsOfTeacherByClass ***");
			}
			try
			{
				PreparedStatement stGetStudentsOfTeacher = 
						conn.prepareStatement("SELECT "+ g.ST_FIELD_ID_STDT + " FROM " + g.TABLE_STDT_TEACHER + 
												" WHERE " + g.ST_FIELD_ID_TEACHER + " = ? AND " + g.ST_FIELD_CLASS + " = ?" );

				stGetStudentsOfTeacher.setInt(1, idTeacher);
				stGetStudentsOfTeacher.setString(2, group);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stGetStudentsOfTeacher.toString());
				}
				
				ResultSet results = stGetStudentsOfTeacher.executeQuery();
				
				ArrayList<JSONObject> students = new ArrayList<JSONObject>();
				
				while (results.next())
				{
					if (g.DEBUG_SQL)
					{
						System.out.println();
					}
					
					JSONObject student = getStudentsByID(results.getInt(1));
					
					student.put(g.ST_FIELD_CLASS, group);
										
					students.add(student);
				}
				return students;			
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (getStudentsOfTeacherByClass): " + e.getMessage());
				return null;
			}
		}
		
		public int removeStudentFromTeacher(int idStudent, int idTeacher)
		{
			if (g.DEBUG)
			{
				System.out.println("*** removeStudentFromTeacher ***");
			}
			try
			{
				
				PreparedStatement stDeleteStudent = 
						conn.prepareStatement("DELETE FROM  "+ g.TABLE_STDT_TEACHER + " WHERE " + 
											g.ST_FIELD_ID_STDT + " = ? AND " + g.ST_FIELD_ID_TEACHER + " = ?");

				stDeleteStudent.setInt(1, idStudent);
				stDeleteStudent.setInt(2, idTeacher);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stDeleteStudent.toString());
				}
				
				stDeleteStudent.executeUpdate();
				
				if (g.DEBUG)
				{
					System.out.println("SUCCESS");
					System.out.println();
				}
				
				return g.CST_RETURN_SUCCESS;
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (removeStudentFromTeacher): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}

		
		public int removeStudentFromTeacherByClass(int idStudent, int idTeacher, String className)
		{
			if (g.DEBUG)
			{
				System.out.println("*** removeStudentFromTeacher ***");
			}
			try
			{
				
				PreparedStatement stDeleteStudent = 
						conn.prepareStatement("DELETE FROM  "+ g.TABLE_STDT_TEACHER + " WHERE " + 
											g.ST_FIELD_ID_STDT + " = ? AND " + g.ST_FIELD_ID_TEACHER + " = ? AND " + g.ST_FIELD_CLASS + " = ?");

				stDeleteStudent.setInt(1, idStudent);
				stDeleteStudent.setInt(2, idTeacher);
				stDeleteStudent.setString(3, className);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stDeleteStudent.toString());
				}
				
				stDeleteStudent.executeUpdate();
				
				if (g.DEBUG)
				{
					System.out.println("SUCCESS");
					System.out.println();
				}
				
				return g.CST_RETURN_SUCCESS;
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (removeStudentFromTeacher): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
		public int getStudentVersionOfSG(int idSG, int idStudent) throws Exception
		{
			if (g.DEBUG)
			{
				System.out.println("*** getStudentVersionOfSG ***");
			}
			PreparedStatement stGetVersionOfSG = 
					conn.prepareStatement("SELECT "+ g.SS_FIELD_VERSION + " FROM " + g.TABLE_SG_STUDENT + 
											" WHERE " + g.SS_FIELD_ID_SG + " = ? AND " + g.SS_FIELD_ID_STUDENT + " = ?" );

			stGetVersionOfSG.setInt(1, idSG);
			stGetVersionOfSG.setInt(2, idStudent);
			
			if (g.DEBUG_SQL)
			{
				System.out.println(stGetVersionOfSG.toString());
			}
			
			ResultSet results = stGetVersionOfSG.executeQuery();
			
			ArrayList<JSONObject> students = new ArrayList<JSONObject>();
			
			if (results.next())
			{
				if (g.DEBUG_SQL)
				{
					System.out.println();
				}	
				return results.getInt(1);
			}
			return g.CST_RETURN_WRONG_ID;			
		}

		public int giveSGaccessToStudent(int idSG, int idStudent, int version)
		{
			if (g.DEBUG)
			{
				System.out.println("*** giveSGaccessToStudent ***");
			}
			try
			{
				String query = "";
				PreparedStatement stGiveAccess;
				
				int versionPlayable =  getStudentVersionOfSG(idSG, idStudent);
				if (versionPlayable<0)
				{
					// insert new row
					query = "INSERT INTO  "+ g.TABLE_SG_STUDENT + "(" + g.SS_FIELD_ID_SG + ", " + g.SS_FIELD_ID_STUDENT +
								", " + g.SS_FIELD_VERSION + ") VALUES (?, ?, ?)";
					stGiveAccess = conn.prepareStatement(query);
					stGiveAccess.setInt(1, idSG);
					stGiveAccess.setInt(2, idStudent);
					stGiveAccess.setInt(3, version);
				}
				else
				{
					// update existing row
					query = "UPDATE  "+ g.TABLE_SG_STUDENT + " SET " + g.SS_FIELD_VERSION + " = ? WHERE " +
							 	g.SS_FIELD_ID_SG + " = ? AND " + g.SS_FIELD_ID_STUDENT + " = ?" ;
					stGiveAccess = conn.prepareStatement(query);
					stGiveAccess.setInt(1, version);
					stGiveAccess.setInt(2, idSG);
					stGiveAccess.setInt(3, idStudent);
				}
				
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stGiveAccess.toString());
				}
				
				stGiveAccess.executeUpdate();
				return g.CST_RETURN_SUCCESS;
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (giveSGaccessToStudent): " + e.getMessage());
				return g.CST_RETURN_SQL_ERROR;
			}
		}
		
		public JSONObject checkStudentUsernameAndPassword(String username, String password)
		{
			if (g.DEBUG)
			{
				System.out.println("*** checkStudentUsernameAndPassword ***");
			}
			try
			{
				PreparedStatement stGetStudent = 
						conn.prepareStatement("SELECT "+ g.STDT_FIELD_ID + ", " + g.STDT_FIELD_AGE + ", "+ g.STDT_FIELD_GENDER + ", " +
												g.STDT_FIELD_ID_SCHOOL + " FROM " + g.TABLE_STUDENT + 
												" WHERE " + g.STDT_FIELD_USERNAME + " = ? AND " + g.STDT_FIELD_PASSWORD + " = ?");

				stGetStudent.setString(1, username);
				stGetStudent.setString(2, password);
				
				if (g.DEBUG_SQL)
				{
					System.out.println(stGetStudent.toString());
				}
				
				ResultSet results = stGetStudent.executeQuery();
				
				if (results.next())
				{
					if (g.DEBUG_SQL)
					{
						System.out.println();
					}
					
					JSONObject student = new JSONObject();
					student.put(g.STDT_FIELD_ID, results.getInt(1));
					student.put(g.STDT_FIELD_USERNAME, username);
					student.put(g.STDT_FIELD_AGE, results.getString(2));
					student.put(g.STDT_FIELD_GENDER, results.getString(3));
					student.put(g.STDT_FIELD_ID_SCHOOL, results.getInt(4));
					//student.put(g.STDT_FIELD_PASSWORD, password);
					
					if (g.DEBUG_SQL)
					{
						System.out.println(student.toJSONString());
					}
					
					return student;
				}
				return null;						
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (checkStudentUsernameAndPassword): " + e.getMessage());
				return null;
			}
		}
}
