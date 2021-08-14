package ch.loway.oss.ari4java.tools;

/**
 * Interface to implementation that can handle HTTP responses
 *
 * @author mwalton
 */
public interface HttpResponseHandler {

    /**
     * Client started rending response.
     */
    void onChReadyToWrite();

    /**
     * Server finished sending response
     */
    void onResponseReceived();

    /**
     * WebSocket disconnected
     */
    void onDisconnect();

    /**
     * All went well.
     *
     * @param response res
     */
    void onSuccess(String response);

    /**
     * All went well.
     *
     * @param response res
     */
    void onSuccess(byte[] response);

    /**
     * Something bad happened.
     *
     * @param e exception
     */
    void onFailure(Throwable e);

    /**
     * The last time in epoch since last response
     *
     * @return epoch
     */
    long getLastResponseTime();

    /**
     * To determine if String or Binary response
     *
     * @return the class
     */
    Class getType();
}
