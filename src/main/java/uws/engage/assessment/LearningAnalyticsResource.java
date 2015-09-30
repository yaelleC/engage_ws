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
import java.util.Arrays;
import java.util.Properties;
import java.util.Collections;
import java.util.Comparator;
 
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
import uws.engage.controller.LearningOutcomeController;
import uws.engage.controller.GamePlayController;
import uws.engage.controller.PlayerController;
import uws.engage.controller.StudentController;
import uws.engage.controller.ActionLogController;
import uws.engage.controller.BadgesController;
import uws.engage.controller.FeedbackLogController;
import uws.engage.controller.General;

/**
 * Root resource (exposed at "seriousgame" path)
 */
@Path("learninganalytics")

/**
* @apiDefine LearningAnalytics Learning Analytics
*
*/

/**
 * This class is the Learning analytics class of the web services, it allows 
 * to retrieve LA info and leaderboard (JSON format)
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 **/
public class LearningAnalyticsResource {

    /**
     * @api {get} /learninganalytics/seriousgame/:idSG/version/:version Get Learning Analytics
     * @apiDescription This web service returns extensive information about the players and the gameplays. 
     * Every action of every player is listed in the JSON returned, along with timestamp and how it affected the score. 
     *
     * @apiName GetLearningAnalytics
     * @apiGroup LearningAnalytics
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idSG ID of the game to retrieve LA from
     * @apiParam {Number} version version number of the current game
     *
     * @apiSuccess {json} LAData all the learning analytics data available on the game
     * @apiSuccessExample {json} Example type
     *  {
     *      "players": [
     *          {
     *              "idPlayer": 1,
     *              "age": "26",
     *              "gender": "f",
     *              "idStudent": 1
     *          }],
     *      "gameplays": [
     *          "id": 600,
     *          "idPlayer": 1,
     *          "timeStarted": "2015-03-12 20:35:01.0",
     *           "finalScores": {
     *              "eu_countries": 16, "lives": 0
     *          },
     *          "timeSpent": 93,
     *          "actions": [
     *           {
     *               "timestamp": 4,
     *               "action": "newCountrySelected",
     *               "mark": -1,
     *               "parameters": {
     *                   "country": "switzerland"
     *               },
     *               "outcome": "lives",
     *               "valuesLog": null
     *           }, ...
     *        ],
     *        "game": {
     *           "genre": "runner",
     *           "idDeveloper": 200,
     *           "learningOutcomes": {
     *               "eu_countries": {
     *                   "desc": "the number of EU countries left to find",
     *                    "feedbackTriggered": [
     *                        {
     *                           "limit": 1,
     *                           "sign": "<",
     *                           "feedback": [
     *                               {
     *                                   "immediate": true,
     *                                   "name": "endWin"
     *                               }
     *                           ]
     *                       }
     *                   ],
     *                   "value": 28
     *               },
     *              "lives": {
     *                   "desc": "the number left to the player",
     *                   "feedbackTriggered": [
     *                       {
     *                           "limit": 1,
     *                            "sign": "<",
     *                           "feedback": [
     *                               {
     *                                   "immediate": true,
     *                                   "name": "endLose"
     *                                }
     *                           ]
     *                       }
     *                   ],
     *                   "value": 3
     *               }
     *           },
     *           "subject": "Europe, Capitals, Geography",
     *           "ageMax": 99,
     *           "playerCharacteristics": [
     *               "age",
     *               "gender"            
     *           ],
     *           "lang": "EN",
     *           "country": "UK",
     *           "version": 0,
     *           "id": 95,
     *           "ageMin": 10,
     *           "description": "Find european capitals",
     *           "name": "EuMouse",
     *           "public": "false"
     *       }
     *  }
     **/
    @GET
    @Path("seriousgame/{idSG}/version/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLAinfo(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("version") int version) 
    {
        try
        {
            General g = new General();
            JSONObject infoLA = new JSONObject();

            // ------------------------- Controllers ----------------------- //
            SeriousGameController sgController = new SeriousGameController();
            ActionLogController actionLogController = new ActionLogController();
            LearningOutcomeController loController = new LearningOutcomeController();
            GamePlayController gpController = new GamePlayController();
            PlayerController playerController = new PlayerController();
            StudentController studentController = new StudentController();            
            BadgesController badgesController = new BadgesController();
            FeedbackLogController feedbackLogController = new FeedbackLogController();

            // ---------------------------- GAME ---------------------------- //
            
            JSONObject cf = sgController.getConfigFile(idSeriousGame, version);

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

            // ************** Get Evidence model ************** //
            JSONObject evidenceModel = (JSONObject) cf.get("evidenceModel");

            game.put("evidenceModel", evidenceModel);

            // ************** Get Feedback ************** //
            JSONObject feedback = (JSONObject) cf.get("feedback");

            game.put("feedback", feedback);


            infoLA.put("game", game);


            // ---------------------------- PLAYERS ---------------------------- //
            ArrayList<JSONObject> players = new ArrayList<JSONObject>();

            ArrayList<JSONObject> gps = gpController.getGameplaysByGame(idSeriousGame, version);
            ArrayList<JSONObject> gpsFiltered = new ArrayList<JSONObject>();

            for (JSONObject gp : gps) {
                if (Integer.parseInt(gp.get("idPlayer").toString()) > 0)
                {
                    JSONObject playerJson = playerController.getPlayerFromId(Integer.parseInt(gp.get("idPlayer").toString()), 
                                                idSeriousGame, version);
                    playerJson.put("idPlayer", playerJson.get("id"));
                    playerJson.remove("id");
                    JSONObject student = studentController.getStudentsByID(Integer.parseInt(playerJson.get("idStudent").toString()));
                    playerJson.put("student", student);
                    // get all badges and those earned by the player
                    int idP = (int)playerJson.get("idPlayer");

                    ArrayList<JSONObject> bs = badgesController.getAllBadges(idSeriousGame, version, idP);                
                    playerJson.put("badges", bs);

                    if (!players.contains(playerJson))  
                    {
                        players.add(playerJson);
                    }  
                    gpsFiltered.add(gp);
                }
            }

            infoLA.put("players", players);

            // ---------------------------- GAMEPLAYS ---------------------------- //
            ArrayList<JSONObject> gameplays = new ArrayList<JSONObject>();

            for (JSONObject gp : gpsFiltered) {
                
                JSONObject gameplay = new JSONObject();
                int idGP = Integer.parseInt(gp.get(g.GP_FIELD_ID).toString());
                gameplay.put("id", idGP);
                gameplay.put("idPlayer", gp.get(g.GP_FIELD_ID_PLAYER));
                if (gp.get(g.GP_FIELD_WIN) != null) 
                { 
                    gameplay.put("won", gp.get(g.GP_FIELD_WIN)); 
                }

                // "2014-11-22 21:04:09.0"
                String target = gp.get(g.GP_FIELD_CREATED).toString();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
                Date start =  df.parse(target);  
    
                gameplay.put("timeStarted", gp.get(g.GP_FIELD_CREATED));

                if (gp.get(g.GP_FIELD_ENDED) != null)
                {
                    String target2 = gp.get(g.GP_FIELD_ENDED).toString();
                    Date end =  df.parse(target2);  
    
                    long timeSpent = (end.getTime() - start.getTime()) / 1000;

                    gameplay.put("timeSpent", timeSpent);
                }
                else
                {
                    String target2 = gp.get(g.GP_FIELD_LASTACTION).toString();
                    Date end =  df.parse(target2);  
    
                    long timeSpent = (end.getTime() - start.getTime()) / 1000;

                    gameplay.put("timeSpent", timeSpent);
                }

                // ---------------------------- Final scores ---------------------------- //
                
                JSONObject finalScores = new JSONObject();

                ArrayList<JSONObject> scores = gpController.getScores(idGP);

                for (JSONObject score : scores) {
                    finalScores.put(score.get("name"), score.get("value"));
                }

                gameplay.put("finalScores", finalScores);   

                // ---------------------------- Actions ---------------------------- //

                ArrayList<JSONObject> actions = new ArrayList<JSONObject>();

                ArrayList<JSONObject> log = actionLogController.getActionLog(idGP);

                for (JSONObject l : log) {
                    JSONObject action = new JSONObject();

                    String targetTime = l.get(g.LOG_A_FIELD_TIME).toString();
                    Date time =  df.parse(targetTime);  
    
                    long timestamp = (time.getTime() - start.getTime()) / 1000;

                    action.put("timestamp", timestamp);
                    JSONObject actionJSON = (JSONObject) l.get("action");

                    action.put("action", actionJSON.get("action"));
                    action.put("parameters", actionJSON.get("values"));
                    action.put("valuesLog", actionJSON.get("valuesLog"));
                    action.put("mark", l.get("mark"));
                    int idOutcome = Integer.parseInt(l.get("idOutcome").toString());
                    action.put("outcome", loController.getOutcomeById(idOutcome).get("name"));

                    actions.add(action);
                }

                gameplay.put("actions", actions);      

                // add feedback that was triggered
                ArrayList<JSONObject> feedbackLogs = feedbackLogController.getFeedbackLog(idGP);
                gameplay.put("feedback", feedbackLogs);   

                gameplays.add(gameplay);
            }

            infoLA.put("gameplays", gameplays);

            actionLogController.finalize();
            loController.finalize();
            gpController.finalize();
            playerController.finalize();
            studentController.finalize();
            sgController.finalize();
            badgesController.finalize();
            feedbackLogController.finalize();

            return infoLA.toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    /**
     * @api {get} /learninganalytics/seriousgame/:idSG/version/:version/teacher/:idTeacher Get Teacher Learning Analytics
     * @apiDescription This web service returns extensive information about the players and the gameplays. 
     * Every action of every player is listed in the JSON returned, along with timestamp and how it affected the score. 
     * The dataset is limited to the students associated to the teacher of ID idTeacher
     *
     * @apiName GetLearningAnalyticsTeacher
     * @apiGroup LearningAnalytics
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idSG ID of the game to retrieve LA from
     * @apiParam {Number} version version number of the current game
     * @apiParam {Number} idTeacher ID of the teacher logged in to see the LA
     *
     * @apiSuccess {json} LAData all the learning analytics data available on the game
     * @apiSuccessExample {json} Example type
     *  {
     *      "players": [
     *          {
     *              "idPlayer": 1,
     *              "age": "26",
     *              "gender": "f",
     *              "idStudent": 1
     *          }],
     *      "gameplays": [
     *          "id": 600,
     *          "idPlayer": 1,
     *          "timeStarted": "2015-03-12 20:35:01.0",
     *           "finalScores": {
     *              "eu_countries": 16, "lives": 0
     *          },
     *          "timeSpent": 93,
     *          "actions": [
     *           {
     *               "timestamp": 4,
     *               "action": "newCountrySelected",
     *               "mark": -1,
     *               "parameters": {
     *                   "country": "switzerland"
     *               },
     *               "outcome": "lives",
     *               "valuesLog": null
     *           }, ...
     *        ],
     *        "game": {
     *           "genre": "runner",
     *           "idDeveloper": 200,
     *           "learningOutcomes": {
     *               "eu_countries": {
     *                   "desc": "the number of EU countries left to find",
     *                    "feedbackTriggered": [
     *                        {
     *                           "limit": 1,
     *                           "sign": "<",
     *                           "feedback": [
     *                               {
     *                                   "immediate": true,
     *                                   "name": "endWin"
     *                               }
     *                           ]
     *                       }
     *                   ],
     *                   "value": 28
     *               },
     *              "lives": {
     *                   "desc": "the number left to the player",
     *                   "feedbackTriggered": [
     *                       {
     *                           "limit": 1,
     *                            "sign": "<",
     *                           "feedback": [
     *                               {
     *                                   "immediate": true,
     *                                   "name": "endLose"
     *                                }
     *                           ]
     *                       }
     *                   ],
     *                   "value": 3
     *               }
     *           },
     *           "subject": "Europe, Capitals, Geography",
     *           "ageMax": 99,
     *           "playerCharacteristics": [
     *               "age",
     *               "gender"            
     *           ],
     *           "lang": "EN",
     *           "country": "UK",
     *           "version": 0,
     *           "id": 95,
     *           "ageMin": 10,
     *           "description": "Find european capitals",
     *           "name": "EuMouse",
     *           "public": "false"
     *       }
     *  }
     **/
    @GET
    @Path("seriousgame/{idSG}/version/{version}/teacher/{idTeacher}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLAinfo(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("version") int version, @PathParam("idTeacher") int idTeacher) 
    {
        try
        {
            General g = new General();
            JSONObject infoLA = new JSONObject();

            // ------------------------- Controllers ----------------------- //
            SeriousGameController sgController = new SeriousGameController();
            ActionLogController actionLogController = new ActionLogController();
            LearningOutcomeController loController = new LearningOutcomeController();
            GamePlayController gpController = new GamePlayController();
            PlayerController playerController = new PlayerController();
            StudentController studentController = new StudentController();            
            BadgesController badgesController = new BadgesController();
            FeedbackLogController feedbackLogController = new FeedbackLogController();

            // ---------------------------- GAME ---------------------------- //
            
            JSONObject cf = sgController.getConfigFile(idSeriousGame, version);

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

            // ************** Get Evidence model ************** //
            JSONObject evidenceModel = (JSONObject) cf.get("evidenceModel");

            game.put("evidenceModel", evidenceModel);

            // ************** Get Feedback ************** //
            JSONObject feedback = (JSONObject) cf.get("feedback");

            game.put("feedback", feedback);


            infoLA.put("game", game);


            // ---------------------------- PLAYERS ---------------------------- //
            ArrayList<JSONObject> players = new ArrayList<JSONObject>();

            ArrayList<JSONObject> gps = gpController.getGameplaysByGame(idSeriousGame, version);
            ArrayList<JSONObject> gpsFiltered = new ArrayList<JSONObject>();

            for (JSONObject gp : gps) {
                if (Integer.parseInt(gp.get("idPlayer").toString()) > 0)
                {
                    JSONObject playerJson = playerController.getPlayerFromId(Integer.parseInt(gp.get("idPlayer").toString()), 
                                                idSeriousGame, version);
                    playerJson.put("idPlayer", playerJson.get("id"));
                    playerJson.remove("id");
                    JSONObject student = studentController.getStudentsByID(Integer.parseInt(playerJson.get("idStudent").toString()));
                    playerJson.put("student", student);

                    // check that player's student exists and that is associated to teacher
                    String[] teachers = student.get("teachers").toString().split(",");
                    String idTeacher_string = idTeacher+"";
                    if (student != null && Arrays.asList(teachers).contains(idTeacher_string))
                    {
                        // get all badges and those earned by the player
                        int idP = (int)playerJson.get("idPlayer");

                        ArrayList<JSONObject> bs = badgesController.getAllBadges(idSeriousGame, version, idP);                
                        playerJson.put("badges", bs);

                        if (!players.contains(playerJson))  
                        {
                            players.add(playerJson);
                        }  
                        gpsFiltered.add(gp);
                    }
                }
            }

            infoLA.put("players", players);

            // ---------------------------- GAMEPLAYS ---------------------------- //
            ArrayList<JSONObject> gameplays = new ArrayList<JSONObject>();

            for (JSONObject gp : gpsFiltered) {
                
                JSONObject gameplay = new JSONObject();
                int idGP = Integer.parseInt(gp.get(g.GP_FIELD_ID).toString());
                gameplay.put("id", idGP);
                gameplay.put("idPlayer", gp.get(g.GP_FIELD_ID_PLAYER));
                
                if (gp.get(g.GP_FIELD_WIN) != null) 
                { 
                    gameplay.put("won", gp.get(g.GP_FIELD_WIN)); 
                }

                // "2014-11-22 21:04:09.0"
                String target = gp.get(g.GP_FIELD_CREATED).toString();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
                Date start =  df.parse(target);  
    
                gameplay.put("timeStarted", gp.get(g.GP_FIELD_CREATED));

                if (gp.get(g.GP_FIELD_ENDED) != null)
                {
                    String target2 = gp.get(g.GP_FIELD_ENDED).toString();
                    Date end =  df.parse(target2);  
    
                    long timeSpent = (end.getTime() - start.getTime()) / 1000;

                    gameplay.put("timeSpent", timeSpent);
                }
                else
                {
                    String target2 = gp.get(g.GP_FIELD_LASTACTION).toString();
                    Date end =  df.parse(target2);  
    
                    long timeSpent = (end.getTime() - start.getTime()) / 1000;

                    gameplay.put("timeSpent", timeSpent);
                }

                // ---------------------------- Final scores ---------------------------- //
                
                JSONObject finalScores = new JSONObject();

                ArrayList<JSONObject> scores = gpController.getScores(idGP);

                for (JSONObject score : scores) {
                    finalScores.put(score.get("name"), score.get("value"));
                }

                gameplay.put("finalScores", finalScores);   

                // ---------------------------- Actions ---------------------------- //

                ArrayList<JSONObject> actions = new ArrayList<JSONObject>();

                ArrayList<JSONObject> log = actionLogController.getActionLog(idGP);

                for (JSONObject l : log) {
                    JSONObject action = new JSONObject();

                    String targetTime = l.get(g.LOG_A_FIELD_TIME).toString();
                    Date time =  df.parse(targetTime);  
    
                    long timestamp = (time.getTime() - start.getTime()) / 1000;

                    action.put("timestamp", timestamp);
                    JSONObject actionJSON = (JSONObject) l.get("action");

                    action.put("action", actionJSON.get("action"));
                    action.put("parameters", actionJSON.get("values"));
                    action.put("valuesLog", actionJSON.get("valuesLog"));
                    action.put("mark", l.get("mark"));
                    int idOutcome = Integer.parseInt(l.get("idOutcome").toString());
                    action.put("outcome", loController.getOutcomeById(idOutcome).get("name"));

                    actions.add(action);
                }

                gameplay.put("actions", actions);      

                // add feedback that was triggered
                ArrayList<JSONObject> feedbackLogs = feedbackLogController.getFeedbackLog(idGP);
                gameplay.put("feedback", feedbackLogs);   

                gameplays.add(gameplay);
            }

            infoLA.put("gameplays", gameplays);

            actionLogController.finalize();
            loController.finalize();
            gpController.finalize();
            playerController.finalize();
            studentController.finalize();
            sgController.finalize();
            badgesController.finalize();
            feedbackLogController.finalize();

            return infoLA.toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }


    /**
     * @api {get} /learninganalytics/leaderboard/seriousgame/:idSG/version/:version Get LeaderBoard
     * @apiDescription EngAGe also gives you access to the game's leader board. 
     * To retrieve it, you need to call this web service.
     *
     * @apiName GetLeaderBoard
     * @apiGroup 3AfterGameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idSG ID of the game to retrieve the leaderboard from
     * @apiParam {Number} version version number of the game
     *
     * @apiSuccess {json} gameLearderboard game leaderboard: JSON object containing an array of performances (15 max) for each score of the game. 
     * The performances are in descending order and are composed of a name and a score. 
     * The leaderboard also include the longest and shortest times (in seconds).
     * @apiSuccessExample {json} Example
     *  {
     *       "score1": [
     *           {
     *               "name": "Anonymous",
     *               "score": 74
     *           },
     *           {
     *               "name": "yaelle",
     *               "score": 38
     *           }
     *       ],
     *       "score2": [
     *           {
     *               "name": "yaelle",
     *               "score": 129
     *           },
     *           {
     *               "name": "Anonymous",
     *               "score": 112
     *           }
     *       ], 
     *      "longestGameplays": [
     *           {
     *               "name": "Yaelle",
     *               "score": 77
     *           },
     *           {
     *               "name": "Anonymous",
     *               "score": 40
     *           }
     *       ],
     *       "shortestGameplays": [
     *           {
     *               "name": "Anonymous",
     *               "score": 6
     *           },
     *           {
     *               "name": "Anonymous",
     *               "score": 40
     *           }
     *       ]
     *   }       
     **/
    @GET
    @Path("leaderboard/seriousgame/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLeaderBoard(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) 
    {
        try
        {            
            return getLeaderboard(idSeriousGame, version, 15).toString();
        }
        catch(Exception e)
        {
            JSONObject error = new JSONObject();
            error.put("error", e);
            return error.toString();
        }
    } 

    private JSONObject getLeaderboard(int idSeriousGame, int version, int limit) throws Exception
    {
        JSONObject infoLeaderboard = new JSONObject();

            LearningOutcomeController loController = new LearningOutcomeController();
            GamePlayController gpController = new GamePlayController();
            PlayerController playerController = new PlayerController();
            StudentController studentController = new StudentController();

            // ************** Get learning Outcomes ************** //

            ArrayList<JSONObject> los = loController.getOutcomeListByGame(idSeriousGame, version);

            // get longest gameplays
            ArrayList<JSONObject> gpsLongestTime = gpController.getBestTimeGameplaysByGame(idSeriousGame, version, limit, true);
            ArrayList<JSONObject> listLongestPlayers = new ArrayList<JSONObject>();
            for (JSONObject gp : gpsLongestTime) {

                    int idPlayer = Integer.parseInt(gp.get("idPlayer").toString());
                    String playerName = "Anonymous";
                    JSONObject leader = new JSONObject();
                    if (idPlayer > 0)
                    {
                        JSONObject player = playerController.getPlayerFromId(idPlayer, idSeriousGame, version);
                        // get player's name
                        if (player.get("idStudent") != 0)
                        {
                            JSONObject student = studentController.getStudentsByID(Integer.parseInt(player.get("idStudent").toString()));
                            playerName = student.get("username").toString();
                        }
                        else
                        {
                            if (player.get("name") != null)
                            {
                                playerName = player.get("name").toString();
                            }
                            else if (player.get("username") != null)
                            {
                                playerName = player.get("username").toString();
                            }
                        }
                    }
                    leader.put("name", playerName);
                    leader.put("score", gp.get("value"));
                                        
                    listLongestPlayers.add(leader);
                    
                }

                infoLeaderboard.put("longestGameplays", listLongestPlayers);

            // get shortest gameplays
            ArrayList<JSONObject> gpsShortestTime = gpController.getBestTimeGameplaysByGame(idSeriousGame, version, limit, false);
            ArrayList<JSONObject> listShortestPlayers = new ArrayList<JSONObject>();
            for (JSONObject gp : gpsShortestTime) {

                    int idPlayer = Integer.parseInt(gp.get("idPlayer").toString());
                    String playerName = "Anonymous";
                    JSONObject leader = new JSONObject();
                    if (idPlayer > 0)
                    {
                        JSONObject player = playerController.getPlayerFromId(idPlayer, idSeriousGame, version);

                        // get player's name
                        if (player.get("idStudent") != 0)
                        {
                            JSONObject student = studentController.getStudentsByID(Integer.parseInt(player.get("idStudent").toString()));
                            playerName = student.get("username").toString();
                        }
                        else
                        {
                            if (player.get("name") != null)
                            {
                                playerName = player.get("name").toString();
                            }
                            else if (player.get("username") != null)
                            {
                                playerName = player.get("username").toString();
                            }
                        }
                    }
                    leader.put("name", playerName);
                    leader.put("score", gp.get("value"));
                                        
                    listShortestPlayers.add(leader);
                    
                }

                infoLeaderboard.put("shortestGameplays", listShortestPlayers);

            // get each score
            for (JSONObject lo : los ) {
                ArrayList<JSONObject> listPlayers = new ArrayList<JSONObject>();

                ArrayList<JSONObject> gps = gpController.getBestGameplaysByGameAndOutcome(idSeriousGame, version, 
                                                                                        limit, lo.get("name").toString(), true);

                for (JSONObject gp : gps) {

                    int idPlayer = Integer.parseInt(gp.get("idPlayer").toString());
                    String playerName = "Anonymous";
                    JSONObject leader = new JSONObject();
                    if (idPlayer > 0)
                    {
                        JSONObject player = playerController.getPlayerFromId(idPlayer, idSeriousGame, version);

                        // get player's name
                        if (player.get("idStudent") != 0)
                        {
                            JSONObject student = studentController.getStudentsByID(Integer.parseInt(player.get("idStudent").toString()));
                            playerName = student.get("username").toString();
                        }
                        else
                        {
                            if (player.get("name") != null)
                            {
                                playerName = player.get("name").toString();
                            }
                            else if (player.get("username") != null)
                            {
                                playerName = player.get("username").toString();
                            }
                        }
                    }

                    leader.put("name", playerName);
                    leader.put("score", gp.get("value"));
                                        
                    listPlayers.add(leader);
                    
                }


                infoLeaderboard.put(lo.get("name"), listPlayers);
            }
            loController.finalize();
            gpController.finalize();
            playerController.finalize();
            studentController.finalize();
            return infoLeaderboard;
    }

    /**
     * @api {get} /learninganalytics/leaderboard/:limit/seriousgame/:idSG/version/:version Get LeaderBoard (limit)
     * @apiDescription EngAGe allows you to specify the number of results you want for the game's leader board. 
     *
     * @apiName GetLeaderBoardLimit
     * @apiGroup 3AfterGameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idSG ID of the game to retrieve the leaderboard from
     * @apiParam {Number} version version number of the game
     * @apiParam {Number} limit maximum number of performances to retrieve
     *
     * @apiSuccess {json} gameLearderboard game leaderboard: JSON object containing an array of performances (max :limit) for each score of the game. 
     * The performances are in descending order and are composed of a name and a score. 
     * The leaderboard also include the longest and shortest times (in seconds).
     * @apiSuccessExample {json} Example
     *  {
     *       "score1": [
     *           {
     *               "name": "Anonymous",
     *               "score": 74
     *           },
     *           {
     *               "name": "yaelle",
     *               "score": 38
     *           }
     *       ],
     *       "score2": [
     *           {
     *               "name": "yaelle",
     *               "score": 129
     *           },
     *           {
     *               "name": "Anonymous",
     *               "score": 112
     *           }
     *       ], 
     *      "longestGameplays": [
     *           {
     *               "name": "Yaelle",
     *               "score": 77
     *           },
     *           {
     *               "name": "Anonymous",
     *               "score": 40
     *           }
     *       ],
     *       "shortestGameplays": [
     *           {
     *               "name": "Anonymous",
     *               "score": 6
     *           },
     *           {
     *               "name": "Anonymous",
     *               "score": 40
     *           }
     *       ]
     *   }       
     **/
    @GET
    @Path("leaderboard/{numResults}/seriousgame/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLeaderBoardNumResults(@PathParam("numResults") int numRes, @PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) 
    {
        try
        {
            return getLeaderboard(idSeriousGame, version, numRes).toString();
        }
        catch(Exception e)
        {
            JSONObject error = new JSONObject();
            error.put("error", e);
            return error.toString();
        }
    }    
}

class LeadersCompare implements Comparator<JSONObject>
{
    @Override
    public int compare(JSONObject o1, JSONObject o2)
    {
        Float o1Score = Float.parseFloat(o1.get("score").toString());
        Float o2Score = Float.parseFloat(o2.get("score").toString());
        return (o2Score.compareTo(o1Score));
    }
}