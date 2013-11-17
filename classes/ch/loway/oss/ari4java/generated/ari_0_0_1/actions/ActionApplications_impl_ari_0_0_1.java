package ch.loway.oss.ari4java.generated.ari_0_0_1.actions;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;
import com.fasterxml.jackson.core.type.TypeReference;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;

public class ActionApplications_impl_ari_0_0_1 extends BaseAriAction  implements ActionApplications {
/**********************************************************
 * Stasis applications
 * 
 * List all applications.
 *********************************************************/
private void buildGetApplications() {
reset();
url = "/applications";
}

@Override
public List<? extends Application> getApplications() throws RestException {
buildGetApplications();
String json = httpActionSync();
return deserializeJson( json, new TypeReference<List<Application_impl_ari_0_0_1>>() {} ); 
}

@Override
public void getApplications(AriCallback<List<? extends Application>> callback) {
buildGetApplications();
httpActionAsync(callback, new TypeReference<List<Application_impl_ari_0_0_1>>() {});
}

/**********************************************************
 * Stasis application
 * 
 * Get details of an application.
 *********************************************************/
private void buildGetApplication(String applicationName) {
reset();
url = "/applications/" + applicationName + "";
lE.add( BaseAriAction.HttpResponse.build( 404, "Application does not exist.") );
}

@Override
public Application getApplication(String applicationName) throws RestException {
buildGetApplication(applicationName);
String json = httpActionSync();
return deserializeJson( json, Application_impl_ari_0_0_1.class ); 
}

@Override
public void getApplication(String applicationName, AriCallback<Application> callback) {
buildGetApplication(applicationName);
httpActionAsync(callback, Application_impl_ari_0_0_1.class);
}

/**********************************************************
 * Stasis application
 * 
 * Subscribe an application to a event source.
 * Returns the state of the application after the subscriptions have changed
 *********************************************************/
private void buildApplicationSubscribe(String applicationName, String eventSource) {
reset();
url = "/applications/" + applicationName + "/subscription";
lParamQuery.add( BaseAriAction.HttpParam.build( "eventSource", eventSource) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Missing parameter.") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Application does not exist.") );
lE.add( BaseAriAction.HttpResponse.build( 422, "Event source does not exist.") );
}

@Override
public Application applicationSubscribe(String applicationName, String eventSource) throws RestException {
buildApplicationSubscribe(applicationName, eventSource);
String json = httpActionSync();
return deserializeJson( json, Application_impl_ari_0_0_1.class ); 
}

@Override
public void applicationSubscribe(String applicationName, String eventSource, AriCallback<Application> callback) {
buildApplicationSubscribe(applicationName, eventSource);
httpActionAsync(callback, Application_impl_ari_0_0_1.class);
}

/**********************************************************
 * Stasis application
 * 
 * Unsubscribe an application from an event source.
 * Returns the state of the application after the subscriptions have changed
 *********************************************************/
private void buildApplicationUnsubscribe(String applicationName, String eventSource) {
reset();
url = "/applications/" + applicationName + "/subscription";
lParamQuery.add( BaseAriAction.HttpParam.build( "eventSource", eventSource) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Missing parameter; event source scheme not recognized.") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Application does not exist.") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Application not subscribed to event source.") );
lE.add( BaseAriAction.HttpResponse.build( 422, "Event source does not exist.") );
}

@Override
public Application applicationUnsubscribe(String applicationName, String eventSource) throws RestException {
buildApplicationUnsubscribe(applicationName, eventSource);
String json = httpActionSync();
return deserializeJson( json, Application_impl_ari_0_0_1.class ); 
}

@Override
public void applicationUnsubscribe(String applicationName, String eventSource, AriCallback<Application> callback) {
buildApplicationUnsubscribe(applicationName, eventSource);
httpActionAsync(callback, Application_impl_ari_0_0_1.class);
}

};

