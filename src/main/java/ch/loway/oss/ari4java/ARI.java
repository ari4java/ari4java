package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.actions.*;
import ch.loway.oss.ari4java.generated.actions.requests.EventsEventWebsocketGetRequest;
import ch.loway.oss.ari4java.generated.models.Application;
import ch.loway.oss.ari4java.generated.models.Message;
import ch.loway.oss.ari4java.tools.*;
import ch.loway.oss.ari4java.tools.tags.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private static Logger logger = LoggerFactory.getLogger(ARI.class);

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
     * @throws RuntimeException when version null
     */
    public AriVersion getVersion() {
        if (version == null) {
            throw new RuntimeException("AriVersion not set");
        }
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
        if (!klazz.getName().startsWith("ch.loway.oss.ari4java.generated.models.")) {
            throw new ARIException("Invalid Class for model " + klazz);
        }
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
        Class<?> concrete = getVersion().builder().getClassFactory().getImplementationFor(klazz);
        if (concrete == null) {
            throw new ARIException("No concrete implementation in " + getVersion().name() + " for " + klazz);
        }
        try {
            return concrete.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ARIException("Unable to build concrete implementation for "
                    + klazz.getName() + " in " + getVersion().name(), e);
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
        ba.disconnectWs();
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
     * If the version is set as IM_FEELING_LUCKY the AriFactory will determine the version by 1st connecting
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
        try {
            return AriFactory.nettyHttp(url, user, pass, version, app, testConnection);
        } catch (Exception e) {
            throw new ARIException(e.getMessage(), e);
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
        if (wsClient != null) {
            wsClient.destroy();
        }
        if (httpClient != null && !httpClient.equals(wsClient)) {
            httpClient.destroy();
        }
        wsClient = null;
        httpClient = null;
        liveActionEvent = null;
    }

    /**
     * unsubscribe from all resources of the stasis application
     *
     * @throws ARIException when error
     */
    private void unsubscribeApplication() throws ARIException {
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
     * Create the events Websocket with the provided callback
     *
     * @throws ARIException when error
     */
    public void eventsCallback(AriCallback<Message> callback) throws ARIException {
        eventsCallback(callback, false);
    }

    /**
     * Create the events Websocket with the provided callback
     *
     * @throws ARIException when error
     */
    public void eventsCallback(AriCallback<Message> callback, boolean subscribeAll) throws ARIException {
        if (liveActionEvent != null) {
            throw new ARIException("Websocket already present");
        }
        EventsEventWebsocketGetRequest eventsRequest = events().eventWebsocket(appName);
        try {
            eventsRequest.setSubscribeAll(subscribeAll);
        } catch (UnsupportedOperationException e) {
            logger.warn(e.getMessage(), e);
        }
        eventsRequest.execute(callback);
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
        eventsCallback(callback, subscribeAll);
        return q;
    }

    /**
     * Gets a ready to use Applications Action.
     *
     * @return ActionApplications
     */
    public ActionApplications applications() {
        return (ActionApplications) setupAction(getVersion().builder().actionApplications());
    }

    /**
     * Gets a ready to use Asterisk Action.
     *
     * @return ActionAsterisk
     */
    public ActionAsterisk asterisk() {
        return (ActionAsterisk) setupAction(getVersion().builder().actionAsterisk());
    }

    /**
     * Gets a ready to use Bridges Action.
     *
     * @return ActionBridges
     */
    public ActionBridges bridges() {
        return (ActionBridges) setupAction(getVersion().builder().actionBridges());
    }

    /**
     * Gets a ready to use Channels Action.
     *
     * @return ActionChannels
     */
    public ActionChannels channels() {
        return (ActionChannels) setupAction(getVersion().builder().actionChannels());
    }

    /**
     * Gets a ready to use Device States Action.
     *
     * @return ActionDeviceStates
     */
    public ActionDeviceStates deviceStates() {
        return (ActionDeviceStates) setupAction(getVersion().builder().actionDeviceStates());
    }

    /**
     * Gets a ready to use Endpoints Action.
     *
     * @return ActionEndpoints
     */
    public ActionEndpoints endpoints() {
        return (ActionEndpoints) setupAction(getVersion().builder().actionEndpoints());
    }

    /**
     * Gets a ready to use Events Action.
     *
     * @return ActionEvents
     */
    public ActionEvents events() {
        liveActionEvent = (ActionEvents) setupAction(getVersion().builder().actionEvents());
        return liveActionEvent;
    }

    /**
     * Gets a ready to use Mailboxes Action.
     *
     * @return ActionMailboxes
     */
    public ActionMailboxes mailboxes() {
        return (ActionMailboxes) setupAction(getVersion().builder().actionMailboxes());
    }

    /**
     * Gets a ready to use Playbacks Action.
     *
     * @return ActionPlaybacks
     */
    public ActionPlaybacks playbacks() {
        return (ActionPlaybacks) setupAction(getVersion().builder().actionPlaybacks());
    }

    /**
     * Gets a ready to use Recordings Action.
     *
     * @return ActionRecordings
     */
    public ActionRecordings recordings() {
        return (ActionRecordings) setupAction(getVersion().builder().actionRecordings());
    }

    /**
     * Gets a ready to use Sounds Action.
     *
     * @return ActionSounds
     */
    public ActionSounds sounds() {
        return (ActionSounds) setupAction(getVersion().builder().actionSounds());
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
            if (httpClient == null || wsClient == null) {
                throw new IllegalArgumentException("ARI possibly shutdown or not setup");
            }
            action.setHttpClient(httpClient);
            action.setWsClient(wsClient);
            action.setLiveActionList(liveActionList);
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
            logger.warn("Interrupted: " + e.getMessage(), e); //NOSONAR
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
        SecureRandom random = new SecureRandom();
        for (int n = 0; n < 15; n++) {
            if ((n % 5) == 0) {
                sb.append(".");
            }
            int pos = (int) (random.nextDouble() * ALLOWED_IN_UID.length());
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
     * Gets the package information an
     *
     * @return String
     */
    public String getBuildVersion() {
        String version = "x";
        try {
            if (getClass().getPackage().getImplementationVersion() != null) {
                version = getClass().getPackage().getImplementationVersion();
            }
        } catch (Exception e) {
            // oh well
        }
        InputStream stream = null;
        try {
            stream = getClass().getClassLoader().getResourceAsStream("build.properties");
            if (stream != null) {
                Properties p = new Properties();
                p.load(stream);
                if (p.containsKey("BUILD_NUMBER") && p.getProperty("BUILD_NUMBER") != null &&
                        !"x".equalsIgnoreCase(p.getProperty("BUILD_NUMBER"))) {
                    version += " (Build: " + p.getProperty("BUILD_NUMBER") + ")";
                }
            }
        } catch (IOException e) {
            // oh well
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // oh well
                }
            }
        }
        return version;
    }

    /**
     * This interface is used to go from an interface to its concrete implementation.
     */
    public interface ClassFactory {
        public Class<?> getImplementationFor(Class<?> interfaceClass);
    }
}
