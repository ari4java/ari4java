package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.actions.*;
import ch.loway.oss.ari4java.generated.actions.requests.EventsEventWebsocketGetRequest;
import ch.loway.oss.ari4java.generated.models.Application;
import ch.loway.oss.ari4java.generated.models.Message;
import ch.loway.oss.ari4java.tools.*;
import ch.loway.oss.ari4java.tools.http.NettyHttpClient;
import ch.loway.oss.ari4java.tools.tags.EventSource;

import java.net.URISyntaxException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ARI factory and helper class
 *
 * @author lenz
 * @author mwalton
 */
public class ARI {

    private final static String ALLOWED_IN_UID = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String appName = "";
    private AriVersion version;
    private HttpClient httpClient;
    private WsClient wsClient;
    private ActionEvents liveActionEvent = null;
    private AriSubscriber subscriptions = new AriSubscriber();
    private final CopyOnWriteArrayList<BaseAriAction> liveActionList = new CopyOnWriteArrayList<>();

    /**
     * Sets the client
     *
     * @param httpClient the http client
     */
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Sets the client
     *
     * @param wsClient the ws client
     */
    public void setWsClient(WsClient wsClient) {
        this.wsClient = wsClient;
    }

    /**
     * Sets the Version
     *
     * @param version the version
     */
    public void setVersion(AriVersion version) {
        this.version = version;
    }

    /**
     * Returns the current ARI version
     *
     * @return the version
     */
    public AriVersion getVersion() {
        return version;
    }

    /**
     * Get the implementation for a given action interface
     *
     * @param klazz the required action interface class
     * @param <T>   interface class
     * @return An implementation instance
     * @throws ARIException when error
     */
    @SuppressWarnings("unchecked")
    public <T> T getActionImpl(Class<T> klazz) throws ARIException {
        if (!klazz.getName().startsWith("ch.loway.oss.ari4java.generated.actions.Action")) {
            throw new ARIException("Invalid Class for action " + klazz);
        }
        Object action = buildConcreteImplementation(klazz);
        setupAction(action);
        if (klazz == ActionEvents.class) {
            liveActionEvent = (ActionEvents) action;
        }
        return (T) action;
    }

    /**
     * Get the implementation for a given model interface
     *
     * @param klazz the required model interface class
     * @param <T>   interface class
     * @return An implementation instance
     * @throws ARIException when error
     */
    @SuppressWarnings("unchecked")
    public <T> T getModelImpl(Class<T> klazz) throws ARIException {
        return (T) buildConcreteImplementation(klazz);
    }

    /**
     * Builds a concrete instance given an interface.
     * Note that we make no assumptions on the type of objects being built.
     *
     * @param klazz the class
     * @return the concrete implementation for that interface under the ARI in use.
     * @throws ARIException when error
     */
    private Object buildConcreteImplementation(Class<?> klazz) throws ARIException {
        if (version == null) {
            throw new ARIException("AriVersion not set");
        }
        Class<?> concrete = version.builder.getClassFactory().getImplementationFor(klazz);
        if (concrete == null) {
            throw new ARIException("No concrete implementation in " + version.name() + " for " + klazz);
        }
        try {
            return concrete.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ARIException("Unable to build concrete implementation for "
                    + klazz.getName() + " in " + version.name(), e);
        }
    }

    /**
     * Close an action object that is open for WebSocket interaction
     *
     * @param action the action object
     * @throws ARIException when error
     */
    public void closeAction(Object action) throws ARIException {
        if (!(action instanceof BaseAriAction)) {
            throw new ARIException("Class " + action.getClass().getName() + " is not an Action implementation");
        }
        BaseAriAction ba = (BaseAriAction) action;
        try {
            ba.disconnectWs();
        } catch (RestException e) {
            throw new ARIException(e.getMessage());
        }
    }

    /**
     * Minimal helper to build an instance of ARI
     *
     * @param url     url
     * @param app     app
     * @param user    user
     * @param pass    The password
     * @param version The required version
     * @return instance
     * @throws ARIException exception
     * @see ARI#build(String, String, String, String, AriVersion, boolean)
     */
    public static ARI build(String url, String app, String user, String pass, AriVersion version) throws ARIException {
        return build(url, app, user, pass, version, true);
    }

    /**
     * Helper to build an instance of ARI.
     * If the version is set as IM_FEELING_LUCKY the ARI version will be determined by 1st connecting
     * to the server and requesting the resources.json to extract the version number.
     *
     * @param url            The URL of the Asterisk web server, e.g. http://10.10.5.8:8088/ (defined in http.conf)
     * @param app            The app
     * @param user           The user name (defined in ari.conf)
     * @param pass           The password (defined in ari.conf)
     * @param version        The required version
     * @param testConnection Test the connection details by executing the ping operation
     * @return an instance
     * @throws ARIException If the url is invalid, or the version of ARI is not supported.
     */
    public static ARI build(String url, String app, String user, String pass, AriVersion version, boolean testConnection) throws ARIException {
        if (version == AriVersion.IM_FEELING_LUCKY) {
            AriVersion currentVersion = detectAriVersion(url, user, pass);
            return build(url, app, user, pass, currentVersion, testConnection);
        } else {
            try {
                return AriFactory.nettyHttp(url, user, pass, version, app, testConnection);
            } catch (URISyntaxException e) {
                throw new ARIException("Wrong URI format: " + url);
            }
        }
    }

