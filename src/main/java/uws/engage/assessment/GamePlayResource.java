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
* @apiDefine 2Gameplay Gameplay
*
*/

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
     * @api {get} /gameplay/:idGP/feedback Get Feedback
     * @apiDescription If a feedback is triggered by an action you would receive it after invoking the assess web service. 
     * For any other feedback triggered (inactivity or score related) you can invoke this web service. 
     *
     * @apiName GetFeedback
     * @apiGroup 2Gameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idGP ID of the current gameplay 
     *
     * @apiSuccess {json} Feedback list of feedback to be triggered in the current gameplay
     * @apiSuccessExample {json} Response type
     *  [
     *     {
     *         "message": String,
     *         "id": integer,
     *         "name": String,
     *         "final": String ("true", "win", "lose")
     *         "type": String (POSITIVE, NEGATIVE, NEUTRAL, BADGE or HINT)
     *     },
     *     …
     *   ]
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
    /**
     * @api {get} /gameplay/:idGP/log Get Log
     * @apiDescription After the gameplay, you might want to present the player with a final summative feedback, 
     * to do so you can use the logs that EngAGe keeps of gameplays. 
     *
     * @apiName GetLog
     * @apiGroup 3AfterGameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idGP ID of the current gameplay 
     *
     * @apiSuccess {json} gameplayLog JSON object listing actions and feedback of the gameplay
     * @apiSuccessExample {json} Response type
     *  {
     *     "actionLog": [
     *       {
     *         "time": timestamp (yyyy-MM-dd hh:mm:ss.s),
     *         "action": {
     *             "values": {
     *                 “name”: “value”, …
     *             },
     *             "action": String
     *         },
     *         "mark": integer,
     *         "idOutcome": integer
     *       }, …
     *     ],
     *     "feedbackLog": [
     *       {
     *         "feedback": {
     *             "id": integer,
     *             "message": String,
     *             "name": String,
     *             "type": String (POSITIVE, NEGATIVE, NEUTRAL, BADGE or HINT)
     *         },
     *         "time": timestamp (yyyy-MM-dd hh:mm:ss.s)
     *       },…
     *     ]
     *   }
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
     * @api {get} /gameplay/:idGP/scores Get Scores
     * @apiDescription To retrieve the current values of the learning outcome scores, invoke this web service.
     *
     * @apiName GetScores
     * @apiGroup 2Gameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idGP ID of the current gameplay 
     *
     * @apiSuccess {json} Scores the scores of the current gameplay
     * @apiSuccessExample {json} Example
     *  [
     *       {
     *           "id": 183,
     *           "startingValue": 28,
     *           "description": "distinct countries of the EU left to find",
     *           "name": "eu_countries",
     *           "value": 15
     *       },
     *       {
     *           "id": 184,
     *           "startingValue": 3,
     *           "description": "number of lives the player has",
     *           "name": "lives",
     *           "value": 2
     *       }
     *   ]
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
     * @api {get} /gameplay/:idGP/score/:idScore Get Score
     * @apiDescription To retrieve the current values of the learning outcome scores, invoke this web service.
     *
     * @apiName GetScore
     * @apiGroup 2Gameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idGP ID of the current gameplay 
     * @apiParam {Number} idScore ID of the specific score to lookup 
     *
     * @apiSuccess {Number} Score the score of ID idScore for the current gameplay (float)
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
    * Method handling HTTP POST requests on path "gameplay/"
     * 
     * @param gameplayP = a JSON object containing 3 Integers "idSG", "version" and "idStudent"; 
     * amd an array of characteristics for the player "params"
     * @return the id of the gameplay created if successful, 
     */
            
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String startGamePlay2(String gameplayP)
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

    
    @POST
    @Path("/start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String startGamePlayWithPlayerId2(String gameplayP)
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
     * @api {post} /gameplay/startGP Start a Gameplay
     * @apiDescription When a player starts playing your game, you need to tell the engine to set up a new gameplay. 
     * That way, new scores will be set for your learning outcomes and the player's actions will be associated to them. 
     * In order to create a new gameplay, you need to invoke the <code>gameplay</code> web service as follows.
     *
     * @apiName StartGameplay
     * @apiGroup 2Gameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {json} gamePlayerData JSON object containing game and player data <br/>
     * <i>If idStudent is 0, the game is started for a guest. </i>
     * @apiParamExample {json} Known Player
     *   {  
     *      "idSG": 92,
     *      "version": 0,
     *      "idPlayer": 2
     *   }
     * @apiParamExample {json} New Player
     *   { 
     *       "idSG": 92,
     *       "version": 0,
     *       "idStudent": 9,
     *       "params": [
     *          {   
     *              "name": "age",
     *              "type": "Int",
     *              "value": 15
     *          },
     *          {   
     *               "name": "gender",
     *               "type": "Char",
     *               "value": "m"
     *          }
     *       ]
     *   }
     * @apiParamExample {json} Guest
     *   { 
     *       "idSG": 92,
     *       "version": 0,
     *       "idStudent": 0,
     *       "params": [
     *          {   
     *              "name": "age",
     *              "type": "Int",
     *              "value": 26
     *          },
     *          {   
     *               "name": "gender",
     *               "type": "Char",
     *               "value": "f"
     *          }
     *       ]
     *   }
     *
     * @apiSuccess {json} gameplayData json with ID of gameplay created and ID of player
     * @apiSuccessExample {json} Example
     *       {
     *           "idGameplay": 183,
     *           "idPlayer": 28
     *       }
     */
    @POST
    @Path("/startGP")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String startGamePlayWithPlayerIdReturnJSON(String gameplayP)
    {
        try
        {
            JSONObject gameplay=(JSONObject) JSONValue.parse(gameplayP);

            if (gameplay.get("idSG") == null )
            {
                 return "{ error: \"idSG must be specified\"}";
            }
            if ( gameplay.get("version") == null)
            {
                 return "{ error: \"version must be specified\"}";
            }
           
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
                        return "{ error: \"params must be specified\"}";
                    }
                    if (params.size() > 0 && (params.get(0).get("name") == null ||  params.get(0).get("value") == null ))
                    {
                        return "{ error: \"each parameter must include a name and a value\"}";
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
                            return "{ error: \"params must be specified\"}";
                        }
                        if (params.size() > 0 && (params.get(0).get("name") == null ||  params.get(0).get("value") == null ))
                        {
                            return "{ error: \"each parameter must include a name and a value\"}";
                        }
                        // create a player
                        idPlayer = playerController.createPlayer(idSG, version, idStudent, params);
                    }
                }
            }
            if (idPlayer < 0)
            {
                return "{ error: \"impossible to create player, check that the parameters sent are the ones required.\"}";
            }

            // create row in gameplay table         -> idGameplay
            GamePlayController gpController = new GamePlayController();
            int idGamePlay = gpController.startGamePlay(idPlayer, idSG, version);

            JSONObject gpData = new JSONObject();
            gpData.put("idGameplay", idGamePlay);
            gpData.put("idPlayer", idPlayer);
                    
            return gpData.toString();
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
     * Method handling HTTP POST requests on path "gameplay/{idGP}/assess"
     * 
     * @param idGP = an integer id of the current gameplay
     * @param actionP = a JSON object containing a String "action" and a JSON "values" 
     * (containg a String foreach parameter of the action)
     * @return the list of feedback messages to trigger (JSON object)
     */
    @POST
    @Path("/{idGP}/assess")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String assess2(String actionP, @PathParam("idGP") int idGP)
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
     * Method handling HTTP POST requests on path "gameplay/{idGP}/assess"
     * 
     * @param idGP = an integer id of the current gameplay
     * @param actionP = a JSON object containing a String "action" and a JSON "values" 
     * (containg a String foreach parameter of the action)
     * @return the list of feedback messages to trigger (JSON object)
     */
     /**
     * @api {post} /gameplay/:idGameplay/AssessAndScore Assess
     * @apiDescription During the gameplay, you will want to assess every player's action, 
     * you can do so by invoking the <code>assess</code> web service as follows.
     *
     * @apiName PostAssess
     * @apiGroup 2Gameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idGameplay ID of the current gameplay
     * @apiParam {json} actionData JSON object containing the action to assess (name and parameters)
     * @apiParamExample {json} Example one parameter
     *   {  
     *      "action": "collectEUcountries",
     *      "values": {
     *          "country": "France"
     *      }
     *   }
     * @apiParamExample {json} Example several parameters
     *   {  
     *      "action": "translateToItalian",
     *      "values": {
     *          "englishWord": "Hello",
     *          "italianWord": "Buongiorno",
     *      }
     *   }
     *
     * @apiSuccess {json} feedbackAndScore A JSON object containing feedback to trigger (if any) and the new score values
     * @apiSuccessExample {json} Example
     *  {
     *       "feedback": [
     *           {   "message": "Yes, France is indeed part of the EU",
     *               "name": "correct_country",
     *               "type": "POSITIVE"
     *           }
     *       ],
     *       "scores": [
     *           {   "startingValue": 28,
     *               "description": "countries of the EU left to find",
     *               "name": "eu_countries",
     *               "value": 27
     *           },
     *           {   "startingValue": 3,
     *               "description": "number of lives the player has",
     *               "name": "lives",
     *               "value": 3
     *           }
     *       ]
     *   }
     */
    @POST
    @Path("/{idGP}/assessAndScore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String assessAndScore2(String actionP, @PathParam("idGP") int idGP)
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
     * @api {post} /gameplay/:idGP/end/:end End gameplay
     * @apiDescription When the player stops or finishes the gameplay, 
     * notify the engine by invoking the <code>end</code> web service. This will update the database
     * allowing for accurate calculation of the time spent playing. Some badges are based on the number of gameplays won
     * so it is crucial to save the data.
     *
     * @apiName PostEndGameplay
     * @apiGroup 2Gameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idGP ID of the current gameplay 
     * @apiParam {String="win", "lose", "end"} end specifies whether the game was won, lost or simply ended 
     *
     * @apiSuccess {Number=0, 1, -1} endStatus returns 
     * <ul><li>1 if the gameplay has ended correctly</li><li>0 if the game had already ended</li><li>-1 is the game doesn't exist</li></ul>
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
            if (win.equals("end"))
            {
                return gpController.endGamePlay(idGP) + "";
            }
            else
            {
                Boolean gameWon = win.equals("win");
           
                return gpController.endGamePlay(idGP, gameWon) + "";
            }

        }
        catch( Exception e )
        {
            return "{ error: \"" + e + "\"}";
        }
    }

}
