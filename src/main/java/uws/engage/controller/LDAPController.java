package uws.engage.controller;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;
import javax.naming.NamingEnumeration;

/**
 * This class allows LDAP access
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 3.0
 *
 */
public class LDAPController {

    /**
     * General useful variables (names of databases, fields...)
     */
    private General g;

    /**
     * Creates an LDAP controller
     * @throws Exception
     */
    public LDAPController( ) throws Exception
    {
        g = new General();
        // in debug mode = trace
        if (g.DEBUG) { System.out.println("### LDAPController.LDAPController() ###"); }
    }

    public boolean checkLDAPUsernameAndPassword(String username, String password) {
        if (g.DEBUG) {
            System.out.println("*** checkLDAPUsernameAndPassword "+username+"-"+password+" ***");
        }
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();

            env.put(Context.INITIAL_CONTEXT_FACTORY, g.LDAP_INITIAL_CONTEXT_FACTORY);
            env.put(Context.SECURITY_AUTHENTICATION, g.LDAP_SECURITY_AUTHENTICATION);
            env.put(Context.PROVIDER_URL, g.LDAP_PROVIDER_URL);

            // The value of Context.SECURITY_PRINCIPAL must be the logon username with the domain name
            env.put(Context.SECURITY_PRINCIPAL, username);
            env.put(Context.SECURITY_CREDENTIALS, password);

            DirContext ctx;

            try {
                // Authenticate the logon user
                ctx = new InitialDirContext(env);
                /**
                 * Once the above line was executed successfully, the user is said to be
                 * authenticated and the InitialDirContext object will be created.
                 */
                if (g.DEBUG) {
                    System.out.println("*** user " + username + " found ***");
                }
                ctx.close();
                return true;
            } catch (Exception ex) {
                // Authentication failed, just check on the exception and do something about it.
                System.err.println("ERROR (checkLDAPUsernameAndPassword1): ");
                System.err.println(ex);
                return false;
            }
        } catch (Exception e) {
            System.err.println("ERROR (checkLDAPUsernameAndPassword2): " + e.getMessage());
            return false;
        }
    }
}