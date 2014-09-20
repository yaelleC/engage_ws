package uws.assess4me.assessment;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
/**
 * Root resource (exposed at "gameplay" path)
 */
@Path("gameplay")
/**
 * This class is the class of the web services, it allows
 * 2. to create a new game play and retrieve its id ()
 * 3. to send meaningful action from the game-play for assessment ()
 * 4. get feedback if any ()
 * 5. get the current score of a game-play
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 0.1
 *
 */
public class GamePlayResource {
	
	/**
     * Method handling HTTP GET requests. 
     *
     * @return a list of parameters required for the player 
     */
    @POST
    @Path("/parameters")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getParameters(String detailsP) {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
           JSONObject details=(JSONObject) JSONValue.parse(detailsP);
            
            int idSG = Integer.parseInt(details.get("idSG").toString());
           int version = Integer.parseInt(details.get("version").toString());
           int idStudent = Integer.parseInt(details.get("idStudent").toString());

            return engage.getParametersRequired(idStudent, idSG, version).toString();
        }
        catch( Exception e )
        {
            return null;
        }
    }

    @GET
    @Path("/{idGP}/feedback")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFeedback(@PathParam("idGP") int idGP) {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getFeedback(idGP).toString();
        }
        catch( Exception e )
        {
            return null;
        }
    }

    @GET
    @Path("/{idGP}/log")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLog(@PathParam("idGP") int idGP) {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getLog(idGP).toString();
        }
        catch( Exception e )
        {
            return null;
        }
    }

    @GET
    @Path("/{idGP}/scores")
    @Produces(MediaType.APPLICATION_JSON)
    public String getScores(@PathParam("idGP") int idGP) {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getScores(idGP).toString();
        }
        catch( Exception e )
        {
            return null;
        }
    }

    @GET
    @Path("/{idGP}/score/{idOutcome}")
    @Produces(MediaType.TEXT_PLAIN)
    public float getScores(@PathParam("idGP") int idGP, @PathParam("idOutcome") int idOutcome) {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getScore(idGP, idOutcome);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int startGamePlay(String gameplayP)
    {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject gameplay=(JSONObject) JSONValue.parse(gameplayP);
           
           int idSG = Integer.parseInt(gameplay.get("idSG").toString());
            int version = Integer.parseInt(gameplay.get("version").toString());
            int idStudent = Integer.parseInt(gameplay.get("idStudent").toString());

            ArrayList<JSONObject> params = (ArrayList<JSONObject>) gameplay.get("params");

            return engage.startGamePlay(idSG, version, idStudent, params);
        }
        catch( Exception e )
        {
            return -10;
        }
    }


    @PUT
    @Path("/{idGP}/assess")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String assess(String actionP, @PathParam("idGP") int idGP)
    {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
           JSONObject action=(JSONObject) JSONValue.parse(actionP);
           return engage.assess(idGP, action).toString();
        }
        catch( Exception e )
        {
            return null;
        }
    }

    @POST
    @Path("/{idGP}/end")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int endGamePlay(@PathParam("idGP") int idGP)
    {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.endGamePlay(idGP);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

}
