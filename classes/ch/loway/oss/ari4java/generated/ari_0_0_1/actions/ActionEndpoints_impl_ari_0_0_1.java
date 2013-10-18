package ch.loway.oss.ari4java.generated.ari_0_0_1.actions;
import ch.loway.oss.ari4java.generated.*;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.*;
public class ActionEndpoints_impl_ari_0_0_1 extends BaseAriAction  implements ActionEndpoints {
/** =====================================================
 * Asterisk endpoints
 * 
 * List all endpoints.
 * ====================================================== */
public List<Endpoint> getEndpoints() throws RestException {
String url = "/endpoints";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lP, lE);
return (List<Endpoint>) deserializeJson( json, List.class); 
}

/** =====================================================
 * Asterisk endpoints
 * 
 * List available endoints for a given endpoint technology.
 * ====================================================== */
public List<Endpoint> getEndpointsByTech(String tech) throws RestException {
String url = "/endpoints/" + tech + "";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lP, lE);
return (List<Endpoint>) deserializeJson( json, List.class); 
}

/** =====================================================
 * Single endpoint
 * 
 * Details for an endpoint.
 * ====================================================== */
public Endpoint getEndpoint(String tech, String resource) throws RestException {
String url = "/endpoints/" + tech + "/" + resource + "";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lP, lE);
return (Endpoint) deserializeJson( json, Endpoint.class); 
}

};

