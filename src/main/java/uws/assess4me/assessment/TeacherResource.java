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
/**
 * Root resource (exposed at "teacher" path)
 */
@Path("teacher")
/**
 * This class allows CRUD actions on a teacher
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2014.07
 *
 */
public class TeacherResource {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int createTeacher(String teacher)
    {
        JSONObject teacherJson=(JSONObject) JSONValue.parse(teacher);
        
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.createTeacher(teacherJson);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginTeacher(String login) {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject loginJson=(JSONObject) JSONValue.parse(login);
        
            String email = loginJson.get("email").toString();
            String password = loginJson.get("password").toString();

            return engage.loginTeacher(email, password).toString();
        }
        catch( Exception e )
        {
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int updateTeacher(String teacher)
    {
        JSONObject teacherJson=(JSONObject) JSONValue.parse(teacher);
        
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.updateTeacher(teacherJson);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    @DELETE
    @Path("/{idTeacher}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int deleteTeacher(@PathParam("idTeacher") int idTeacher)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.deleteTeacher(idTeacher);
        }
        catch( Exception e )
        {
            return -10;
        }
    }
}