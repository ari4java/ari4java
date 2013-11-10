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
import com.fasterxml.jackson.core.type.TypeReference;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;

public class ActionEndpoints_impl_ari_0_0_1 extends BaseAriAction  implements ActionEndpoints {
/**********************************************************
 * Asterisk endpoints
 * 
 * List all endpoints.
 *********************************************************/
public List<? extends Endpoint> getEndpoints() throws RestException {
String url = "/endpoints";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (List<? extends Endpoint>) deserializeJson( json, new TypeReference<List<Endpoint_impl_ari_0_0_1>>() {} ); 
}

/**********************************************************
 * Asterisk endpoints
 * 
 * List available endoints for a given endpoint technology.
 *********************************************************/
public List<? extends Endpoint> getEndpointsByTech(String tech) throws RestException {
String url = "/endpoints/" + tech + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (List<? extends Endpoint>) deserializeJson( json, new TypeReference<List<Endpoint_impl_ari_0_0_1>>() {} ); 
}

/**********************************************************
 * Single endpoint
 * 
 * Details for an endpoint.
 *********************************************************/
public Endpoint getEndpoint(String tech, String resource) throws RestException {
String url = "/endpoints/" + tech + "/" + resource + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (Endpoint) deserializeJson( json, Endpoint_impl_ari_0_0_1.class ); 
}

};

