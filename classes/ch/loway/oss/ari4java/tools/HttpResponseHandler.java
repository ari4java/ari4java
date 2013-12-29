package ch.loway.oss.ari4java.tools;

/**
 * Interface to implementation that can handle HTTP responses
 * 
 * @author mwalton
 */
public interface HttpResponseHandler {
	void onConnect();
	void onDisconnect();
    void onSuccess(String response);
    void onFailure(Throwable e);
}
