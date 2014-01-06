package ch.loway.oss.ari4java.tools;


import ch.loway.oss.ari4java.generated.Message;
import ch.loway.oss.ari4java.tools.WsClient.WsClientConnection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base functionality for ARI actions
 * 
 * Provides asynchronous and synchronous methods for forwarding requests
 * to the HTTP or WebSocket server. 
 * 
 * Provides serialize/deserialize interface for JSON encoded objects 
 *
 * @author lenz
 * @author mwalton
 */
public class BaseAriAction {

    /**
     * Delegate for handling asynchronous ARI responses
     */
    public static class AriAsyncHandler<T> implements HttpResponseHandler {

        private final AriCallback<? super T> callback;
        private Class<T> klazz;
        private TypeReference<T> klazzType;

        public AriAsyncHandler(AriCallback<? super T> callback, Class<T> klazz) {
            this.callback = callback;
            this.klazz = klazz;
        }

        public AriAsyncHandler(AriCallback<? super T> callback,
                TypeReference<T> klazzType) {
            this.callback = callback;
        }

        public AriCallback<? super T> getCallback() {
            return callback;
        }

        private void handleResponse(String json) {
            try {
                T result;
                if (Void.class.equals(klazz)) {
                    result = null;
                } else if (klazz != null) {
                    result = deserializeJson(json, klazz);
                } else {
                    result = deserializeJson(json, klazzType);
                }
                this.callback.onSuccess(result);
            } catch (RestException e) {
                this.callback.onFailure(e);
            }
        }

        @Override
        public void onConnect() {
        }

        @Override
        public void onDisconnect() {
        }

        @Override
        public void onSuccess(String response) {
            handleResponse(response);
        }

        @Override
        public void onFailure(Throwable e) {
            this.callback.onFailure(new RestException(e));
        }
    }

    public static class HttpParam {

        public String name = "";
        public String value = "";

        public static HttpParam build(String n, String v) {
            HttpParam p = new HttpParam();
            p.name = n;
            p.value = v;
            return p;
        }

        public static HttpParam build(String n, int v) {
            return build(n, Integer.toString(v));
        }

        public static HttpParam build(String n, long v) {
            return build(n, Long.toString(v));
        }

        public static HttpParam build(String n, boolean v) {
            return build(n, v ? "true" : "false");
        }
    }

//            public AriConnector daddy = null;
//
//        public void configure(AriConnector connector) {
//            daddy = connector;
//        }


    public static class HttpResponse {

        public int code = 0;
        public String description = "";

        public static HttpResponse build(int code, String descr) {
            HttpResponse r = new HttpResponse();
            r.code = code;
            r.description = descr;
            return r;
        }
    }
    private String forcedResponse = null;
    private HttpClient httpClient;
    private WsClient wsClient;
    protected List<HttpParam> lParamQuery;
    protected List<HttpParam> lParamForm;
    protected List<HttpResponse> lE;
    protected String url;
    protected String method;
    protected boolean wsUpgrade = false;
    protected WsClientConnection wsConnection;

    /**
     * Reset contents in preparation for new RPC
     */
    protected synchronized void reset() {
        lParamQuery = new ArrayList<HttpParam>();
        lParamForm = new ArrayList<HttpParam>();
        lE = new ArrayList<HttpResponse>();
        url = null;
        wsUpgrade = false;
    }

    public void forceResponse(String r) {
        forcedResponse = r;
    }

    /**
     * Initiate synchronous HTTP interaction with server 
     * 
     * @return Response from server
     * @throws RestException
     */
    protected synchronized String httpActionSync() throws RestException {
        if (forcedResponse != null) {
            return forcedResponse;
        } else {            
            if (httpClient == null) {
                throw new RestException("HTTP client implementation not set");
            } else {
                return httpClient.httpActionSync(this.url, this.method, this.lParamQuery, this.lParamForm, this.lE);
            }
        }
    }

    /**
     * Initiate asynchronous HTTP or WebSocket interaction with server
     * 
     * @param asyncHandler
     */
    private synchronized void httpActionAsync(AriAsyncHandler<?> asyncHandler) {
        if (forcedResponse != null) {
            asyncHandler.handleResponse(forcedResponse);
        } else if (wsUpgrade) {
            // Websocket connection
            if (wsClient == null) {
                asyncHandler.getCallback().onFailure(new RestException("WebSocket client implementation not set"));
                return;
            }
            if (wsConnection != null) {
                asyncHandler.getCallback().onFailure(new RestException("This action is already connected to a WebSocket"));
                return;
            }
            try {
                wsConnection = wsClient.connect(asyncHandler, this.url, this.lParamQuery);
            } catch (RestException e) {
                asyncHandler.getCallback().onFailure(e);
            }
        } else if (httpClient == null) {
            asyncHandler.getCallback().onFailure(new RestException("HTTP client implementation not set"));
        } else {
            try {
                httpClient.httpActionAsync(this.url, this.method, this.lParamQuery, this.lParamForm, this.lE, asyncHandler);
            } catch (RestException e) {
                asyncHandler.getCallback().onFailure(e);
            }
        }
    }

    // Different styled asynchronous methods
    protected void httpActionAsync(AriCallback<Void> callback) {
        httpActionAsync(new AriAsyncHandler<Void>(callback, Void.class));
    }

    protected <S, T extends S> void httpActionAsync(AriCallback<S> callback, Class<T> klazz) {
        httpActionAsync(new AriAsyncHandler<T>(callback, klazz));
    }

    protected <S, T extends S> void httpActionAsync(AriCallback<S> callback, TypeReference<T> klazzType) {
        httpActionAsync(new AriAsyncHandler<T>(callback, klazzType));
    }

//    private String httpActionImpl(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpResponse> errors) throws RestException {
//        return daddy.performHttp(uri, method, parametersQuery, parametersForm, errors);
//    }

    /**
     * Deserialize a type
     *
     * @param json
     * @param klazz
     * @return Deserialized  type
     */
    public static <T> T deserializeJson(String json, Class<T> klazz) throws RestException {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, klazz);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            throw new RestException("Decoding JSON: " + e.getMessage());
        }
    }

    /**
     * Deserialize a list
     * 
     * @param json
     * @param klazzType
     * @return Deserialized list
     */
    public static <T> T deserializeJson(String json, TypeReference<T> klazzType) throws RestException {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, klazzType);
        } catch (IOException e) {
            throw new RestException("Decoding JSON list: " + e.toString());
        }

    }

    /**
     * Deserialize the event.
     *
     * @param json
     * @param klazz
     * @return The event deserialized.
     * @throws RestException
     */
    public static Message deserializeEvent(String json, Class<?> klazz) throws RestException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (Message) mapper.readValue(json, klazz);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            throw new RestException("Decoding JSON event: " + e.toString());
        }
    }

    /**
     * Close the WebSocket connection 
     * 
     * @throws RestException
     */
    public synchronized void close() throws RestException {
        System.out.println( "Closing connection" );
        if (wsUpgrade && wsConnection == null) {
            throw new RestException("No WebSocket connection is open");
        }
        wsConnection.disconnect();
        wsConnection = null;
    }

    public synchronized void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public synchronized void setWsClient(WsClient wsClient) {
        this.wsClient = wsClient;
    }
}

//

