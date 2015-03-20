
package ch.loway.oss.ari4java.tools;

/**
 *
 * @author lenz
 */
public class HttpResponse {
    public int code = 0;
    public String description = "";

    public static HttpResponse build(int code, String descr) {
        HttpResponse r = new HttpResponse();
        r.code = code;
        r.description = descr;
        return r;
    }

}

