
package ch.loway.oss.ari4java.codegen2.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class ApiOperation {

    public HttpMethod httpMethod = null;
    public String summary = "";
    public String notes = "";
    public String nickname = "";
    public DataType responseClass = null;
    public UpgradeTo upgrade = null;
    public WebsocketProtocol websocketProtocol = null;
					
    public List<ApiParameter> parameters = new ArrayList<ApiParameter>();
    public List<ApiErrorResponse> errorResponses = new ArrayList<ApiErrorResponse>();

    
    public static enum HttpMethod {
        GET,
        POST,
        DELETE,
        PUT
    }
    
    
    public static enum UpgradeTo {
        websocket
    }
    
    public static enum WebsocketProtocol {
        ari
    }
    
}

// $Log$
//
