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
import uws.engage.controller.General;

/**
 * Root resource (exposed at "seriousgame" path)
 */
@Path("learninganalytics")

/**
 * This class is the Learning analytics class of the web services, it allows 
 * to retrieve LA info and leaderboard (JSON format)
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
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
            General g = new General();
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

            GamePlayController gpController = new GamePlayController();
            PlayerController playerController = new PlayerController();

            ArrayList<JSONObject> gps = gpController.getGameplaysByGame(idSeriousGame, version);

            // TODO: deal with gps null case -> error when gameplay was not ended? check

            for (JSONObject gp : gps) {
                JSONObject playerJson = playerController.getPlayerFromId(Integer.parseInt(gp.get("idPlayer").toString()), 
                                            idSeriousGame, version);
                playerJson.put("idPlayer", playerJson.get("id"));
                playerJson.remove("id");
                if (!players.contains(playerJson))  
                {
                    players.add(playerJson);
                }  
            }

            infoLA.put("players", players);

            // ---------------------------- GAMEPLAYS ---------------------------- //
            ArrayList<JSONObject> gameplays = new ArrayList<JSONObject>();

            for (JSONObject gp : gps) {
                
                JSONObject gameplay = new JSONObject();
                int idGP = Integer.parseInt(gp.get(g.GP_FIELD_ID).toString());
                gameplay.put("id", idGP);
                gameplay.put("idPlayer", gp.get(g.GP_FIELD_ID_PLAYER));

                // "2014-11-22 21:04:09.0"
                String target = gp.get(g.GP_FIELD_CREATED).toString();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
                Date start =  df.parse(target);  
    
                gameplay.put("timeStarted", gp.get(g.GP_FIELD_CREATED));

                System.out.println(start);

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
                ActionLogController actionLogController = new ActionLogController();
                LearningOutcomeController loController = new LearningOutcomeController();

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

                gameplays.add(gameplay);
            }

            infoLA.put("gameplays", gameplays);


            return infoLA.toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    /*    
        "actions": [
          {
            "timestamp": 77,
            "action": "associateCountryCapital",
            "parameters": [
              "Paris",
              "France"
            ],
            "mark": 2,
            "outcome": "european_capital"
          }
        ]

        "time": "2014-11-22 21:04:09.0",
            "action": 
            {
                "values": {
                    "selectedProp": 1
                },
                "action": "compare",
                "valuesLog": {
                    "propName": "Salt and Pepper"
                }
            },
            "mark": -1,
            "idOutcome": 72
    */

    /**
     * Method handling HTTP GET requests on path "learninganalytics/leaderboard/seriousgame/{idSG}/version/{idVersion}"
     * 
     * @param idSG = an integer id of the SG to retrieve
     * @param idVersion = an integer, id of the version of the SG to retrieve
     * @return a json, representing the leaderboard 
     */
    @GET
    @Path("leaderboard/seriousgame/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLeaderBoard(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) 
    {
        try
        {
            JSONObject infoLeaderboard = new JSONObject();

            LearningOutcomeController loController = new LearningOutcomeController();
            GamePlayController gpController = new GamePlayController();
            PlayerController playerController = new PlayerController();
            StudentController studentController = new StudentController();

            // ************** Get learning Outcomes ************** //

            ArrayList<JSONObject> los = loController.getOutcomeListByGame(idSeriousGame, version);

            ArrayList<JSONObject> gps = gpController.getGameplaysByGame(idSeriousGame, version);

            for (JSONObject lo : los ) {
                ArrayList<JSONObject> listPlayers = new ArrayList<JSONObject>();

                for (JSONObject gp : gps) {

                    int idGP = Integer.parseInt(gp.get("id").toString());
                    ArrayList<JSONObject> scores = gpController.getScores(idGP);
                    int idPlayer = Integer.parseInt(gp.get("idPlayer").toString());
                    JSONObject player = playerController.getPlayerFromId(idPlayer, idSeriousGame, version);
                    String playerName = "Anonymous";
                    JSONObject leader = new JSONObject();

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

                    leader.put("name", playerName);

                    
                    for (JSONObject score : scores) {
                        if (score.get("name").toString().equals(lo.get("name").toString()))
                        {
                            leader.put("score", score.get("value"));
                        }
                    }

                    System.out.println(leader.toString());
                    listPlayers.add(leader);
                }

                Collections.sort(listPlayers, new LeadersCompare());

                infoLeaderboard.put(lo.get("name"), listPlayers);
            }
            return infoLeaderboard.toString();
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