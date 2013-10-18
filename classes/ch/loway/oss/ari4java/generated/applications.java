package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

/*** ====================================================
 * Interface for ch.loway.oss.ari4java.generated
 * Please do not edit.
 * ================================================= */
public interface applications {

// Application applicationUnsubscribe String String
/*** ====================================================
 * unknown comment
 * ================================================= */
public Application applicationUnsubscribe(String applicationName, String eventSource) throws RestException;



// List<Application> getApplications
/*** ====================================================
 * unknown comment
 * ================================================= */
public List<Application> getApplications() throws RestException;



// Application applicationSubscribe String String
/*** ====================================================
 * unknown comment
 * ================================================= */
public Application applicationSubscribe(String applicationName, String eventSource) throws RestException;



// Application getApplication String
/*** ====================================================
 * unknown comment
 * ================================================= */
public Application getApplication(String applicationName) throws RestException;


}
;
