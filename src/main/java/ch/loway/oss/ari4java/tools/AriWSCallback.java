package ch.loway.oss.ari4java.tools;

/**
 * Callback interface for WS ARI operations
 *
 * @param <T> The type of result returned in callback
 */
public interface AriWSCallback<T> extends AriCallback<T> {
    void onConnectionEvent(AriConnectionEvent event);
}
