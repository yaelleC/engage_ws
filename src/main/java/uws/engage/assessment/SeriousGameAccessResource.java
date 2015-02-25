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

import uws.engage.controller.StudentController;

/**
 * Root resource (exposed at "SGaccess" path)
 */
@Path("SGaccess")
/**
 * This class is the SeriousGameAccess class of the web services, it allows
 * 1. To retrieve the verion to be player by a student
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 1.0
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

            StudentController stdtController = new StudentController();
            JSONObject student = stdtController.checkStudentUsernameAndPassword(username, password);

            if (student != null)
            {

                int idStudent = Integer.parseInt(student.get("id").toString());
                int version = stdtController.getStudentVersionOfSG(idSG, idStudent);

                System.out.println("version : " + version);

                returnData.put("loginSuccess", true);
                returnData.put("student", student);
                returnData.put("version", version);

                System.out.println(returnData.toString());
            }
            else
            {
                returnData.put("loginSuccess", false);
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