package ch.loway.oss.ari4java.tools;

import java.util.List;

/**
 * Interface to pluggable HTTP client implementation
 *
 * @author mwalton
 *
 */
public interface HttpClient {

    String httpActionSync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpParam> parametersBody,List<HttpResponse> errors) throws RestException;

    void httpActionAsync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpParam> parametersBody, List<HttpResponse> errors, HttpResponseHandler responseHandler) throws RestException;
}
