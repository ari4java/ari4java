package ch.loway.oss.ari4java.connector;

import ch.loway.oss.ari4java.connector.netty.BlockingHttpClient;
import ch.loway.oss.ari4java.connector.netty.BlockingHttpClient.HttpClientHandler;
import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.BaseAriAction.HttpParam;
import ch.loway.oss.ari4java.tools.RestException;
import io.netty.handler.codec.http.HttpMethod;
import java.net.URI;
import java.util.List;

/**
 * An ARI connector based on the Netty.io library.
 * 
 * @author lenz
 */
public class NettyConnector extends AriConnector {

    @Override
    public void openConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void closeConnection() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String performHttp(String urlFragment, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<BaseAriAction.HttpResponse> lErrors) throws RestException {
        String url = credentials.server + ":" + credentials.port + "/ari" + urlFragment;

        HttpMethod nettyMethod = HttpMethod.POST;

        if ( method.equalsIgnoreCase("GET") ) {
            nettyMethod = HttpMethod.GET;
        } else
        if ( method.equalsIgnoreCase("POST") ) {
            nettyMethod = HttpMethod.POST;
        } else
        if ( method.equalsIgnoreCase("DELETE") ) {
            nettyMethod = HttpMethod.DELETE;
        } else
        { 
            throw new UnsupportedOperationException("Would not know hhow to handle " + method );
        }

        BlockingHttpClient client = new BlockingHttpClient();
        try {
            HttpClientHandler clientHdlr = client.httpRequestViaNetty( new URI(url), nettyMethod, credentials.login, credentials.password,  parametersQuery );

            if ( clientHdlr.responseCode == 200 ) {
                return clientHdlr.sb.toString();
            } else {

                for ( BaseAriAction.HttpResponse r: lErrors) {
                    if ( r.code == clientHdlr.responseCode ) {
                        throw new RestException( r.description + " [" + r.code + "]" );
                    }
                }

                throw new RestException("Unknown error response " + clientHdlr.responseCode );

            }

        } catch ( Throwable t ) {
            throw new RestException("Failed access");
        }

    }

    
    
}
