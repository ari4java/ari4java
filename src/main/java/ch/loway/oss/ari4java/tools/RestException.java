package ch.loway.oss.ari4java.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A REST error.
 * Made it inherit from ARIException so that you can trap ony one exception.
 *
 * @author lenz
 */
public class RestException extends ARIException {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final long serialVersionUID = 1L;
    private int code = 0;
    private String message;
    private String response;

    public RestException(String s) {
        super(s);
        message = s;
    }

    public RestException(String s, int code) {
        super(s);
        message = extractError(s);
        this.code = code;
    }

    public RestException(String s, String r, int code) {
        this(s, code);
        String msg = extractError(message = r);
        if (!msg.equals(r)) {
            message = msg;
        }
    }

    public RestException(Throwable cause) {
        super(cause);
    }

    public RestException(String s, Throwable cause) {
        super(s, cause);
        message = extractError(s);
    }

    /**
     * parse the response and extract the "message" or "error" property
     * @param s
     * @return
     */
    private String extractError(String s) {
        if (s != null && s.indexOf("{") == 0) {
            response = s;
            try {
                JsonNode jsonNode = mapper.readTree(s);
                if (jsonNode.has("message")) {
                    return jsonNode.get("message").asText();
                } else if (jsonNode.has("error")) {
                    return jsonNode.get("error").asText();
                }
            } catch (JsonProcessingException e) {
                // ignoring
            }
        }
        return s;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        if (code != 0) {
            s += ": (HTTP " + code + " Error)";
        }
        if (message != null) {
            if (code == 0) {
                s += ":";
            }
            s += " " + message;
        }
        return s;
    }
}
