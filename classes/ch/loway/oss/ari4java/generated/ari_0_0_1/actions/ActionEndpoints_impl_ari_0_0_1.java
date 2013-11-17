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

public class ActionEndpoints_impl_ari_0_0_1 extends BaseAriAction  implements ActionEndpoints {
/**********************************************************
 * Asterisk endpoints
 * 
 * List all endpoints.
 *********************************************************/
private void buildGetEndpoints() {
reset();
url = "/endpoints";
}

@Override
public List<? extends Endpoint> getEndpoints() throws RestException {
buildGetEndpoints();
String json = httpActionSync();
return deserializeJson( json, new TypeReference<List<Endpoint_impl_ari_0_0_1>>() {} ); 
}

@Override
public void getEndpoints(AriCallback<List<? extends Endpoint>> callback) {
buildGetEndpoints();
httpActionAsync(callback, new TypeReference<List<Endpoint_impl_ari_0_0_1>>() {});
}

/**********************************************************
 * Asterisk endpoints
 * 
 * List available endoints for a given endpoint technology.
 *********************************************************/
private void buildGetEndpointsByTech(String tech) {
reset();
url = "/endpoints/" + tech + "";
}

@Override
public List<? extends Endpoint> getEndpointsByTech(String tech) throws RestException {
buildGetEndpointsByTech(tech);
String json = httpActionSync();
return deserializeJson( json, new TypeReference<List<Endpoint_impl_ari_0_0_1>>() {} ); 
}

@Override
public void getEndpointsByTech(String tech, AriCallback<List<? extends Endpoint>> callback) {
buildGetEndpointsByTech(tech);
httpActionAsync(callback, new TypeReference<List<Endpoint_impl_ari_0_0_1>>() {});
}

/**********************************************************
 * Single endpoint
 * 
 * Details for an endpoint.
 *********************************************************/
private void buildGetEndpoint(String tech, String resource) {
reset();
url = "/endpoints/" + tech + "/" + resource + "";
}

@Override
public Endpoint getEndpoint(String tech, String resource) throws RestException {
buildGetEndpoint(tech, resource);
String json = httpActionSync();
return deserializeJson( json, Endpoint_impl_ari_0_0_1.class ); 
}

@Override
public void getEndpoint(String tech, String resource, AriCallback<Endpoint> callback) {
buildGetEndpoint(tech, resource);
httpActionAsync(callback, Endpoint_impl_ari_0_0_1.class);
}

};

