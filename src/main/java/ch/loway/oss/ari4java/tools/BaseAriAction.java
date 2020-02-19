package ch.loway.oss.ari4java.tools;

import ch.loway.oss.ari4java.generated.models.Message;
import ch.loway.oss.ari4java.tools.WsClient.WsClientConnection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Base functionality for ARI actions
 * <p>
 * Provides asynchronous and synchronous methods for forwarding requests to the
 * HTTP or WebSocket server.
 * <p>
 * Provides serialize/deserialize interface for JSON encoded objects
 *
 * @author lenz
 * @author mwalton
 */
public class BaseAriAction {

    private static Logger logger = LoggerFactory.getLogger(BaseAriAction.class);

    public static class AriRequest {
        private List<HttpParam> queryParams = new ArrayList<>();
        private Map<String, Object> bodyFields = new HashMap<>();
        private List<HttpResponse> lE = new ArrayList<>();
        private String url;
        private String method;
        private boolean wsUpgrade = false;

        public AriRequest(String url, String method) {
            this.url = url;
            this.method = method;
        }

        public void addParamQuery(HttpParam param) {
            queryParams.add(param);
        }

        public void setBodyField(String field, Object value) {
            if ("fields".equals(field) && value instanceof Map) {
                ArrayList<Map<String, String>> list = new ArrayList<>();
                Iterator<Map.Entry<String, String>> entryIterator = ((Map<String, String>) value).entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, String> item = entryIterator.next();
                    Map<String, String> f = new HashMap<>();
                    f.put("attribute", item.getKey());
                    f.put("value", item.getValue());
                    list.add(f);
                }
                bodyFields.put(field, list);
            } else {
                bodyFields.put(field, value);
            }
        }

        public void addErrorResponse(HttpResponse res) {
            lE.add(res);
        }

