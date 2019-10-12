package ch.loway.oss.ari4java.tools;

public class ARIException extends Exception {

    private static final long serialVersionUID = 1L;
    private String message = "";
    private Throwable cause = null;

    public ARIException(String message) {
        this.message = message;
    }

    public ARIException(Throwable t) {
        cause = t;
    }

    @Override
    public String getMessage() {
        if (cause != null) {
            return cause.getMessage();
        }
        return this.message;
    }

    @Override
    public synchronized Throwable getCause() {
        if (cause != null) {
            return cause;
        }
        return super.getCause();
    }
}