    /**
     * Sets the application name.
     *
     * @param s app name
     */
    public void setAppName(String s) {
        this.appName = s;
    }

    /**
     * Return the current application name.
     *
     * @return the appName
     */
    public String getAppName() {
        return this.appName;
    }

    /**
     * Connect and detect the current ARI version.
     * If the ARI version is not supported,
     * will raise an exception as we have no bindings for it.
     *
     * @param url  url
     * @param user user
     * @param pass pass
     * @return the version of your server
     * @throws ARIException if the version is not supported
     */
    protected static AriVersion detectAriVersion(String url, String user, String pass) throws ARIException {
        try {
            NettyHttpClient hc = new NettyHttpClient();
            hc.initialize(url, user, pass);
            String response = hc.httpActionSync("/api-docs/resources.json", "GET", null, null, null);
            hc.destroy();
            String version = findVersionString(response);
            return AriVersion.fromVersionString(version);
        } catch (Exception e) {
            if (e instanceof ARIException) {
                throw (ARIException) e;
            }
            throw new ARIException(e.getMessage(), e);
        }
    }

    /**
     * Matches the version string out of the resources.json file.
     *
     * @param response res
     * @return a String describing the version reported from Asterisk.
     * @throws ARIException when error
     */
    private static String findVersionString(String response) throws ARIException {
        Pattern p = Pattern.compile(".apiVersion.:\\s*\"(.+?)\"", Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(response);
        if (m.find()) {
            return m.group(1);
        } else {
            throw new ARIException("Could find apiVersion");
        }
    }

    /**
     * This operation is the opposite of a build() - to be called in the final
     * clause where the ARI object is built.
     * <p>
     * In any case, it is good practice to have a way to deallocate stuff like
     * the websocket or any circular reference.
     */
    public void cleanup() {
        try {
            unsubscribeApplication();
        } catch (ARIException e) {
            // ignore on cleanup...
        }
        for (BaseAriAction liveAction : liveActionList) {
            try {
                closeAction(liveAction);
            } catch (ARIException e) {
                // ignore on cleanup...
            }
        }

        destroy(wsClient);
        if (wsClient != httpClient) {
            destroy(httpClient);
        }

        wsClient = null;
        httpClient = null;
    }

    /**
     * unsubscribe from all resources of the stasis application
     *
     * @throws RestException when error
     */
    private void unsubscribeApplication() throws RestException {
        Application application = applications().get(appName).execute();
        // unsubscribe from all channels
        for (int i = 0; i < application.getChannel_ids().size(); i++) {
            applications().unsubscribe(appName, "channel:" + application.getChannel_ids().get(i)).execute();
        }
        // unsubscribe from all bridges
        for (int i = 0; i < application.getBridge_ids().size(); i++) {
            applications().unsubscribe(appName, "bridge:" + application.getBridge_ids().get(i)).execute();
        }
        // unsubscribe from all endpoints
        for (int i = 0; i < application.getEndpoint_ids().size(); i++) {
            applications().unsubscribe(appName, "endpoint:" + application.getEndpoint_ids().get(i)).execute();
        }
        // unsubscribe from all deviceState
        for (int i = 0; i < application.getDevice_names().size(); i++) {
            applications().unsubscribe(appName, "deviceState:" + application.getDevice_names().get(i)).execute();
        }
    }

    /**
     * Does the destruction of a client. In a sense, it is a reverse factory.
     *
     * @param client the client object
     * @throws IllegalArgumentException All clients should be of a known type. Let's play it safe.
     */
    private void destroy(Object client) {
        if (client != null) {
            if (client instanceof NettyHttpClient) {
                NettyHttpClient nhc = (NettyHttpClient) client;
                nhc.destroy();
            } else {
                throw new IllegalArgumentException("Unknown client object " + client);
            }
        }
    }

    /**
     * Gets an instance of a message queue
     *
     * @return MessageQueue
     * @throws ARIException when error
     */
    public MessageQueue getWebsocketQueue() throws ARIException {
        return getWebsocketQueue(false);
    }

    /**
     * In order to avoid multi-threading for users, you can get a
     * MessageQueue object and poll on it for new messages.
     * This makes sure you don't really need to synchonize or be worried by
     * threading issues
     *
     * @param subscribeAll subscribe to all events
     * @return The MQ connected to your websocket.
     * @throws ARIException when error
     */
    public MessageQueue getWebsocketQueue(boolean subscribeAll) throws ARIException {
        if (liveActionEvent != null) {
            throw new ARIException("Websocket already present");
        }
        final MessageQueue q = new MessageQueue();
        AriCallback<Message> callback = new AriCallback<Message>() {
            @Override
            public void onSuccess(Message result) {
                q.queue(result);
            }

            @Override
            public void onFailure(RestException e) {
                q.queueError("Err:" + e.getMessage());
            }
        };
        EventsEventWebsocketGetRequest eventsRequest = events().eventWebsocket(appName);
        try {
            eventsRequest.setSubscribeAll(subscribeAll);
        } catch (UnsupportedOperationException e) {
            // ignore
        }
        eventsRequest.execute(callback);
        return q;
    }

    /**
     * Gets a ready to use Applications Action.
     *
     * @return ActionApplications
     */
    public ActionApplications applications() {
        return (ActionApplications) setupAction(version.builder().actionApplications());
    }

    /**
     * Gets a ready to use Asterisk Action.
     *
     * @return ActionAsterisk
     */
    public ActionAsterisk asterisk() {
        return (ActionAsterisk) setupAction(version.builder().actionAsterisk());
    }

    /**
     * Gets a ready to use Bridges Action.
     *
     * @return a Bridges object.
     */
    public ActionBridges bridges() {
        return (ActionBridges) setupAction(version.builder().actionBridges());
    }

    /**
     * Gets a ready to use Channels Action.
     *
     * @return ActionChannels
     */
    public ActionChannels channels() {
        return (ActionChannels) setupAction(version.builder().actionChannels());
    }

    /**
     * Gets a ready to use Device States Action.
     *
     * @return ActionDeviceStates
     */
    public ActionDeviceStates deviceStates() {
        return (ActionDeviceStates) setupAction(version.builder().actionDeviceStates());
    }

    /**
     * Gets a ready to use Endpoints Action.
     *
     * @return ActionEndpoints
     */
    public ActionEndpoints endpoints() {
        return (ActionEndpoints) setupAction(version.builder().actionEndpoints());
    }

    /**
     * Gets a ready to use Events Action.
     *
     * @return ActionEvents
     */
    public ActionEvents events() {
        liveActionEvent = (ActionEvents) setupAction(version.builder().actionEvents());
        return liveActionEvent;
    }

    /**
     * Gets a ready to use Mailboxes Action.
     *
     * @return ActionMailboxes
     */
    public ActionMailboxes mailboxes() {
        return (ActionMailboxes) setupAction(version.builder().actionMailboxes());
    }

    /**
     * Gets a ready to use Playbacks Action.
     *
     * @return ActionPlaybacks
     */
    public ActionPlaybacks playbacks() {
        return (ActionPlaybacks) setupAction(version.builder().actionPlaybacks());
    }

    /**
     * Gets a ready to use Recordings Action.
     *
     * @return ActionRecordings
     */
    public ActionRecordings recordings() {
        return (ActionRecordings) setupAction(version.builder().actionRecordings());
    }

    /**
     * Gets a ready to use Sounds Action.
     *
     * @return ActionSounds
     */
    public ActionSounds sounds() {
        return (ActionSounds) setupAction(version.builder().actionSounds());
    }

    /**
     * Checks if a BaseAriAction and sets the base properties
     *
     * @param a the action object
     * @return an Action object on which we'll set the default clients.
     * @throws IllegalArgumentException when error
     */
    private Object setupAction(Object a) throws IllegalArgumentException {
        if (a instanceof BaseAriAction) {
            BaseAriAction action = (BaseAriAction) a;
            action.setHttpClient(this.httpClient);
            action.setWsClient(this.wsClient);
            action.setLiveActionList(this.liveActionList);
        } else {
            throw new IllegalArgumentException("Object does not seem to be an Action implementation " + a.toString());
        }
        return a;
    }

    /**
     * Wrapper of the Thread.sleep() to avoid exception.
     *
     * @param ms how long is it going to sleep.
     */
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println("Interrupted: " + e.getMessage());
        }
    }

    /**
     * Generates a pseudo-random ID like "a4j.ZH6IA.IXEX0.TUIE8".
     *
     * @return the UID
     */
    public static String getUID() {
        StringBuilder sb = new StringBuilder(20);

        sb.append("a4j");

        for (int n = 0; n < 15; n++) {
            if ((n % 5) == 0) {
                sb.append(".");
            }
            int pos = (int) (Math.random() * ALLOWED_IN_UID.length());
            sb.append(ALLOWED_IN_UID.charAt(pos));
        }
        return sb.toString();

    }

    /**
     * Subscribes to an event source.
     *
     * @param m event
     * @return the Application object
     * @throws RestException when error
     */
    public Application subscribe(EventSource m) throws RestException {
        return subscriptions.subscribe(this, m);
    }

    /**
     * Unsubscribes from an event source.
     *
     * @param m event
     * @throws RestException when error
     */
    public void unsubscribe(EventSource m) throws RestException {
        subscriptions.unsubscribe(this, m);
    }

    /**
     * Unsubscribes from all known subscriptions.
     *
     * @throws RestException when error
     */
    public void unsubscribeAll() throws RestException {
        subscriptions.unsubscribeAll(this);
    }

    /**
     * This interface is used to go from an interface to its concrete implementation.
     */
    public interface ClassFactory {
        public Class<?> getImplementationFor(Class<?> interfaceClass);
    }
}
