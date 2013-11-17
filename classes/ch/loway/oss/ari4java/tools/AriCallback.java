package ch.loway.oss.ari4java.tools;

public interface AriCallback<T> {
    void onSuccess(T result);
    void onFailure(RestException e);
}
