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

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;

import uws.engage.controller.StudentController;
import uws.engage.controller.PlayerController;
import uws.engage.controller.SeriousGameController;
/**
 * Root resource (exposed at "SGaccess" path)
 */
/**
* @apiDefine 1OutsideGameplay Outside the gameplay
*
*/
@Path("SGaccess")
/**
 * This class is the SeriousGameAccess class of the web services, it allows
 * 1. To retrieve the verion to be player by a student
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 *
 */
public class SeriousGameAccessResource {

    /**
     * Method handling HTTP GET requests on path "SGaccess/{idSG}/student/{idStudent}"
     * 
     * @param idSG = an integer id of the SG to play
     * @param idStudent = an integer, id of the student (0 if unauthentificated player)
     * @return an integer version number to play if successful
     */
	@GET
    @Path("/{idSG}/student/{idStudent}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getVersion(@PathParam("idSG") int idSG, @PathParam("idStudent") int idStudent) {
        try
        {
            if (idStudent == 0)
            {
                return "0";
            }
            StudentController stdtController = new StudentController();
            return stdtController.getStudentVersionOfSG(idSG, idStudent) + "";
        }
        catch( Exception e )
        {
            return "error: " + e;
        }
    }    

     /**
     * @api {post} /SGaccess Login Student or Guest
     * @apiDescription A teacher can modify your game’s assessment, thus creating a new version of the game. 
     * Before the player can start the game, you need to check their credential (username and password) 
     * and get the version the teacher decided they would play. You do so by invoking the <code>SGaccess</code> web service. 
     * It will also return a list of caracteristics you need to know about the player 
     * (based on the Player section of the configuration file).
     *
     * @apiName 1PostLogin
     * @apiGroup 1OutsideGameplay
     *
     * @apiVersion 2.0.0
     *
     * @apiParam {json} loginData JSON with three key/value: <ul><li><i>idSG </i>: int, ID of current game 
     * </li><li><i>username </i>: string, username of player, empty for guest login
     * </li><li><i>password </i>: string, password of player, empty for guest login </li></ul>
     *
     * @apiParamExample {json} player login
     *   {
     *     "idSG": 92,
     *     "username": "yaelle",
     *     "password": "password"
     *   }
     * 
     * @apiParamExample {json} guest login
     *   {
     *     "idSG": 92,
     *     "username": "",
     *     "password": ""
     *   }
     *
     * @apiSuccess {json} loginResult JSON with: <ul><li><i>loginSuccess </i>: true if username/password exists
     *       </li><li><i>params </i>: list of information to ask about the player, if any
     *       </li><li><i>version </i>: version of the game to be played by the player
     *       </li><li><i>idPlayer </i>: ID of the player if he/she has played before
     *       </li><li><i>student </i>: basic info about the student logged in </li></ul>
     * @apiSuccessExample {json} Wrong/Guest login, public game
     *  {
     *       "loginSuccess": false,
     *       "params": [
     *           {
     *               "name": "age",
     *               "type": "Int"
     *           },
     *           {
     *               "name": "gender",
     *               "type": "Char"
     *           }
     *       ],
     *       "version": 0
     *   }
     * @apiSuccessExample {json}  Wrong login, non-public game
     *   {  
     *      "loginSuccess": false
     *   }
     * @apiSuccessExample {json}  Correct login, known player
     *   {  
     *           "idPlayer": 2,
     *           "student": {
     *               "id": 9,
     *               "username": "yaelle",
     *               "idSchool": 1,
     *               "dateBirth": "1988-08-17"
     *           },
     *           "loginSuccess": true,
     *           "params": [],
     *           "version": 0
     *   } 
     * @apiSuccessExample {json}  Correct login, new student
     *   {  
     *           "student": {
     *               "id": 1,
     *               "username": "test",
     *               "idSchool": 1,
     *               "dateBirth": "2000-01-01"
     *           },
     *           "loginSuccess": true,
     *           "params":[
     *              {
     *                   "name": "age",
     *                   "type": "Int"
     *              },
     *              {
     *                  "name": "gender",
     *                  "type": "Char"
     *              }
     *          ],
     *          "version": 0
     *   }
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String logonAndGetVersion(String loginParams) {
        try
        {
            JSONObject returnData = new JSONObject();

            JSONObject loginParamsJson=(JSONObject) JSONValue.parse(loginParams);

            String username = loginParamsJson.get("username").toString();
            String password = loginParamsJson.get("password").toString();
            int idSG = Integer.parseInt(loginParamsJson.get("idSG").toString());


            PlayerController playerController = new PlayerController();
            ArrayList<JSONObject> params = new ArrayList<JSONObject>();
          

            StudentController stdtController = new StudentController();
            JSONObject student = stdtController.checkStudentUsernameAndPassword(username, password);

            if (student != null)
            {

                int idStudent = Integer.parseInt(student.get("id").toString());
                int version = stdtController.getStudentVersionOfSG(idSG, idStudent);

                returnData.put("loginSuccess", true);
                returnData.put("student", student);
                returnData.put("version", version);

                int idPlayer = playerController.getPlayerFromIdStudent(idStudent, idSG, version);
                if (idPlayer != 0)
                {
                    returnData.put("idPlayer", idPlayer);
                }
                
                params = playerController.getParametersRequired(idStudent,idSG, version);
                returnData.put("params", params);

                System.out.println(returnData.toString());
            }
            else
            {
                //if the serious game is public
                SeriousGameController sgController = new SeriousGameController();
                JSONObject sg = sgController.getSGById(idSG, 0);
                if ((Boolean) sg.get("public"))
                {
                    returnData.put("loginSuccess", false);
                    returnData.put("version", 0);
                    params = playerController.getParametersRequired(idSG, 0);
                    returnData.put("params", params);
                }
                // else
                else
                {
                    returnData.put("loginSuccess", false);
                }
            }

            return returnData.toString();

        }
        catch( Exception e )
        {
            System.out.println("error : " + e);
            JSONObject error = new JSONObject();
            error.put("error", e);
            return error.toString();
        }
    }    
}