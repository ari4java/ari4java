
package ch.loway.oss.ari4java.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static List<ch.loway.oss.ari4java.tools.HttpParam> build(String key, Map<String,String> variables) {
        ArrayList<ch.loway.oss.ari4java.tools.HttpParam> vars = new ArrayList<>();
        if (variables != null) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                vars.add(build(entry.getKey(), entry.getValue()));
            }
        }
        return vars;
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
