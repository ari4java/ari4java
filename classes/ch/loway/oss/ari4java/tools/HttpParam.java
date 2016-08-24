
package ch.loway.oss.ari4java.tools;

/**
 *
 * @author lenz
 */
public class HttpParam {
    public String name = "";
    public String value = "";

    public static HttpParam build(String n, String v) {
        HttpParam p = new HttpParam();
        p.name = n;
        p.value = v;
        return p;
    }

    public static HttpParam build(String n, int v) {
        return build(n, Integer.toString(v));
    }

    public static HttpParam build(String n, long v) {
        return build(n, Long.toString(v));
    }

    public static HttpParam build(String n, boolean v) {
        return build(n, v ? "true" : "false");
    }

}
//            public AriConnector daddy = null;
//
//        public void configure(AriConnector connector) {
//            daddy = connector;
//        }

// $Log$
//
