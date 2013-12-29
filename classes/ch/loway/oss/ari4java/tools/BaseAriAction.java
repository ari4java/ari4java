
package ch.loway.oss.ari4java.tools;

import ch.loway.oss.ari4java.connector.AriConnector;
import ch.loway.oss.ari4java.generated.Event;
import ch.loway.oss.ari4java.generated.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

/**
 *
 *
 * @author lenz
 */
public class BaseAriAction {

    private String forcedResponse = null;

    public void forceResponse( String r ) {
        forcedResponse = r;
    }

    public AriConnector daddy = null;

    public void configure( AriConnector connector ) {
        daddy = connector;
    }


    public String httpAction( String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpResponse> errors ) throws RestException {

        if ( forcedResponse != null ) {
            return forcedResponse;
        } else {
            return httpActionImpl(uri, method, parametersQuery, parametersForm, errors);
        }


    }

    private String httpActionImpl( String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpResponse> errors ) throws RestException {
        return daddy.performHttp(uri, method, parametersQuery, parametersForm, errors);
    }

    /**
     * Deserialize a type
     *
     * @param json
     * @param klazz
     * @return Deserialized  type
     */
    public Object deserializeJson( String json, Class klazz) throws RestException {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue( json , klazz);
        } catch ( IOException e ) {
            e.printStackTrace( System.err );
            throw new RestException( "Decoding JSON: " + e.getMessage() );
        }
    }

    /**
     * Deserialize a list
     * 
     * @param json
     * @param klazzType
     * @return Deserialized list
     */

    public Object deserializeJson( String json, TypeReference klazzType ) throws RestException {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue( json , klazzType);
        } catch ( IOException e ) {
            throw new RestException( "Decoding JSON list: " + e.toString() );
        }

    }

    /**
     * Deserialize the event.
     *
     * @param json
     * @param klazz
     * @return The event deserialized.
     * @throws RestException
     */

    public Message deserializeEvent( String json, Class klazz ) throws RestException {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return (Message) mapper.readValue( json , klazz);
        } catch ( IOException e ) {
            e.printStackTrace( System.err );
            throw new RestException( "Decoding JSON event: " + e.toString() );
        }


    }

    public static class HttpParam {
        public String name = "";
        public String value = "";

        public static HttpParam build( String n, String v ) {
            HttpParam p = new HttpParam();
            p.name = n;
            p.value = v;
            return p;
        }

        public static HttpParam build( String n, int v ) {
            return build( n, Integer.toString(v));
        }

        public static HttpParam build( String n, long v ) {
            return build( n, Long.toString(v));
        }


        public static HttpParam build( String n, boolean v ) {
            return build( n, v ? "true" : "false");
        }


    }

    public static class HttpResponse {
        public int code = 0;
        public String description = "";

        public static  HttpResponse build( int code, String descr ) {
            HttpResponse r = new HttpResponse();
            r.code = code;
            r.description = descr;
            return r;
        }
    }

}

// $Log$
//
