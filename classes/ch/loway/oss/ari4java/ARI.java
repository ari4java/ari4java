package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.generated.ActionApplications;
import ch.loway.oss.ari4java.generated.ActionAsterisk;
import ch.loway.oss.ari4java.generated.ActionBridges;
import ch.loway.oss.ari4java.generated.ActionChannels;
import ch.loway.oss.ari4java.generated.ActionDeviceStates;
import ch.loway.oss.ari4java.generated.ActionEndpoints;
import ch.loway.oss.ari4java.generated.ActionEvents;
import ch.loway.oss.ari4java.generated.ActionPlaybacks;
import ch.loway.oss.ari4java.generated.ActionRecordings;
import ch.loway.oss.ari4java.generated.ActionSounds;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.HttpClient;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.WsClient;
import ch.loway.oss.ari4java.tools.http.NettyHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ARI factory and helper class
 * 
 * @author lenz
 * @author mwalton
 */
public class ARI {


    private String appName = "";
    private AriVersion version;
    private HttpClient httpClient;
    private WsClient wsClient;
    // Map of interfaces (key) and implementations (value) for actions
    private Map<Class<?>, Class<?>> actionMap = new HashMap<Class<?>, Class<?>>();
    // Map of interfaces (key) and implementations (value) for models
    private Map<Class<?>, Class<?>> modelMap = new HashMap<Class<?>, Class<?>>();

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setWsClient(WsClient wsClient) {
        this.wsClient = wsClient;
    }

