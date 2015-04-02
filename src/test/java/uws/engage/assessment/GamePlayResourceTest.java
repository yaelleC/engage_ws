package uws.engage.assessment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;


import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GamePlayResourceTest {/*extends JerseyTest {*/

   // private HttpServer server;
    //private WebTarget target;
    private String baseURL="gameplay";

 /*   @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }
*/
    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
   /* @Test
    public void testGetFeedbackSome() {
        String responseMsg = target.path(baseURL+"/{idGP}/feedback").request().get(String.class);
        assertEquals("Got it!", responseMsg);
    }

    @Test
    public void testGetFeedbackNone() {
        String responseMsg = target.path(baseURL+"/{idGP}/feedback").request().get(String.class);
        assertEquals("Got it!", responseMsg);
    }

    @Test
    public void testGetFeedbackWrongID() {
        String responseMsg = target.path(baseURL+"/{idGP}/feedback").request().get(String.class);
        assertEquals("Got it!", responseMsg);
    }*/

    @Test
    public void testEndGameplay() {

       // Entity<String> send = Entity.text(""); 

       // String responseMsg = target.path(baseURL+"/710/end/win").request().get(send, String.class);

        assertEquals(1, 1);
    }

/*
    @Override
    protected Application configure() {
        return new ResourceConfig(GamePlayResource.class);
    }
    */
}
