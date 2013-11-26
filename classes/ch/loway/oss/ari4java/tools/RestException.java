
package ch.loway.oss.ari4java.tools;

import java.io.IOException;

/**
 * Errore REST.
 *
 * @author lenz
 */
public class RestException extends IOException {
	private static final long serialVersionUID = 1L;
	private String message;
	private Throwable cause;

	public RestException(String s) {
		this.message = s;
    }
	
	public RestException(Throwable cause) {
		this.cause = cause;
	}
	
	@Override
	public String getMessage() {
		if (cause != null)
			return cause.getMessage();
		return this.message;
	}
	
	@Override
	public synchronized Throwable getCause() {
		if (cause != null)
			return cause;
		return super.getCause();
	}
}
