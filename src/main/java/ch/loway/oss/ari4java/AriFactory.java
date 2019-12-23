package ch.loway.oss.ari4java;

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
     * @param uri     uri
     * @param login   login
     * @param pass    pass
     * @param version version
     * @return a ready-to-use connector.
     * @throws URISyntaxException when error
     */
    public static ARI nettyHttp(String uri, String login, String pass, AriVersion version) throws URISyntaxException {
        return nettyHttp(uri, login, pass, version, "");
    }

    /**
     * This connects to an application.
     *
     * @param uri     uri
     * @param login   login
     * @param pass    pass
     * @param version version
     * @param app     app
     * @return your ready-to-use connector.
     * @throws URISyntaxException when error
     */
    public static ARI nettyHttp(String uri, String login, String pass, AriVersion version, String app) throws URISyntaxException {
        if (AriVersion.IM_FEELING_LUCKY.equals(version)) {
            throw new UnsupportedOperationException("IM_FEELING_LUCKY not a valid option here");
        }
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
