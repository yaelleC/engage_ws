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
* @apiDefine 3AfterGameplay After the gameplay(s)
*
*/
/**
 * This class is the Badges class of the web services, it allows
 * to retrieve the badges achieved by a player for a certain game (JSON format)
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class BadgesResource {
    /**
     * @api {get} /badges/seriousgame/:idSG/version/:version/player/:idPlayer Get Badges
     * @apiDescription At some point in your game, you might want to display the badges
     * earned by the player logged in. <br/> To retrieve them, you need to call this web service
     *
     * @apiName GetBadges
     * @apiGroup 3AfterGameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idSG ID of the current game
     * @apiParam {Number} version version number of the current game
     * @apiParam {Number} idPlayer ID of the current player
     *
     * @apiSuccess {json} listBadges A list of badges the player has earned, each badge has: 
     * <ul><li>A<i> message </i> : string </li><li>
     * An<i> id </i> : integer</li><li>
     * A<i> name </i> : string </li></ul>
     * @apiSuccessExample {json} Example
     *  [
     *      {
     *          "message": "You played 10+ times",
     *          "id": 466,
     *          "name": "effort"
     *      },
     *      {
     *          "message": "You found 50 EU countries",
     *          "id": 469,
     *          "name": "bronze_medal"
     *      }
     *  ]
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

    /**
     * @api {get} /badges/all/seriousgame/:idSG/version/:version/player/:idPlayer Get All Badges
     * @apiDescription At some point in your game, you might want to display the badges
     * earned by the player logged in. <br/> 
     * This function returns all the badges available in the game and specify if the player earned it.
     *
     * @apiName GetAllBadges
     * @apiGroup 3AfterGameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idSG ID of the current game
     * @apiParam {Number} version version number of the current game
     * @apiParam {Number} idPlayer ID of the current player
     *
     * @apiSuccess {json} listBadges A list of badges the player has earned, each badge has: 
     * <ul><li>A<i> message </i> : string </li><li>
     * An<i> id </i> : integer</li><li>
     * A<i> name </i> : string </li></ul>
     * @apiSuccessExample {json} Example
     *  [
     *      {
     *          "message": "You played 10+ times",
     *          "id": 466,
     *          "name": "effort",
     *          "earned": false,
     *          "goalNum": 10,
     *          "playerNum": 8
     *      },
     *      {
     *          "message": "You found 50 EU countries",
     *          "id": 469,
     *          "name": "bronze_medal",
     *          "earned": true,
     *          "goalNum": 50,
     *          "playerNum": 76
     *      }
     *  ]
     */
    @GET
    @Path("all/seriousgame/{idSG}/version/{version}/player/{idPlayer}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllBadges(@PathParam("idSG") int idSeriousGame, @PathParam("version") int version, 
                                        @PathParam("idPlayer") int idPlayer) 
    {
        try
        {
            BadgesController badgesController = new BadgesController();
            return badgesController.getAllBadges(idSeriousGame, version, idPlayer).toString();
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }
    
}