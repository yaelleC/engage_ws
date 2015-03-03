package uws.engage.assessment;


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

import uws.engage.controller.GamePlayController;
import uws.engage.controller.PlayerController;
import uws.engage.controller.FeedbackTriggerController;
import uws.engage.controller.FeedbackLogController;
import uws.engage.controller.ActionLogController;

/**
 * Root resource (exposed at "gameplay" path)
 */
@Path("gameplay")
/**
 * This class is the GamePlay class of the web services, it allows
 * 1. To get required parameters for a player before starting a game
 * 2. to create a new game play and retrieve its id ()
 * 3. to send meaningful action from the game-play for assessment ()
 * 4. get feedback if any ()
 * 5. get the current score of a game-play
 * 6. End a gameplay
 * 7. Get logs (actions and feedback) of a gameplay
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class GamePlayResource {
	
	/**
     * Method handling HTTP POST requests on path "gameplay/parameters"
     * 
     * @param detailsP = a JSON object containing 3 Integers "idSG", "version" and "idStudent"
     * @return a list of parameters required for the player (json format)
     */
    @POST
    @Path("/parameters")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getParameters(String detailsP) {
        try
        {
           JSONObject details=(JSONObject) JSONValue.parse(detailsP);
            
            int idSG = Integer.parseInt(details.get("idSG").toString());
            int version = Integer.parseInt(details.get("version").toString());
            int idStudent = Integer.parseInt(details.get("idStudent").toString());

            // if non-identified student, always return all fields
            if (idStudent == 0)
            {
                idStudent = -1;
            }
            PlayerController playerController = new PlayerController();
            return playerController.getParametersRequired(idStudent, idSG, version).toString();
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    /**
    * Method handling HTTP GET requests on path "gameplay/{idGP}/feedback"
     * 
     * @param idGP = an integer id of the current gameplay
     * @return a list of feedback messages to be triggered in the current gameplay (json format)
     */
    @GET
    @Path("/{idGP}/feedback")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFeedback(@PathParam("idGP") int idGP) {
        try
        {
            FeedbackTriggerController feedbackTriggerController = new FeedbackTriggerController();
            return feedbackTriggerController.getFeedback(idGP).toString();
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    /**
    * Method handling HTTP GET requests on path "gameplay/{idGP}/log"
     * 
     * @param idGP = an integer id of the current gameplay
     * @return the log of the current gameplay (json format)
     */
    @GET
    @Path("/{idGP}/log")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLog(@PathParam("idGP") int idGP) {
        try
        {
            // if gameplay of id idGP not found, return error
            GamePlayController gpController = new GamePlayController();
            if (gpController.getGamePlay(idGP) == null)
            {
                return "{ error: \"Gameplay of id: "+ idGP +" was not found in the database.\" }";
            }

            JSONObject logs = new JSONObject();
        
            // add action logs
            ArrayList<JSONObject> scores = gpController.getScores(idGP);
            logs.put("scores", scores);
        
            // add action logs
            ActionLogController actionLogController = new ActionLogController();
            ArrayList<JSONObject> actionLogs = actionLogController.getActionLog(idGP);
            logs.put("actionLog", actionLogs);
            
            // add feedback logs
            FeedbackLogController feedbackLogController = new FeedbackLogController();
            ArrayList<JSONObject> feedbackLogs = feedbackLogController.getFeedbackLog(idGP);
            logs.put("feedbackLog", feedbackLogs);
            
            return logs.toString();
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    /**
    * Method handling HTTP GET requests on path "gameplay/{idGP}/scores"
     * 
     * @param idGP = an integer id of the current gameplay
     * @return the scores for the learning outcomes of the current gameplay (json format)
     */
    @GET
    @Path("/{idGP}/scores")
    @Produces(MediaType.APPLICATION_JSON)
    public String getScores(@PathParam("idGP") int idGP) {
        try
        {
            GamePlayController gpController;
            gpController = new GamePlayController();
            return gpController.getScores(idGP).toString();
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }
    
    /**
    * Method handling HTTP GET requests on path "gameplay/{idGP}/scores"
     * 
     * @param idGP = an integer id of the current gameplay
     * @param idOutcome = an integer id of the learning outcome to look up
     * @return the score for the learning outcome of id idOutcome in the current gameplay (json format)
     */
    @GET
    @Path("/{idGP}/score/{idOutcome}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getScore(@PathParam("idGP") int idGP, @PathParam("idOutcome") int idOutcome) {
        try
        {
            GamePlayController gpController;
            gpController = new GamePlayController();
            return gpController.getScore(idGP, idOutcome) + "";
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    /**
    * Method handling HTTP PUT requests on path "gameplay/"
     * 
     * @param gameplayP = a JSON object containing 3 Integers "idSG", "version" and "idStudent"; 
     * amd an array of characteristics for the player "params"
     * @return the id of the gameplay created if successful, 
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String startGamePlay(String gameplayP)
    {
        try
        {
            JSONObject gameplay=(JSONObject) JSONValue.parse(gameplayP);
           
            int idSG = Integer.parseInt(gameplay.get("idSG").toString());
            int version = Integer.parseInt(gameplay.get("version").toString());
            int idStudent = Integer.parseInt(gameplay.get("idStudent").toString());

            ArrayList<JSONObject> params = (ArrayList<JSONObject>) gameplay.get("params");

            // use or create player                 -> idPlayer 
            PlayerController playerController = new PlayerController();
            int idPlayer;

            if (idStudent == 0)
            {
                // create a player
                idPlayer = playerController.createPlayer(idSG, version, idStudent, params);
            }
            else
            {
                idPlayer = playerController.getPlayerFromIdStudent(idStudent, idSG, version);
                if (idPlayer == 0)
                {
                    // create a player
                    idPlayer = playerController.createPlayer(idSG, version, idStudent, params);
                }
            }
            
            // create row in gameplay table         -> idGameplay
            GamePlayController gpController = new GamePlayController();
            int idGamePlay = gpController.startGamePlay(idPlayer, idSG, version);
                    
            return idGamePlay + "";
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    /**
    * Method handling HTTP PUT requests on path "gameplay/"
     * 
     * @param gameplayP = a JSON object containing 3 Integers "idSG", "version" and "idStudent"; 
     * amd an array of characteristics for the player "params"
     * @return the id of the gameplay created if successful, 
     */
    @PUT
    @Path("/start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String startGamePlayWithPlayerId(String gameplayP)
    {
        try
        {
            JSONObject gameplay=(JSONObject) JSONValue.parse(gameplayP);
           
            int idSG = Integer.parseInt(gameplay.get("idSG").toString());
            int version = Integer.parseInt(gameplay.get("version").toString());
            int idPlayer;

            if (gameplay.get("idPlayer") != null)
            {
                idPlayer = Integer.parseInt(gameplay.get("idPlayer").toString());
            }
            else
            {
                int idStudent = (gameplay.get("idStudent") != null)? Integer.parseInt(gameplay.get("idStudent").toString()) : 0;
                ArrayList<JSONObject> params = (ArrayList<JSONObject>) gameplay.get("params");

                PlayerController playerController = new PlayerController();

                if (idStudent == 0)
                {
                    if (params == null)
                    {
                        return "Error: The field parameters is missing";
                    }
                    // create a player
                    idPlayer = playerController.createPlayer(idSG, version, idStudent, params);
                }
                else
                {
                    idPlayer = playerController.getPlayerFromIdStudent(idStudent, idSG, version);
                    if (idPlayer == 0)
                    {
                        if (params == null)
                        {
                            return "Error: The field parameters is missing";
                        }
                        // create a player
                        idPlayer = playerController.createPlayer(idSG, version, idStudent, params);
                    }
                }
            }

            // create row in gameplay table         -> idGameplay
            GamePlayController gpController = new GamePlayController();
            int idGamePlay = gpController.startGamePlay(idPlayer, idSG, version);
                    
            return idGamePlay + "";
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    /**
     * Method handling HTTP PUT requests on path "gameplay/{idGP}/assess"
     * 
     * @param idGP = an integer id of the current gameplay
     * @param actionP = a JSON object containing a String "action" and a JSON "values" 
     * (containg a String foreach parameter of the action)
     * @return the list of feedback messages to trigger (JSON object)
     */
    @PUT
    @Path("/{idGP}/assess")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String assess(String actionP, @PathParam("idGP") int idGP)
    {
        try
        {
            JSONObject action=(JSONObject) JSONValue.parse(actionP);
            GamePlayController gpController = new GamePlayController();
            FeedbackTriggerController feedbackTriggerController = new FeedbackTriggerController();

            ArrayList<JSONObject> feedback = gpController.assess(idGP, action);
            feedback.addAll(feedbackTriggerController.getFeedback(idGP));

            return feedback.toString();
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    /**
     * Method handling HTTP PUT requests on path "gameplay/{idGP}/assess"
     * 
     * @param idGP = an integer id of the current gameplay
     * @param actionP = a JSON object containing a String "action" and a JSON "values" 
     * (containg a String foreach parameter of the action)
     * @return the list of feedback messages to trigger (JSON object)
     */
    @PUT
    @Path("/{idGP}/assessAndScore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String assessAndScore(String actionP, @PathParam("idGP") int idGP)
    {
        try
        {
            JSONObject action=(JSONObject) JSONValue.parse(actionP);
            GamePlayController gpController = new GamePlayController();
            FeedbackTriggerController feedbackTriggerController = new FeedbackTriggerController();

            ArrayList<JSONObject> feedback = gpController.assess(idGP, action);
            feedback.addAll(feedbackTriggerController.getFeedback(idGP));

            ArrayList<JSONObject> scores = gpController.getScores(idGP);

            JSONObject returnJSON = new JSONObject();
            returnJSON.put("feedback", feedback);
            returnJSON.put("scores", scores);

            return returnJSON.toString();
        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    /**
     * Method handling HTTP POST requests on path "gameplay/{idGP}/end"
     * 
     * @param idGP = an integer id of the current gameplay
     * @return 1 if the gameplay successfully ended, -1 if the gameplay id doesn't exist in the database
     * 0 if the id exists but corresponds to a gameplay already ended
     */
    @POST
    @Path("/{idGP}/end")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String endGamePlay(@PathParam("idGP") int idGP)
    {
        try
        {
            GamePlayController gpController = new GamePlayController();
           
            return gpController.endGamePlay(idGP) + "";

        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

    /**
     * Method handling HTTP POST requests on path "gameplay/{idGP}/end"
     * 
     * @param idGP = an integer id of the current gameplay
     * @return 1 if the gameplay successfully ended, -1 if the gameplay id doesn't exist in the database
     * 0 if the id exists but corresponds to a gameplay already ended
     */
    @POST
    @Path("/{idGP}/end/{win}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String endGamePlay(@PathParam("idGP") int idGP, @PathParam("win") String win)
    {
        try
        {
            GamePlayController gpController = new GamePlayController();
            Boolean gameWon = win.equals("win");
           
            return gpController.endGamePlay(idGP, gameWon) + "";

        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

}
