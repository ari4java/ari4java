package ch.loway.oss.ari4java.tools;

import java.util.List;
import java.util.Map;

/**
 * Interface to pluggable HTTP client implementation
 *
 * @author mwalton
 *
 */
public interface HttpClient {

    String httpActionSync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm,Map<String, Map<String, String>> parametersBody, List<HttpResponse> errors) throws RestException;

    void httpActionAsync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm,Map<String, Map<String, String>> parametersBody, List<HttpResponse> errors,  HttpResponseHandler responseHandler) throws RestException;
}