        public void setWsUpgrade(boolean wsUpgrade) {
            this.wsUpgrade = wsUpgrade;
        }
    }

    // Shared ObjectMapper
    private static final ObjectMapper mapper = new ObjectMapper();

    private String forcedResponse = null;
    private HttpClient httpClient;
    private WsClient wsClient;
    protected WsClientConnection wsConnection;
    private List<BaseAriAction> liveActionList;

    /**
     * Exception safe way to encode url parts
     *
     * @param urlPart the part to encode
     * @return a encoded string
     * @see ARIEncoder#ENCODING
     */
    protected String encodeUrlPart(String urlPart) {
        return ARIEncoder.encodeUrl(urlPart);
    }

    /**
     * Initiate synchronous HTTP interaction with server
     *
     * @param request the request object
     * @return Response from server
     * @throws RestException when error
     */
    protected synchronized String httpActionSync(AriRequest request) throws RestException {
        if (forcedResponse != null) {
            return forcedResponse;
        } else {
            if (httpClient == null) {
                throw new RestException("HTTP client implementation not set");
            } else {
                return httpClient.httpActionSync(request.url, request.method, request.queryParams, serializeToJson(request.bodyFields), request.lE);
            }
        }
    }

    /**
     * Initiate synchronous HTTP interaction with server
     *
     * @param request the request object
     * @return Response from server
     * @throws RestException when error
     */
    protected synchronized byte[] httpActionSyncAsBytes(AriRequest request) throws RestException {
        if (httpClient == null) {
            throw new RestException("HTTP client implementation not set");
        } else {
            return httpClient.httpActionSyncAsBytes(request.url, request.method, request.queryParams, serializeToJson(request.bodyFields), request.lE);
        }
    }

    /**
     * Initiate asynchronous HTTP or WebSocket interaction with server
     *
     * @param request      the request object
     * @param asyncHandler the handler
     */
    private synchronized void httpActionAsync(AriRequest request, AriAsyncHandler<?> asyncHandler) {
        if (forcedResponse != null) {
            asyncHandler.handleResponse(forcedResponse);
        } else if (request.wsUpgrade) {
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
                wsConnection = wsClient.connect(asyncHandler, request.url, request.queryParams);
                liveActionList.add(this);
            } catch (RestException e) {
                asyncHandler.getCallback().onFailure(e);
            }
        } else if (httpClient == null) {
            asyncHandler.getCallback().onFailure(new RestException("HTTP client implementation not set"));
        } else {
            try {
                boolean binary = byte[].class.equals(asyncHandler.getType());
                httpClient.httpActionAsync(request.url, request.method, request.queryParams, serializeToJson(request.bodyFields), request.lE, asyncHandler, binary);
            } catch (RestException e) {
                asyncHandler.getCallback().onFailure(e);
            }
        }
    }

    // Different styled asynchronous methods
    protected void httpActionAsync(AriRequest request, AriCallback<Void> callback) {
        httpActionAsync(request, new AriAsyncHandler<Void>(callback, Void.class));
    }

    protected <S, T extends S> void httpActionAsync(AriRequest request, AriCallback<S> callback, Class<T> klazz) {
        httpActionAsync(request, new AriAsyncHandler<T>(callback, klazz));
    }

    protected <A, C extends A> void httpActionAsync(
            AriRequest request, AriCallback<List<A>> callback, TypeReference<List<C>> klazzType) {
        httpActionAsync(request, new AriAsyncHandler(callback, klazzType));
    }

    /**
     * Serialize an object to json
     *
     * @param obj the Object
     * @return String
     */
    public static String serializeToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map && ((Map) obj).size() == 0) {
            return null;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Encoding JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Deserialize a type
     *
     * @param json  the json string
     * @param klazz the class to deserialize to
     * @param <T>   the class to deserialize to
     * @return Deserialized type
     * @throws RestException when fail to deserialize
     */
    public static <T> T deserializeJson(String json, Class<T> klazz) throws RestException {
        try {
            return mapper.readValue(json, klazz);
        } catch (IOException e) {
            throw new RestException("Decoding JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Deserialize a list
     *
     * @param json      the json string
     * @param klazzType the class to deserialize to
     * @param <T>       the class to deserialize to
     * @return The deserialized list
     * @throws RestException when fail to deserialize
     */
    public static <T> T deserializeJson(String json, TypeReference<T> klazzType) throws RestException {
        try {
            return mapper.readValue(json, klazzType);
        } catch (IOException e) {
            throw new RestException("Decoding JSON list: " + e.getMessage(), e);
        }
    }

    /**
     * Deserialize an object as a list of interface. Class erasure in Java plain
     * sucks, I tell ya.
     * <p>
     * In theory we should be safe given the condition that A extends C. I hope
     * at least. This is bug #17 - Avoid Lists of ? extends something
     *
     * @param <A>             The abstract type for members of the list.
     * @param <C>             The concrete type for members of the list.
     * @param json            Data in
     * @param refConcreteType The reference concrete type, should be a List&lt;C&gt;
     * @return a list of A's
     * @throws RestException when fail to deserialize
     */
    public static <A, C extends A> List<A> deserializeJsonAsAbstractList(String json, TypeReference<List<C>> refConcreteType) throws RestException {
        try {
            List<C> lC = mapper.readValue(json, refConcreteType);
            List<A> lA = (List<A>) lC;
            return lA;
        } catch (IOException e) {
            throw new RestException("Decoding JSON list: " + e.getMessage(), e);
        }
    }

    /**
     * Deserialize the event.
     *
     * @param json  the json string
     * @param klazz the class to deserialize to
     * @return The event deserialized.
     * @throws RestException when fail to deserialize
     */
    public static Message deserializeEvent(String json, Class<?> klazz) throws RestException {
        try {
            return (Message) mapper.readValue(json, klazz);
        } catch (IOException e) {
            throw new RestException("Decoding JSON event: " + e.getMessage(), e);
        }
    }

    /**
     * Close the WebSocket connection
     *
     * @throws RestException when fail
     */
    public synchronized void disconnectWs() throws RestException {
        if (wsConnection != null) {
            liveActionList.remove(this);
            wsConnection.disconnect();
        }
        wsConnection = null;
    }

    public synchronized void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public synchronized HttpClient getHttpClient() {
        return this.httpClient;
    }

    public synchronized void setWsClient(WsClient wsClient) {
        this.wsClient = wsClient;
    }

    public synchronized WsClient getWsClient() {
        return this.wsClient;
    }

    public synchronized void setLiveActionList(List<BaseAriAction> liveActionList) {
        this.liveActionList = liveActionList;
    }

    public synchronized List<BaseAriAction> getLiveActionList() {
        return this.liveActionList;
    }

    public synchronized void setForcedResponse(String forcedResponse) {
        this.forcedResponse = forcedResponse;
    }

    public synchronized String getForcedResponse() {
        return this.forcedResponse;
    }

    public static void setObjectMapperLessStrict() {
        mapper.addHandler(new DeserializationProblemHandler() {
            @Override
            public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser p, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
                Collection<Object> propIds = (deserializer == null) ? null : deserializer.getKnownPropertyNames();
                UnrecognizedPropertyException e = UnrecognizedPropertyException.from(p, beanOrClass, propertyName, propIds);
                logger.warn(e.toString());
                return e.toString().length() > 0;
            }
        });
    }
}

//

