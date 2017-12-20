package ch.loway.oss.ari4java.tools;

import java.util.List;

/**
 * Interface to pluggable go-ari-proxy via RabbitMQ or NATS client implementation
 *
 */
public interface GoAriNatsClient {

    String httpActionSync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpParam> parametersBody,List<HttpResponse> errors) throws RestException;

    void httpActionAsync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpParam> parametersBody, List<HttpResponse> errors, HttpResponseHandler responseHandler) throws RestException;
}
