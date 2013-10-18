package ch.loway.oss.ari4java.generated.ari_0_0_1.actions;
import ch.loway.oss.ari4java.generated.*;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.*;
public class applications_impl_ari_0_0_1 extends BaseAriAction  implements applications {
public List<Application> getApplications() throws RestException {
String url = "/applications";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lP, lE);
return (List<Application>) deserializeJson( json, List.class); 
}

public Application getApplication(String applicationName) throws RestException {
String url = "/applications/" + applicationName + "";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Application does not exist.") );
String json = httpAction( url, "GET", lP, lE);
return (Application) deserializeJson( json, Application.class); 
}

public Application applicationSubscribe(String applicationName, String eventSource) throws RestException {
String url = "/applications/" + applicationName + "/subscription";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lP.add( BaseAriAction.HttpParam.build( "eventSource", eventSource) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Missing parameter.") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Application does not exist.") );
lE.add( BaseAriAction.HttpResponse.build( 422, "Event source does not exist.") );
String json = httpAction( url, "POST", lP, lE);
return (Application) deserializeJson( json, Application.class); 
}

public Application applicationUnsubscribe(String applicationName, String eventSource) throws RestException {
String url = "/applications/" + applicationName + "/subscription";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lP.add( BaseAriAction.HttpParam.build( "eventSource", eventSource) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Missing parameter; event source scheme not recognized.") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Application does not exist.") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Application not subscribed to event source.") );
lE.add( BaseAriAction.HttpResponse.build( 422, "Event source does not exist.") );
String json = httpAction( url, "DELETE", lP, lE);
return (Application) deserializeJson( json, Application.class); 
}

};

