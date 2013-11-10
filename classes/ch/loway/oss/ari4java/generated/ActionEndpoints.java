package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ActionEndpoints {

// List<? extends Endpoint> getEndpoints
/**********************************************************
 * List all endpoints.
 *********************************************************/
public List<? extends Endpoint> getEndpoints() throws RestException;



// Endpoint getEndpoint String String
/**********************************************************
 * Details for an endpoint.
 *********************************************************/
public Endpoint getEndpoint(String tech, String resource) throws RestException;



// List<? extends Endpoint> getEndpointsByTech String
/**********************************************************
 * List available endoints for a given endpoint technology.
 *********************************************************/
public List<? extends Endpoint> getEndpointsByTech(String tech) throws RestException;


}
;
