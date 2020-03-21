package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.tools.http.NettyHttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URISyntaxException;

/**
 * This class is a placeholder for convenience factory methods.
 *
 * @author lenz
 */
public class AriFactory {

    static NettyHttpClient nettyHttpClient = null;

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
        return nettyHttp(uri, login, pass, version, app, true);
    }

    /**
     * This connects to an application.
     * If the version is set as IM_FEELING_LUCKY the AriFactory will determine the version by 1st connecting
     * to the server and requesting the resources.json to extract the version number.
     *
     * @param uri            uri
     * @param login          login
     * @param pass           pass
     * @param version        version
     * @param app            app
     * @param testConnection testConnection
     * @return your ready-to-use connector.
     * @throws URISyntaxException when error
     */
    public static ARI nettyHttp(String uri, String login, String pass, AriVersion version, String app, boolean testConnection) throws URISyntaxException {
        NettyHttpClient client;
        if (nettyHttpClient != null) {
            client = nettyHttpClient;
        } else {
            client = new NettyHttpClient();
            client.initialize(uri, login, pass);
        }
        ARI ari = new ARI();
        ari.setAppName(app);
        ari.setHttpClient(client);
        ari.setWsClient(client);
        if (AriVersion.IM_FEELING_LUCKY.equals(version)) {
            ari.setVersion(detectAriVersion(client));
        } else {
            ari.setVersion(version);
        }
        try {
            int majorVer = Integer.parseInt(ari.getVersion().version().split("\\.")[0]);
            // ping was added in version 5 (Asterisk 16) and back ported to some... I'm only including 1.10.2 (Asterisk 13) from the back port...
            if (testConnection && (majorVer >= 5 || AriVersion.ARI_1_10_2.equals(version))) {
                ari.asterisk().ping();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to test connection: " + e.getMessage(), e);
        }
        return ari;
    }

    /**
     * Connect and detect the current ARI version.
     * If the ARI version is not supported,
     * will raise an exception as we have no bindings for it.
     *
     * @param client an instance of the NettyHttpClient
     * @return the version of your server
     * @throws RuntimeException if the version is not supported
     */
    protected static AriVersion detectAriVersion(NettyHttpClient client) {
        try {
            String response = client.httpActionSync("/api-docs/resources.json", "GET", null, null, null);
            if (response != null && !response.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.reader().readTree(response);
                if (jsonNode.has("apiVersion")) {
                    return AriVersion.fromVersionString(jsonNode.get("apiVersion").asText());
                }
            }
            throw new RuntimeException("Could find apiVersion");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
