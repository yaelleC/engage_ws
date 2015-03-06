package uws.engage.assessment;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("crossdomain.xml")

public class CrossDomainResource {

	@GET
    @Produces(MediaType.APPLICATION_XML)
    public String getCrossDomain() 
    {
        try
        {
            String crossdomain = "<?xml version=\"1.0\"?>" +
									"<cross-domain-policy>" +
									"<allow-access-from domain=\"*\"/>" +
									"<allow-http-request-headers-from domain=\"*\" headers=\"*\"/>" +
									"</cross-domain-policy>";
			return crossdomain;
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

}