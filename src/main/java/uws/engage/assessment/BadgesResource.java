package uws.engage.assessment;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.eclipse.xtext.validation.Issue;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import uws.engage.controller.BadgesController;

/**
 * Root resource (exposed at "seriousgame" path)
 */
@Path("badges")

/**
 * This class is the Badges class of the web services, it allows
 * to retrieve the badges achieved by a player for a certain game (JSON format)
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class BadgesResource {
    /**
     * Method handling HTTP GET requests on path "learninganalytics/seriousgame/{idSG}/version/{idVersion}/player/{idP}"
     * 
     * @param idSG = an integer id of the SG to retrieve
     * @param idVersion = an integer, id of the version of the SG to retrieve
     * @param idPlayer = an integer, id of the player
     * @return a json, representing the badges the player has
     */
    @GET
    @Path("seriousgame/{idSG}/version/{version}/player/{idPlayer}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBadges(@PathParam("idSG") int idSeriousGame, @PathParam("version") int version, 
                                        @PathParam("idPlayer") int idPlayer) 
    {
        try
        {
            BadgesController badgesController = new BadgesController();
            return badgesController.getBadges(idSeriousGame, version, idPlayer).toString();
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    
}