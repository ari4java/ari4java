package ch.loway.oss.ari4java.tools;

import java.util.List;

/**
 * Interface to pluggable WebSocket client implementation
 *
 * @author mwalton
 */
public interface WsClient {

    public interface WsClientConnection {
        void disconnect() throws RestException;
    }

    WsClientConnection connect(HttpResponseHandler callback,
                               String url,
                               List<HttpParam> lParamQuery) throws RestException;

    void destroy();

}
