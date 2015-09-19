/*
 * 
 */
package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.tools.http.NettyHttpClient;
import java.net.URISyntaxException;

/**
 * This class is a placeholder for convenience factory methods.
 * 
 * @author lenz
 */
public class AriFactory {

    /**
     * Your default HTTP connector through Netty (without app).
     * 
     * @param uri
     * @param login
     * @param pass
     * @param version
     * @return a ready-to-use connector.
     * @throws ARIException
     * @throws URISyntaxException 
     */
    
    
    public static ARI nettyHttp(String uri, String login, String pass, AriVersion version) throws ARIException, URISyntaxException {
        return nettyHttp(uri, login, pass, version, "");
    }
    
    /**
     * This connects to an application.
     * 
     * @param uri
     * @param login
     * @param pass
     * @param version
     * @param app
     * @return your ready-to-use connector.
     * @throws ARIException
     * @throws URISyntaxException 
     */

    public static ARI nettyHttp(String uri, String login, String pass, AriVersion version, String app) throws ARIException, URISyntaxException {
        ARI ari = new ARI();
        ari.setAppName(app);
        NettyHttpClient hc = new NettyHttpClient();

        ari.setHttpClient(hc);
        ari.setWsClient(hc);

        ari.setVersion(version);

        hc.initialize(uri, login, pass);
        return ari;
    }
    
}
