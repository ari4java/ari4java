
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
        vars.add(build("key", key));
        if (variables != null) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                vars.add(build(entry.getKey(), entry.getValue()));
            }
        }
        return vars;
    }

    public static HttpParam build(String n, Integer v) {
        if (null == v)
            return build(n, (String)null);
        return build(n, Integer.toString(v));
    }

    public static HttpParam build(String n, Long v) {
        if (null == v)
            return build(n, (String)null);
        return build(n, Long.toString(v));
    }

    public static HttpParam build(String n, Boolean v) {
        if (null == v)
            return build(n, (String)null);
        return build(n, v ? "true" : "false");
    }

}

// $Log$
//
