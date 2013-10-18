
package ch.loway.oss.ari4java.tools;

import java.util.List;

/**
 *
 *
 * @author lenz
 */
public class BaseAriAction {


    public String httpAction( String uri, String method, List<HttpParam> parameters, List<HttpResponse> errors ) throws RestException {
        return "";
    }

    public Object deserializeJson( String json, Class klazz) {
        return null;
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
