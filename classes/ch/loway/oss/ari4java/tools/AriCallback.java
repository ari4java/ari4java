package ch.loway.oss.ari4java.tools;

/**
 * Callback interface for asynchronous ARI operations 
 * 
 * @author mwalton
 *
 * @param <T>
 * The type of result returned in callback
 */
public interface AriCallback<T> {
    void onSuccess(T result);
    void onFailure(RestException e);
}
