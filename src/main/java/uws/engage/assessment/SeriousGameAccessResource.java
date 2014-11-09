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
}