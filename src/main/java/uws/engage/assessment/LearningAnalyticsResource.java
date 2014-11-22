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

import uws.engage.controller.SeriousGameController;

/**
 * Root resource (exposed at "seriousgame" path)
 */
@Path("learninganalytics")

/**
 * This class is the SeriousGame class of the web services, it allows
 * 1. To retrieve a serious game configuration (json format)
 * 2. create an SG configuration from a config file (DSL format)
 * 3. Check a DSL config file grammar
 * 4. update an SG configuration from a config file (JSON format)
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 1.0
 *
 */
public class LearningAnalyticsResource {
    /**
     * Method handling HTTP GET requests on path "learninganalytics/seriousgame/{idSG}/version/{idVersion}"
     * 
     * @param idSG = an integer id of the SG to retrieve
     * @param idVersion = an integer, id of the version of the SG to retrieve
     * @return a json, representing the SG assessment configuration
     */
    @GET
    @Path("seriousgame/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLAinfo(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) 
    {
        try
        {
            JSONObject infoLA = new JSONObject();
            SeriousGameController sgController = new SeriousGameController();
            JSONObject cf = sgController.getConfigFile(idSeriousGame, version);

            // ---------------------------- GAME ---------------------------- //
            
            // ************ Get game basic information ************ //
            JSONObject game = (JSONObject) cf.get("seriousGame");
            game.put("id", idSeriousGame);
            game.put("version", version);

            
            // ************ Get player characteristics ************ //
            ArrayList<String> playerCharacteristics = new ArrayList<String>();
            ArrayList<JSONObject> player = (ArrayList<JSONObject>) cf.get("player");

            for (JSONObject characteristic : player) {
                playerCharacteristics.add(characteristic.get("name").toString());                
            }
            game.put("playerCharacteristics", playerCharacteristics);

            
            // ************** Get learning Outcomes ************** //
            JSONObject learningOutcomes = (JSONObject) cf.get("learningOutcomes");

            game.put("learningOutcomes", learningOutcomes);


            infoLA.put("game", game);


            // ---------------------------- PLAYERS ---------------------------- //
            ArrayList<JSONObject> players = new ArrayList<JSONObject>();

            

            infoLA.put("players", players);

            return infoLA.toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    /*

        players: [
            { idPlayer:7, idStudent: 1, age:10, gender:"girl" },
            { idPlayer:5, idStudent: 2, age:8, gender:"boy" },
            { idPlayer:2, idStudent: 3, age:9, gender:"girl" },
            { idPlayer:4, idStudent: 4, age:10, gender:"girl" },
            { idPlayer:3, idStudent: 5, age:11, gender:"boy" },
            { idPlayer:6, idStudent: 6, age:9, gender:"boy" },
            { idPlayer:1, idStudent: 7, age:10, gender:"boy" }
        ],
    */

    
}