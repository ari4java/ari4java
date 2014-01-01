package ch.loway.oss.ari4java.tools;

/**
 * A REST error.
 * Made it inherit from ARIException so that you can trap ony one exception.
 *
 * @author lenz
 */
public class RestException extends ARIException {

    private static final long serialVersionUID = 1L;

    public RestException(String s) {
        super(s);
    }

    public RestException(Throwable cause) {
        super(cause);
    }
}
