package uws.assess4me.assessment;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
/**
 * Root resource (exposed at "student" path)
 */
@Path("student")
/**
 * This class allows CRUD actions on a student
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2014.07
 *
 */
public class StudentResource {


   @GET
    @Path("/{idStudent}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStudent(@PathParam("idStudent") int idStudent) {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject student = engage.getStudentById(idStudent);
            
            return student.toString();
        }
        catch( Exception e )
        {
            return e.getMessage();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int loginStudent(String login) {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject loginJson=(JSONObject) JSONValue.parse(login);
            
            String username = loginJson.get("username").toString();
            String password = loginJson.get("password").toString();
            int idSG = Integer.parseInt(loginJson.get("idSG").toString());

            return engage.loginStudent(username, password, idSG);
        }
        catch( Exception e )
        {
            return -2;
        }
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int createStudent(String student)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject studentJson=(JSONObject) JSONValue.parse(student);
            return engage.createStudent(studentJson);
        }
        catch( Exception e )
        {
            return -2;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int updateStudent(String student)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject studentJson=(JSONObject) JSONValue.parse(student);
            return engage.updateStudent(studentJson);
        }
        catch( Exception e )
        {
            return -2;
        }
    }

    @DELETE
    @Path("/{idStudent}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int deleteStudent(@PathParam("idStudent") int idStudent)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.deleteStudent(idStudent);
        }
        catch( Exception e )
        {
            return -2;
        }
    }
}