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
 * Root resource (exposed at "seriousgame" path)
 */
@Path("seriousgame")
/**
 * This class allows CRUD actions on a seriousgame
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2014.07
 *
 */
public class SeriousGameResource {

// TODO: To decomment when Xtext project is updated

	@GET
    @Path("/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSeriousGame(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getConfigFile(idSeriousGame, version).toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int createSeriousGame(String configFile)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.createSG(configFile);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int updateSeriousGame(String seriousgame)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject sg =(JSONObject) JSONValue.parse(seriousgame);
            return engage.updateSG(sg);
        }
        catch( Exception e )
        {
            return -10;
        }
    }
}