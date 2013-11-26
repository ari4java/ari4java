package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;

public interface ActionApplications {

// void unsubscribe String String AriCallback<Application> callback
/**********************************************************
 * 
 *********************************************************/
public void unsubscribe(String applicationName, String eventSource, AriCallback<Application> callback);



// List<? extends Application> list
/**********************************************************
 * List all applications.
 *********************************************************/
public List<? extends Application> list() throws RestException;



// void subscribe String String AriCallback<Application> callback
/**********************************************************
 * 
 *********************************************************/
public void subscribe(String applicationName, String eventSource, AriCallback<Application> callback);



// void get String AriCallback<Application> callback
/**********************************************************
 * 
 *********************************************************/
public void get(String applicationName, AriCallback<Application> callback);



// Application get String
/**********************************************************
 * Get details of an application.
 *********************************************************/
public Application get(String applicationName) throws RestException;



// Application unsubscribe String String
/**********************************************************
 * Unsubscribe an application from an event source.
 * Returns the state of the application after the subscriptions have changed
 *********************************************************/
public Application unsubscribe(String applicationName, String eventSource) throws RestException;



// void list AriCallback<List<? extends Application>> callback
/**********************************************************
 * 
 *********************************************************/
public void list(AriCallback<List<? extends Application>> callback);



// Application subscribe String String
/**********************************************************
 * Subscribe an application to a event source.
 * Returns the state of the application after the subscriptions have changed
 *********************************************************/
public Application subscribe(String applicationName, String eventSource) throws RestException;


}
;
