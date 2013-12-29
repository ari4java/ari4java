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

public interface ActionEndpoints {

// Endpoint get String String
/**********************************************************
 * Details for an endpoint.
 *********************************************************/
public Endpoint get(String tech, String resource) throws RestException;



// void list AriCallback<List<? extends Endpoint>> callback
/**********************************************************
 * 
 *********************************************************/
public void list(AriCallback<List<? extends Endpoint>> callback);



// List<? extends Endpoint> listByTech String
/**********************************************************
 * List available endoints for a given endpoint technology.
 *********************************************************/
public List<? extends Endpoint> listByTech(String tech) throws RestException;



// void get String String AriCallback<Endpoint> callback
/**********************************************************
 * 
 *********************************************************/
public void get(String tech, String resource, AriCallback<Endpoint> callback);



// void listByTech String AriCallback<List<? extends Endpoint>> callback
/**********************************************************
 * 
 *********************************************************/
public void listByTech(String tech, AriCallback<List<? extends Endpoint>> callback);



// List<? extends Endpoint> list
/**********************************************************
 * List all endpoints.
 *********************************************************/
public List<? extends Endpoint> list() throws RestException;


}
;
