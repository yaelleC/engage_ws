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
     * Method handling HTTP GET requests on path "SGaccess/{idSG}/student/{idStudent}"
     * 
     * @param idSG = an integer id of the SG to play
     * @param idStudent = an integer, id of the student (0 if unauthentificated player)
     * @return an integer version number to play if successful
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