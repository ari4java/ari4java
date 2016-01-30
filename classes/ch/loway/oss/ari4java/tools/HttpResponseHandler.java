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
     * All went well.
     *
     * @param response
     */
    void onSuccess(String response);

    /**
     * Something bad happened.
     *
     * @param e
     */
    void onFailure(Throwable e);
}
