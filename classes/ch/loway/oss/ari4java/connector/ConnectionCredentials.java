
package ch.loway.oss.ari4java.connector;

/**
 * URL and auth creedntials to be used.
 * 
 * @author lenz
 */
public class ConnectionCredentials {

    public String server = "";
    public int port = 1234;
    public String login = "";
    public String password = "";


    /**
     * Info about the Asterisk server.
     * 
     * @param server
     * @param port
     * @param login
     * @param password
     * @return
     */

    public static ConnectionCredentials build( String server, int port, String login, String password ) {
        ConnectionCredentials cc = new ConnectionCredentials();
        cc.server = server;
        cc.port = port;
        cc.login = login;
        cc.password = password;
        return cc;
    }



}

// $Log$
//
