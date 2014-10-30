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
/**
 * Root resource (exposed at "seriousgame" path)
 */
@Path("seriousgame")
/**
 * This class allows CRUD actions on a seriousgame
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2014.07
 *
 */
public class SeriousGameResource {

// TODO: To decomment when Xtext project is updated

	@GET
    @Path("/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSeriousGame(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            return engage.getConfigFile(idSeriousGame, version).toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String createSeriousGame(String configFile)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            sendEmail(configFile, "EngAGe - CreateSeriousGame");
            return engage.createSG(configFile) + "";
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }

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

    @PUT
    @Path("/checkAndEmail/{email}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String checkDSLandEmail(String configFile, @PathParam("email") String emailUser)
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
            String text = "from: " + emailUser + "\n\n" + errors.toString() + "\n\n" + configFile;
            sendEmail(text, "EngAGe - user needs help with CF");

            return errors.toString();
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int updateSeriousGame(String seriousgame)
    {
    	uws.chaudy.generator.Engage engage = new uws.chaudy.generator.Engage();
        try
        {
            JSONObject sg =(JSONObject) JSONValue.parse(seriousgame);
            return engage.updateSG(sg);
        }
        catch( Exception e )
        {
            return -10;
        }
    }

    /**
     * Send the email via SMTP using StartTLS and SSL
     */
    private static void sendEmail(String text, String subject) {
  
        // Create all the needed properties
        Properties connectionProperties = new Properties();
        // SMTP host
        connectionProperties.put("mail.smtp.host", "smtp.gmail.com");
        // Is authentication enabled
        connectionProperties.put("mail.smtp.auth", "true");
        // Is StartTLS enabled
        connectionProperties.put("mail.smtp.starttls.enable", "true");
        // SSL Port
        // connectionProperties.put("mail.smtp.socketFactory.port", "465");
        // SSL Socket Factory class
        // connectionProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // SMTP port, the same as SSL port :)
        connectionProperties.put("mail.smtp.port", "587");
         
        System.out.print("Creating the session...");
         
        // Create the session
        Session session = Session.getDefaultInstance(connectionProperties,
                new javax.mail.Authenticator() {    // Define the authenticator
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("engage.assess@gmail.com","Engage2013");
                    }
                });
         
        System.out.println("done!");
         
        // Create and send the message
        try {
            // Create the message
            Message message = new MimeMessage(session);
            // Set sender
            message.setFrom(new InternetAddress("engage.assess@gmail.com"));
            // Set the recipients
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("yaelle.chaudy@uws.ac.uk"));
            // Set message subject
            message.setSubject(subject);
            // Set message text
            message.setText(text);
             
            System.out.print("Sending message...");
            // Send the message
            Transport.send(message);
             
            System.out.println("done!");
             
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
}