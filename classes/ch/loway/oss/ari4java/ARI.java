
package ch.loway.oss.ari4java;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.HttpClient;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.WsClient;

/**
 * ARI factory and helper class
 * 
 * @author lenz
 * @author mwalton
 */
public class ARI {
    private AriVersion version;
    private HttpClient httpClient;
    private WsClient wsClient;
    // Map of interfaces (key) and implementations (value) for actions
    private Map<Class<?>,Class<?>> actionMap = new HashMap<Class<?>,Class<?>>();
    // Map of interfaces (key) and implementations (value) for models
    private Map<Class<?>,Class<?>> modelMap = new HashMap<Class<?>,Class<?>>();
    
    public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
    public void setWsClient(WsClient wsClient) {
		this.wsClient = wsClient;
	}
    
    public void setVersion(AriVersion version) throws ARIException {
    	String propFile = "ch/loway/oss/ari4java/generated/"+version.name().toLowerCase()+".properties";
    	Properties prop = new Properties();
    	try {
    		URL propUrl = ClassLoader.getSystemResource(propFile);
    		if (propUrl == null) {
        		throw new ARIException("Properties file "+propFile+" not found");
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
				    		throw new ARIException(implClazz.getName()+" is not an implementation of "+apiClazz.getName());
						}
						if (BaseAriAction.class.isAssignableFrom(implClazz)) {
							actionMap.put(apiClazz, implClazz);
						} else {
							modelMap.put(apiClazz, implClazz);
						}
					} catch (ClassNotFoundException e) {
			    		throw new ARIException("Cannot find API class "+key+" or implementation "+prop.get(key));
					}
    			}
    		}
    	} catch (IOException e) {
    		throw new ARIException("Cannot load properties file "+propFile);
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
    	throw new ARIException("Unable to get action implementation for "+klazz.getName());
    }

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
    	throw new ARIException("Unable to get model implementation for "+klazz.getName());
    }
    
    /**
     * Close an action object that is open for WebSocket interaction
     * 
     * @param action - the action object
     * @throws ARIException
     */
    public void closeAction(Object action) throws ARIException {
    	if (!(action instanceof BaseAriAction)) {
        	throw new ARIException("Class "+action.getClass().getName()+" is not an Action implementation");
    	}
    	BaseAriAction ba = (BaseAriAction) action;
    	try {
			ba.close();
		} catch (RestException e) {
			throw new ARIException(e.getMessage());
		}
    }
    
}

// $Log$
//
