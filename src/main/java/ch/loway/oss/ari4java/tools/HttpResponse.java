
package ch.loway.oss.ari4java.tools;

/**
 *
 * @author lenz
 */
public class HttpResponse {
    private int code = 0;
    private String description = "";

    public static HttpResponse build(int code, String descr) {
        HttpResponse r = new HttpResponse();
        r.setCode(code);
        r.setDescription(descr);
        return r;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

