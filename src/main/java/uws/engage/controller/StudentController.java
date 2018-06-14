package uws.engage.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.*;

import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.StdErrAcceptor;
import org.json.simple.JSONObject;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;
import javax.naming.NamingEnumeration;

/**
 * This class allows access to the student table of the database
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
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

				if (student.get(g.STDT_FIELD_AGE) != null) {
					String studentBirthDate = student.get(g.STDT_FIELD_AGE).toString();
					java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(studentBirthDate);
					java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
					stCreateStudent.setDate(3, sqlDate);
				} else {
					stCreateStudent.setDate(3, null);
				}

				if (student.get(g.STDT_FIELD_GENDER) != null) {
					stCreateStudent.setString(4, student.get(g.STDT_FIELD_GENDER).toString());
				} else {
					stCreateStudent.setString(4, null);
				}

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
				String[] dbURL = g.DB_NAME.split("/");
				String dbName = g.DB_NAME.split("/")[dbURL.length-1];

				//String dbName = "engage_dev";

				PreparedStatement stGetStudent = 
						conn.prepareStatement("SELECT "+ g.STDT_FIELD_USERNAME + ", " + g.STDT_FIELD_AGE + ", "+ g.STDT_FIELD_GENDER + ", " +
												g.STDT_FIELD_ID_SCHOOL + ", " + g.STDT_FIELD_PASSWORD + ", " + 
												"GROUP_CONCAT(" + g.TABLE_STDT_TEACHER + "." + g.ST_FIELD_ID_TEACHER + ")" +
												" FROM " + g.TABLE_STUDENT + ", "+ dbName +"." + g.TABLE_STDT_TEACHER + 
												" WHERE " + g.TABLE_STUDENT + "." + g.STDT_FIELD_ID + " = ? AND " + 
												g.TABLE_STUDENT + "." + g.STDT_FIELD_ID + " = " + 
												g.TABLE_STDT_TEACHER + "." + g.ST_FIELD_ID_STDT + 
												" GROUP BY " + g.STDT_FIELD_USERNAME);

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
					student.put("teachers", results.getString(6));
					
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

		public JSONObject getStudentsByUsername(String username)
		{
			if (g.DEBUG)
			{
				System.out.println("*** getStudentsByUsername ***");
			}
			try
			{
				String[] dbURL = g.DB_NAME.split("/");
				String dbName = g.DB_NAME.split("/")[dbURL.length-1];

				//String dbName = "engage_dev";

				PreparedStatement stGetStudent =
						conn.prepareStatement("SELECT "+ g.STDT_FIELD_ID + ", " + g.STDT_FIELD_AGE + ", "+ g.STDT_FIELD_GENDER + ", " +
								g.STDT_FIELD_ID_SCHOOL + ", " + g.STDT_FIELD_PASSWORD +
								" FROM " + g.TABLE_STUDENT +
								" WHERE " + g.TABLE_STUDENT + "." + g.STDT_FIELD_USERNAME + " = ? ");

				stGetStudent.setString(1, username);

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
				System.err.println("ERROR (getStudentsByUsername): " + e.getMessage());
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

		// TODO: check with LDAP instead
		public JSONObject checkStudentUsernameAndPassword(String username, String password)
		{
			if (g.DEBUG)
			{
				System.out.println("*** checkStudentUsernameAndPassword ***");
			}
			try
			{
				Hashtable<String, String> env = new Hashtable<String, String>();

				env.put(Context.INITIAL_CONTEXT_FACTORY, g.LDAP_INITIAL_CONTEXT_FACTORY);
				env.put(Context.SECURITY_AUTHENTICATION, g.LDAP_SECURITY_AUTHENTICATION);
				env.put(Context.PROVIDER_URL, g.LDAP_PROVIDER_URL);

				// The value of Context.SECURITY_PRINCIPAL must be the logon username with the domain name
				env.put(Context.SECURITY_PRINCIPAL, "cn="+username+",dc=example,dc=com");

				// The value of the Context.SECURITY_CREDENTIALS should be the user's password
				env.put(Context.SECURITY_CREDENTIALS, "password");

				DirContext ctx;

				try {
					// Authenticate the logon user
					ctx = new InitialDirContext(env);
					/**
					 * Once the above line was executed successfully, the user is said to be
					 * authenticated and the InitialDirContext object will be created.
					 */
					if (g.DEBUG)
					{
						System.out.println("*** user "+username+" found ***");
					}
					// The user belongs to the specified OU(s), do something...
					//SearchResult next = getLDAPInformation(ctx, username).nextElement();
					//String studentID = next.getAttributes().get("id").get().toString();
					JSONObject student = getStudentsByUsername(username);
					if (g.DEBUG)
					{
						System.out.println("student:");
						System.out.println(student);
					}

					ctx.close();

					if (student != null)
					{
						return student;
					}
					else
					{
						// if the student logs in for the first time, create an EngAGe account
						student = new JSONObject();
						student.put(g.STDT_FIELD_USERNAME, username);
						student.put(g.STDT_FIELD_ID_SCHOOL, 2);
						student.put(g.STDT_FIELD_PASSWORD, "NO_LDAP");
						createStudent(student);
						student = getStudentsByUsername(username);
						return student;
					}

					//TODO: Start checking if the user is within the organization unit(s)
					/*
					ou=scientists,dc=example,dc=com
					// String searchBase = "OU=scientists,OU=mathematicians,DC=example,DC=com";
					//String searchBase = "DC=example,DC=com";
					//String searchFilter = "(&(objectClass=person)(sAMAccountName=read-only-admin)(userPrincipalName=read-only-admin@example.com))";
					//SearchControls sCtrl = new SearchControls();
					//sCtrl.setSearchScope(SearchControls.ONELEVEL_SCOPE);

					//NamingEnumeration answer = ctx.search(searchBase, searchFilter, sCtrl);

					boolean pass = false;
					if (answer.hasMoreElements()) {
						pass = true;
					}

					if (pass) {

					} else {
						// The user doesn't belong to the specified OU(s)

						System.out.println("*** user not found ***");
						return null;
					}

					*/

				} catch (Exception ex) {
					// Authentication failed, just check on the exception and do something about it.
					System.err.println("ERROR (checkStudentUsernameAndPassword): ");
					System.err.println(ex);
					return null;
				}
				/*
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
				}*/
			}
			catch (Exception e)
			{		
				System.err.println("ERROR (checkStudentUsernameAndPassword): " + e.getMessage());
				return null;
			}
		}
}
