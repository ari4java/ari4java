
package ch.loway.oss.ari4java.tools;

import ch.loway.oss.ari4java.generated.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author lenz
 */
public class BaseAriAction {
	public class AriAsyncHandler<T> {
		private final AriCallback<? super T> callback;
		private Class<T> klazz;
		private TypeReference<T> klazzType;
		public AriAsyncHandler(AriCallback<? super T> callback, Class<T> klazz) {
			this.callback = callback;
			this.klazz = klazz;
		}
		public AriAsyncHandler(AriCallback<? super T> callback,
				TypeReference<T> klazzType) {
			this.callback = callback;
		}
		public void handleResponse(String json) {
			try {
				T result;
				if (klazz != null) {
					result = deserializeJson(json, klazz);
				} else {
					result = deserializeJson(json, klazzType);
				}
				this.callback.onSuccess(result);
			} catch (RestException e) {
				this.callback.onFailure(e);
			}
		}
	}

    private String forcedResponse = null;
    protected List<BaseAriAction.HttpParam> lParamQuery;
    protected List<BaseAriAction.HttpParam> lParamForm;
    protected List<BaseAriAction.HttpResponse> lE;
    protected String url;
    protected AriAsyncHandler<?> asyncHandler;
    
    protected void reset() {
    	lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
    	lParamForm = new ArrayList<BaseAriAction.HttpParam>();
    	lE = new ArrayList<BaseAriAction.HttpResponse>();
    	url = null;
    }
    public void forceResponse( String r ) {
        forcedResponse = r;
    }
    protected String httpActionSync() throws RestException {
        if ( forcedResponse != null ) {
            return forcedResponse;
        } else {
            throw new RestException("Not really implemented yet. ");
        }
    }
    protected void httpActionAsync(AriCallback<Void> callback) {
    	this.asyncHandler = new AriAsyncHandler<Void>(callback, Void.class);
        if ( forcedResponse != null ) {
            this.asyncHandler.handleResponse(forcedResponse);
        } 
    }
    protected <S,T extends S> void httpActionAsync(AriCallback<S> callback, Class<T> klazz) {
    	this.asyncHandler = new AriAsyncHandler<T>(callback, klazz);
        if ( forcedResponse != null ) {
            this.asyncHandler.handleResponse(forcedResponse);
        } 
    }
    protected <S,T extends S> void httpActionAsync(AriCallback<S> callback, TypeReference<T> klazzType) {
    	this.asyncHandler = new AriAsyncHandler<T>(callback, klazzType);
        if ( forcedResponse != null ) {
            this.asyncHandler.handleResponse(forcedResponse);
        } 
    }
    public String httpAction( String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpResponse> errors ) throws RestException {

        if ( forcedResponse != null ) {
            return forcedResponse;
        } else {
            throw new RestException("Not really implemented yet. ");
        }

    }

    /**
     * Deserialize a type
     *
     * @param json
     * @param klazz
     * @return Deserialized  type
     */
    public <T> T deserializeJson( String json, Class<T> klazz) throws RestException {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json , klazz);
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

    public <T> T deserializeJson( String json, TypeReference<T> klazzType ) throws RestException {

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

    public Message deserializeEvent( String json, Class<?> klazz ) throws RestException {

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
