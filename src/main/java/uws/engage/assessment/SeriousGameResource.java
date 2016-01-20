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
import uws.engage.controller.StudentController;

/**
 * Root resource (exposed at "seriousgame" path)
 */
@Path("seriousgame")
/**
 * This class is the SeriousGame class of the web services, it allows
 * 1. To retrieve a serious game configuration (json format)
 * 2. create an SG configuration from a config file (DSL format)
 * 3. Check a DSL config file grammar
 * 4. update an SG configuration from a config file (JSON format)
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class SeriousGameResource {
    /**
     * Method handling HTTP GET requests on path "seriousgame/{idSG}/version/{idVersion}"
     * 
     * @param idSG = an integer id of the SG to retrieve
     * @param idVersion = an integer, id of the version of the SG to retrieve
     * @return a json, representing the SG assessment configuration
     */
    @GET
    @Path("/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSeriousGame(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) 
    {
        try
        {
            SeriousGameController sgController = new SeriousGameController();
            JSONObject cf = sgController.getConfigFile(idSeriousGame, version);
            sgController.finalize();
            return cf.toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    /**
     * @api {get} /seriousgame/info/:idSG/version/:version Get Game Data
     * @apiDescription Because a teacher might modify some of the game information (description, age range...), 
     * you may need to access the game data at some point in your game (e.g. an "about" window). <br/>
     * To retrieve this, you need to call this web service
     *
     * @apiName 2GetGameInfo
     * @apiGroup 1OutsideGameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idSG ID of the current game
     * @apiParam {Number} version version number of the current game
     *
     * @apiSuccess {json} gameData A json describing the current game (name, description... as defined in the configuration file)
     * @apiSuccessExample {json} Example of response
     *  "seriousGame": {
     *       "genre": "Runner",
     *       "idDeveloper": 1,
     *       "ageMin": 10, 
     *       "ageMax": 99,
     *       "description": "This is a mini game that trains you to identify the countries that form the European Union",
     *       "subject": "geography",
     *       "name": "EU mouse",
     *       "public": true,
     *       "lang": "EN",
     *       "country": "UK"
     *  }
     */
    @GET
    @Path("info/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSeriousGameInfo(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) 
    {
        try
        {
            SeriousGameController sgController = new SeriousGameController();
            String sg = sgController.getConfigFile(idSeriousGame, version).get("seriousGame").toString();
            sgController.finalize();
            return sg;
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    /**
     * @api {get} /seriousgame/:idSG/version/:version/actionParam/{action} Get Parameters of action
     * @apiDescription Because a teacher might modify the possible values for an action's parameters, 
     * you may need to access the list of possible values at some point in your game 
     * (e.g. when you generate enemies, or words to translate). <br/>
     * To retrieve this, you need to call this web service
     *
     * @apiName 3GetParametersOfAction
     * @apiGroup 2Gameplay
     *
     * @apiVersion 2.0.0
     * 
     * @apiParam {Number} idSG ID of the current game
     * @apiParam {Number} version version number of the current game
     * @apiParam {String} action to retrieve the values from
     *
     * @apiSuccess {json} actionParams A json listing the possible values for the action's parameters
     * @apiSuccessExample {json} Example of response for action 'translate'
     *  [
     *       { "wordInFrench": "poire", "wordInEnglish": "pear" },
     *       { "wordInFrench": "pomme", "wordInEnglish": "apple" },
     *       { "wordInFrench": "fraise", "wordInEnglish": "strawberry" },
     *  ]
     */
    @GET
    @Path("{idSG}/version/{version}/actionParam/{action}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getActionParams(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("version") int version, @PathParam("action") String action) 
    {
        try
        {
            ArrayList<JSONObject> values = new ArrayList<JSONObject>();
            SeriousGameController sgController = new SeriousGameController();
            JSONObject gameJSON = sgController.getConfigFile(idSeriousGame, version);
            JSONObject evidenceModel = (JSONObject) gameJSON.get("evidenceModel");
            JSONObject actionJSON = (JSONObject) evidenceModel.get(action);


            ArrayList<JSONObject> reactions = (ArrayList<JSONObject>) actionJSON.get("reactions");
            for (JSONObject reaction : reactions)
            {
                if (reaction.get("values") != null)
                {
                    for (JSONObject val : (ArrayList<JSONObject>) reaction.get("values"))
                    {
                        values.add(val);
                    }
                }
            }

            sgController.finalize();
            return values.toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    /**
     * Method handling HTTP PUT requests on path "seriousgame/"
     * 
     * @param configFile = the configuration file (DSL format) of the SG to create
     * @return the id of SG created in the database if successful
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String createSeriousGame(String configFile)
    {
    	try
        {
            uws.engage.dsl.generator.Parser engageParser = new uws.engage.dsl.generator.Parser();
            JSONObject configFileJSON = engageParser.getJSONfromDSL(configFile);

            SeriousGameController sgController = new SeriousGameController();
            int idSG = sgController.createSG(configFileJSON);

            // give access to test user
            StudentController stdController = new StudentController();
            stdController.giveSGaccessToStudent(idSG, 1, 0);

            sgController.finalize();
            stdController.finalize();
            return  idSG + "";
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }

    /**
     * Method handling HTTP POST requests on path "seriousgame/"
     * 
     * @param configFile = the configuration file (DSL format) of the SG to create
     * @return the id of SG created in the database if successful
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String createSeriousGame2(String configFile)
    {
        try
        {
            uws.engage.dsl.generator.Parser engageParser = new uws.engage.dsl.generator.Parser();
            JSONObject configFileJSON = engageParser.getJSONfromDSL(configFile);

            SeriousGameController sgController = new SeriousGameController();
            int idSG = sgController.createSG(configFileJSON);

            // give access to test user
            StudentController stdController = new StudentController();
            stdController.giveSGaccessToStudent(idSG, 1, 0);

            sgController.finalize();
            stdController.finalize();
            return  idSG + "";
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }


    /**
     * Method handling HTTP POST requests on path "seriousgame/"
     * 
     * @param configFile = the configuration file (DSL format) of the SG to create
     * @return the id of SG created in the database if successful
     */
    @POST
    @Path("/{idSG}/createVersion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateSeriousGame(String configFile, @PathParam("idSG") int idSG)
    {
        try
        {
            SeriousGameController sgController = new SeriousGameController();

            JSONObject configFileJSON = (JSONObject) JSONValue.parse(configFile);
            JSONObject seriousgameBlock = (JSONObject) configFileJSON.get("seriousGame");

            // add idSG in JSON
            seriousgameBlock.put("id", idSG);
            // add version in JSON
            int newVersion = sgController.getNewVersion(idSG);
            seriousgameBlock.put("version", newVersion);
            System.out.println(newVersion);

            // modify block in JSON
            configFileJSON.remove("seriousGame");
            configFileJSON.put("seriousGame", seriousgameBlock);

            int idSGCreated = sgController.createSG(configFileJSON);

            sgController.finalize();

            if (idSGCreated == -1)
            {                
                return "{\"error\" : \"The game id does not correspond to an existing game in the database.\"}";
            }

            if (idSG == idSGCreated)
            {
                return configFileJSON.toString();
            }
            else
            {
                return "{\"error\" : \"error while creating new version of the game.\"}";
            }
        }
        catch( Exception e )
        {
            return "{\"error\" : \"error while creating new version of the game.\"}";
        }
    }

    /**
     * Method handling HTTP PUT requests on path "seriousgame/check"
     * 
     * @param configFile = the SG configuration file (DSL format)
     * @return an array of errors found in the grammar (JSON format)
     */
    @PUT
    @Path("/check")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String checkDSL(String configFile)
    {
        uws.engage.dsl.generator.Parser engageParser = new uws.engage.dsl.generator.Parser();
        try
        {
            ArrayList<JSONObject> errors = new ArrayList<JSONObject>();
            uws.engage.dsl.generator.ParseResult result = engageParser.parse(configFile);
            if (!result.issues.isEmpty()) {
               for (Issue issue : result.issues) {
                    JSONObject errorLog = new JSONObject(); 
                    errorLog.put("line", issue.getLineNumber());
                    errorLog.put("offset", issue.getOffset());
                    errorLog.put("message", issue.getMessage());
                    errors.add(errorLog);
                }
            }
            return errors.toString();
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }

    /**
     * Method handling HTTP POST requests on path "seriousgame/check"
     * 
     * @param configFile = the SG configuration file (DSL format)
     * @return an array of errors found in the grammar (JSON format)
     */
    @POST
    @Path("/check")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String checkDSL2(String configFile)
    {
        uws.engage.dsl.generator.Parser engageParser = new uws.engage.dsl.generator.Parser();
        try
        {
            ArrayList<JSONObject> errors = new ArrayList<JSONObject>();
            uws.engage.dsl.generator.ParseResult result = engageParser.parse(configFile);
            if (!result.issues.isEmpty()) {
               for (Issue issue : result.issues) {
                    JSONObject errorLog = new JSONObject(); 
                    errorLog.put("line", issue.getLineNumber());
                    errorLog.put("offset", issue.getOffset());
                    errorLog.put("message", issue.getMessage());
                    errors.add(errorLog);
                }
            }
            return errors.toString();
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }

    /**
     * Method handling HTTP POST requests on path "seriousgame/"
     * 
     * @param seriousgame = the configuration file (JSON format) of SG to update
     * @return 1 if successful
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateSeriousGame(String seriousGameCF)
    {
    	try
        {
            JSONObject configFileJSON=(JSONObject) JSONValue.parse(seriousGameCF);
            SeriousGameController sgController = new SeriousGameController();
            int idSG = sgController.createSG(configFileJSON);

            sgController.finalize();
            return idSG + "";
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }

     /**
     * Method handling HTTP POST requests on path "seriousgame/player/{idSG}/version/{idVersion}"
     * 
     * @param seriousgame = the configuration file (JSON format) of SG to update
     * @return 1 if successful
     */
    @POST
    @Path("player/{idSG}/version/{idVersion}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updatePlayerSeriousGame(String playerInfo)
    {
        try
        {

            String headers = loginParams.split("\\{")[0];
           
            if (headers.trim().length() > 0)
            {
                loginParams = loginParams.replace(headers, " ");
            }

            SeriousGameController sgController = new SeriousGameController();

            JSONObject returnData = new JSONObject();
            JSONObject loginParamsJson=(JSONObject) JSONValue.parse(loginParams);

            String username = loginParamsJson.get("username").toString();
            String password = loginParamsJson.get("password").toString();

            PlayerController playerC = new PlayerController();

            // call updatePlayer
            playerC.updatePlayer(idSG, int version, int idPlayer, ArrayList<JSONObject> params)

            playerC.finalize();
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }
}