package ch.loway.oss.ari4java;

public class ARIException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message = "";
	public ARIException(String message) {
		this.message = message;
	}
	@Override
	public String getMessage() {
		return this.message;
	}
}
