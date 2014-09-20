package uws.assess4me.assessment;

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
/**
 * Root resource (exposed at "SGaccess" path)
 */
@Path("SGaccess")
/**
 * 
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2014.07
 *
 */
public class SeriousGameAccessResource {

	@GET
    @Path("/{idSG}/student/{idStudent}")
    @Produces(MediaType.TEXT_PLAIN)
    public int getVersion(@PathParam("idSG") int idSG, @PathParam("idStudent") int idStudent) {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getStudentVersionOfSG(idSG, idStudent);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    @PUT
    @Path("/{idSG}/school/{idSchool}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int giveAccessToSchool(@PathParam("idSG") int idSG, @PathParam("idSchool") int idSchool)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.giveSGaccessToSchool(idSG, idSchool);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    @PUT
    @Path("/{idSG}/student/{idStudent}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int giveAccessToStudent(@PathParam("idSG") int idSG, int version, @PathParam("idStudent") int idStudent)
    {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.giveSGaccessToStudent(idSG, idStudent, version);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    
}