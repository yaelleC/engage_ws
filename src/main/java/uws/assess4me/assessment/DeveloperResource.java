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
 * Root resource (exposed at "developer" path)
 */
@Path("developer")
/**
 * This class allows CRUD actions on a developer
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2014.07
 *
 */
public class DeveloperResource {

	@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int createDeveloper(String dev)
    {
        JSONObject devJson=(JSONObject) JSONValue.parse(dev);
        
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            System.out.println(dev.toString());
            return engage.createDeveloper(devJson);
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
    public String loginDeveloper(String login) {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject loginJson=(JSONObject) JSONValue.parse(login);
        
            String email = loginJson.get("email").toString();
            String password = loginJson.get("password").toString();
            
            return engage.loginDeveloper(email, password).toString();
        }
        catch( Exception e )
        {
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int updateDeveloper(String dev)
    {
    	JSONObject devJson=(JSONObject) JSONValue.parse(dev);
        
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.updateDeveloper(devJson);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    @DELETE
    @Path("/{idDev}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int deleteDeveloper(@PathParam("idDev") int idDev)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.deleteDeveloper(idDev);
        }
        catch( Exception e )
        {
            return -10;
        }
    }
}