    public void setVersion(AriVersion version) throws ARIException {
        String propFile = "ch/loway/oss/ari4java/generated/" + version.name().toLowerCase() + ".properties";
        Properties prop = new Properties();
        try {
            URL propUrl = ClassLoader.getSystemResource(propFile);
            if (propUrl == null) {
                throw new ARIException("Properties file " + propFile + " not found");
            }
            prop.load(propUrl.openStream());
            this.version = version;
            actionMap.clear();
            modelMap.clear();
            for (Object key : prop.keySet()) {
                if (key instanceof String && prop.get(key) instanceof String) {
                    try {
                        Class<?> apiClazz = ClassLoader.getSystemClassLoader().loadClass((String) key);
                        Class<?> implClazz = ClassLoader.getSystemClassLoader().loadClass((String) prop.get(key));
                        if (!apiClazz.isAssignableFrom(implClazz)) {
                            throw new ARIException(implClazz.getName() + " is not an implementation of " + apiClazz.getName());
                        }
                        if (BaseAriAction.class.isAssignableFrom(implClazz)) {
                            actionMap.put(apiClazz, implClazz);
                        } else {
                            modelMap.put(apiClazz, implClazz);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new ARIException("Cannot find API class " + key + " or implementation " + prop.get(key));
                    }
                }
            }
        } catch (IOException e) {
            throw new ARIException("Cannot load properties file " + propFile);
        }
    }

    /**
     * Get the implementation for a given action interface
     * 
     * @param klazz - the required action interface class 
     * @return An implementation instance
     * @throws ARIException
     */
    @SuppressWarnings("unchecked")
    public <T> T getActionImpl(Class<T> klazz) throws ARIException {
        if (version == null) {
            throw new ARIException("API version not set");
        }
        try {
            BaseAriAction action = (BaseAriAction) actionMap.get(klazz).newInstance();
            action.setHttpClient(this.httpClient);
            action.setWsClient(this.wsClient);
            return (T) action;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Do nothing
        }
        throw new ARIException("Unable to get action implementation for " + klazz.getName());
    }
//
//    /**
//     * Gets you an instance of AriConnector.
//     * When you add a version of the ARI, do not forget to add it here.
//     *
//     * @param mode Which connection mode (actually connector) you require.
//     * @param version The version of ARI you should bind to.
//     * @return a permanent connector instance.
//     */
//    public static AriConnector build(AriConnectorMode mode, AriVersion version, ConnectionCredentials creds) {
//
//        AriConnector c = new NettyConnector();
//        c.setCredentials(creds);
//
//        switch (version) {
//            case ARI_0_0_1:
//
//                ActionApplications_impl_ari_0_0_1 applicationsImpl001 = new ActionApplications_impl_ari_0_0_1();
//                applicationsImpl001.configure(c);
//
//                ActionAsterisk_impl_ari_0_0_1 asteriskImpl001 = new ActionAsterisk_impl_ari_0_0_1();
//                asteriskImpl001.configure(c);
//
//                c.setup(applicationsImpl001, asteriskImpl001);
//                break;
//
//            case ARI_1_0_0:
//                ActionApplications_impl_ari_1_0_0 applicationsImpl100 = new ActionApplications_impl_ari_1_0_0();
//                applicationsImpl100.configure(c);
//
//                ActionAsterisk_impl_ari_1_0_0 asteriskImpl100 = new ActionAsterisk_impl_ari_1_0_0();
//                asteriskImpl100.configure(c);
//
//                c.setup(applicationsImpl100, asteriskImpl100);
//                break;
//
//        }
//
//
//        return c;
//
//    }

    /**
     * Get the implementation for a given model interface
     * 
     * @param klazz - the required model interface class 
     * @return An implementation instance
     * @throws ARIException
     */
    @SuppressWarnings("unchecked")
    public <T> T getModelImpl(Class<T> klazz) throws ARIException {
        if (version == null) {
            throw new ARIException("API version not set");
        }
        try {
            return (T) modelMap.get(klazz).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Do nothing
        }
        throw new ARIException("Unable to get model implementation for " + klazz.getName());
    }

    /**
     * Close an action object that is open for WebSocket interaction
     * 
     * @param action - the action object
     * @throws ARIException
     */
    public void closeAction(Object action) throws ARIException {
        if (!(action instanceof BaseAriAction)) {
            throw new ARIException("Class " + action.getClass().getName() + " is not an Action implementation");
        }
        BaseAriAction ba = (BaseAriAction) action;
        try {
            ba.close();
        } catch (RestException e) {
            throw new ARIException(e.getMessage());
        }
    }

    /**
     * Builds a connector object for the specified ARI version.
     * If the version is set as IM_FEELING_LUCKY, then it will first try connecting,
     * will detect the current ARI version and will then connect to it.
     * This method uses Netty for both websocket and HTTP.
     *
     * As this sets everything up but does not do anything, we do not have any
     * information on whether this connection is valid or not.
     *
     * @param url The URL of the Asterisk web server, e.g. http://10.10.5.8:8088/ - defined in http.conf
     * @param user The user name (defined in ari.conf)
     * @param pass The password
     * @param version The reuired version
     * @return a connection object
     * @throws ARIException If the url is invalid, or the version of ARI is not supported.
     */

    public static ARI build(String url, String app, String user, String pass, AriVersion version) throws ARIException {

        if (version == AriVersion.IM_FEELING_LUCKY) {

            AriVersion currentVersion = detectAriVersion(url, user, pass);
            return build(url, app, user, pass, currentVersion);

        } else {

            try {

                ARI ari = new ARI();
                ari.appName = app;
                NettyHttpClient hc = new NettyHttpClient();
                hc.initialize(url, user, pass);
                ari.setHttpClient(hc);
                ari.setWsClient(hc);
                ari.setVersion( version );

                return ari;

            } catch (URISyntaxException e) {
                throw new ARIException("Wrong URI format: " + url);
            }
        }
    }

    /**
     * Connect and detect the current ARI version.
     * If the ARI version is not supported,
     * will raise an excepttion as we have no bindings for it.
     * 
     * @param url
     * @param user
     * @param pass
     * @return the version of your server
     * @throws ARIException if the version is not supported
     */

    protected static AriVersion detectAriVersion( String url, String user, String pass ) throws ARIException {
        
        String response = doHttpGet( url + "ari/api-docs/resources.json", user, pass );
        String version = findVersionString( response );
        return AriVersion.fromVersionString(version);

    }

    /**
     * Runs an HTTP GET and returns the text downloaded.
     *
     * @param urlWithParms
     * @param user
     * @param pwd
     * @return
     * @throws ARIException
     */

    private static String doHttpGet(String urlWithParms, String user, String pwd) throws ARIException {
        URL url = null;
        try {
            url = new URL(urlWithParms);
        } catch (MalformedURLException e) {
            throw new ARIException("MalformedUrlException: " + e.getMessage());
        }

        URLConnection uc = null;
        try {
            uc = url.openConnection();
        } catch (IOException e) {
            throw new ARIException("IOException: " + e.getMessage());
        }


        String userpass = user + ":" + pwd;
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

        uc.setRequestProperty("Authorization", basicAuth);
        InputStream is = null;
        try {
            is = uc.getInputStream();
        } catch (IOException e) {
            throw new ARIException("Cannot connect: " + e.getMessage());
        }

        BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();

        String line = null;
        try {
            line = buffReader.readLine();
        } catch (IOException e) {
            throw new ARIException("IOException: " + e.getMessage());
        }
        while (line != null) {
            response.append(line);
            response.append('\n');
            try {
                line = buffReader.readLine();
            } catch (IOException e) {
                throw new ARIException("IOException: " + e.getMessage());
            }
        }
        try {
            buffReader.close();
        } catch (IOException e) {
            throw new ARIException("IOException: " + e.getMessage());
        }
        //System.out.println("Response: " + response.toString());
        return response.toString();
    }

    /**
     * Matches the version string out of the resources.json file.
     * 
     * @param response
     * @return
     * @throws ARIException
     */

    private static String findVersionString(String response) throws ARIException {
        Pattern p = Pattern.compile(".apiVersion.:\\s+\"(.+?)\"", Pattern.MULTILINE + Pattern.CASE_INSENSITIVE );

        Matcher m = p.matcher(response);
        if ( m.find()  ) {
            return m.group(1);
        } else {
            throw new ARIException( "Cound not match apiVersion " );
        }
    }


    /**
     * This operation is the opposite of a build() - to be called in the final
     * clause where the ARI object is built.
     *      
     * In any case, it is good practice to have a way to deallocate stuff like
     * the websocket or any circular reference.
     */

    public void cleanup() {        
        
        destroy( wsClient );
        destroy( httpClient );

        wsClient = null;
        httpClient = null;
    }

    /**
     * Does the destruction of a client. In a sense, it is a reverse factory.
     *
     * @param client the client object
     * @throws IllegalArgumentException All clients should be of a known type. Let's play it safe.
     */

    private void destroy( Object client ) {
        if ( client != null ) {
            if ( client instanceof NettyHttpClient ) {
                NettyHttpClient nhc = (NettyHttpClient) client;
                nhc.destroy();
            } else {
                throw new IllegalArgumentException( "Unknown client object " + client );
            }
        }
    }

    /**
     * Gets us a ready to use object.
     * 
     * @return
     */
    public ActionApplications applications() {
        return (ActionApplications) setupAction(version.builder().actionApplications());
    }

    /**
     * Gets us a ready to use object.
     *
     * @return
     */
    public ActionAsterisk asterisk() {
        return (ActionAsterisk) setupAction(version.builder().actionAsterisk());
    }

    /**
     * Gets us a ready to use object.
     *
     * @return
     */
    public ActionBridges bridges() {
        return (ActionBridges) setupAction(version.builder().actionBridges());
    }

    /**
     * Gets us a ready to use object.
     *
     * @return
     */
    public ActionChannels channels() {
        return (ActionChannels) setupAction(version.builder().actionChannels());
    }

    /**
     * Gets us a ready to use object.
     *
     * @return
     */
    public ActionDeviceStates deviceStates() {
        return (ActionDeviceStates) setupAction(version.builder().actionDeviceStates());
    }

    /**
     * Gets us a ready to use object.
     *
     * @return
     */
    public ActionEndpoints endpoints() {
        return (ActionEndpoints) setupAction(version.builder().actionEndpoints());
    }

    /**
     * Gets us a ready to use object.
     *
     * @return
     */
    public ActionEvents events() {
        return (ActionEvents) setupAction(version.builder().actionEvents());
    }

    /**
     * Gets us a ready to use object.
     *
     * @return
     */
    public ActionPlaybacks playbacks() {
        return (ActionPlaybacks) setupAction(version.builder().actionPlaybacks());
    }

    /**
     * Gets us a ready to use object.
     *
     * @return
     */
    public ActionRecordings recordings() {
        return (ActionRecordings) setupAction(version.builder().actionRecordings());
    }

    /**
     * Gets us a ready to use object.
     *
     * @return
     */
    public ActionSounds sounds() {
        return (ActionSounds) setupAction(version.builder().actionSounds());
    }



    /**
     * This code REALLY smells bad.
     * Most likely we should either implement an interface, or push the clients
     * to the default builder.
     * 
     * @param a
     * @return
     * @throws IllegalArgumentException
     */

    public Object setupAction(Object a) throws IllegalArgumentException {
        if (a instanceof BaseAriAction) {
            BaseAriAction action = (BaseAriAction) a;
            action.setHttpClient(this.httpClient);
            action.setWsClient(this.wsClient);
        } else {
            throw new IllegalArgumentException("Object does not seem to be an Action implementation " + a.toString());
        }
        return a;
    }

}

