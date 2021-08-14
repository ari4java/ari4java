package ch.loway.oss.ari4java;

public class ARIRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ARIRuntimeException(String message) {
        super(message);
    }

    public ARIRuntimeException(Throwable t) {
        super(t.getMessage(), t);
    }

    public ARIRuntimeException(String message, Throwable t) {
        super(message, t);
    }

}
