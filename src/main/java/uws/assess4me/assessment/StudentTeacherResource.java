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
import org.json.simple.JSONValue;

import java.util.ArrayList;

/**
 * Root resource (exposed at "developer" path)
 */
@Path("studentTeacher")
/**
 * This class allows CRUD actions on a developer
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 0.1
 *
 */
public class StudentTeacherResource {

	
    @GET
    @Path("/{idTeacher}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStudentsOfTeacher( @PathParam("idTeacher") int idTeacher ) {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getStudentsOfTeacher(idTeacher).toString();
        }
        catch( Exception e )
        {
            return null;
        }
    }

    @GET
    @Path("/{idTeacher}/class/{className}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStudentsOfTeacher( @PathParam("idTeacher") int idTeacher, 
                                            @PathParam("className") String className ) {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getStudentsOfTeacherByClass(idTeacher, className).toString();
        }
        catch( Exception e )
        {
            return null;
        }
    }

    @PUT
    @Path("/{idTeacher}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int addStudentToTeacher(@PathParam("idTeacher") int idTeacher, int idStudent)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.addStudentToTeacher(idStudent, idTeacher);
        }
        catch( Exception e )
        {
            return -2;
        }
    }

    @PUT
    @Path("/{idTeacher}/class/{className}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int addStudentToTeacherByClass(@PathParam("idTeacher") int idTeacher, 
                                            @PathParam("className") String className, 
                                                int idStudent)
    {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
           return engage.addStudentToTeacher(idStudent, idTeacher, className);
        }
        catch( Exception e )
        {
            return -2;
        }
    }


    @DELETE
    @Path("/{idTeacher}/student/{idStudent}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int removeStudentFromTeacher(@PathParam("idTeacher") int idTeacher, 
                                            @PathParam("idStudent") int idStudent)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.removeStudentFromTeacher(idStudent, idTeacher);
        }
        catch( Exception e )
        {
            return -2;
        }
    }

    @DELETE
    @Path("/{idTeacher}/student/{idStudent}/class/{className}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int removeStudentFromTeacher(@PathParam("idTeacher") int idTeacher, 
                                            @PathParam("className") String className, 
                                                @PathParam("idStudent") int idStudent)
    {
        uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.removeStudentFromTeacherByClass(idStudent, idTeacher, className);
        }
        catch( Exception e )
        {
            return -2;
        }
    }
}