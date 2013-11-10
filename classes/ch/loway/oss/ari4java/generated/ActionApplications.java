package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ActionApplications {

// Application applicationUnsubscribe String String
/**********************************************************
 * Unsubscribe an application from an event source.
 * Returns the state of the application after the subscriptions have changed
 *********************************************************/
public Application applicationUnsubscribe(String applicationName, String eventSource) throws RestException;



// List<? extends Application> getApplications
/**********************************************************
 * List all applications.
 *********************************************************/
public List<? extends Application> getApplications() throws RestException;



// Application applicationSubscribe String String
/**********************************************************
 * Subscribe an application to a event source.
 * Returns the state of the application after the subscriptions have changed
 *********************************************************/
public Application applicationSubscribe(String applicationName, String eventSource) throws RestException;



// Application getApplication String
/**********************************************************
 * Get details of an application.
 *********************************************************/
public Application getApplication(String applicationName) throws RestException;


}
;
