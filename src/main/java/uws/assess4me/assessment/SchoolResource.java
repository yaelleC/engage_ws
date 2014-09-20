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
 * Root resource (exposed at "school" path)
 */
@Path("school")
/**
 * This class allows CRUD actions on a school
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2014.07
 *
 */
public class SchoolResource {

	@GET
    @Path("/{idSchool}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSchool(@PathParam("idSchool") int idSchool) {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getSchoolById(idSchool).toString();
        }
        catch( Exception e )
        {
            return e.getMessage();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int createSchool(String school)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject schoolJson=(JSONObject) JSONValue.parse(school);
            return engage.createSchool(schoolJson);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int updateSchool(String school)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject schoolJson=(JSONObject) JSONValue.parse(school);
            return engage.updateSchool(schoolJson);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    @DELETE
    @Path("/{idSchool}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int deleteSchool(@PathParam("idSchool") int idSchool)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.deleteSchool(idSchool);
        }
        catch( Exception e )
        {
            return -10;
        }
    }
}