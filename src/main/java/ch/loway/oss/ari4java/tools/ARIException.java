package ch.loway.oss.ari4java.tools;

public class ARIException extends Exception {

    private static final long serialVersionUID = 1L;

    public ARIException(String message) {
        super(message);
    }

    public ARIException(Throwable t) {
        super(t.getMessage(), t);
    }

    public ARIException(String message, Throwable t) {
        super(message, t);
    }

}
