package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.tools.ARIEncoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

public final class HTTPLogger {

    private HTTPLogger() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(HTTPLogger.class);

    private static final int MAX_LEN = 1000;

    public static void traceRequest(FullHttpRequest request, String body) {
        if (logger.isTraceEnabled()) {
            StringBuilder data = new StringBuilder();
            data.append(request.method());
            data.append(" ");
            data.append(request.uri());
            data.append(" ");
            data.append(request.protocolVersion());
            data.append("\n");
            for (Map.Entry<String, String> e: request.headers()) {
                data.append(e.getKey());
                data.append(": ");
                // replace authorization password
                if ("authorization".equalsIgnoreCase(e.getKey())) {
                    data.append("*****");
                } else {
                    data.append(e.getValue());
                }
                data.append("\n");
            }
            if (body != null) {
                data.append("\n");
                data.append(body);
            }
            logger.trace("HTTP Request:\n{}", data.toString().trim());
        }
    }

    public static void traceResponse(FullHttpResponse response, byte[] responseBytes) {
        if (logger.isTraceEnabled()) {
            StringBuilder data = new StringBuilder();
            data.append(response.protocolVersion());
            data.append(" ");
            data.append(response.status().toString());
            data.append("\n");
            for (Map.Entry<String, String> e: response.headers()) {
                data.append(e.getKey());
                data.append(": ");
                data.append(e.getValue());
                data.append("\n");
            }
            if (responseBytes != null) {
                data.append("\n");
                if ("audio/wav".equals(response.headers().get("Content-Type"))) {
                    data.append("[binary data...]");
                } else {
                    int len = responseBytes.length;
                    if (len > MAX_LEN) {
                        len = MAX_LEN;
                    }
                    data.append(new String(Arrays.copyOf(responseBytes, len), ARIEncoder.ENCODING));
                    if (responseBytes.length > MAX_LEN) {
                        data.append("[truncated...]");
                    }
                }
            }
            logger.trace("HTTP Response:\n{}", data.toString().trim());
        }
    }

    public static void traceWebSocketFrame(String text) {
        if (logger.isTraceEnabled()) {
            if (text.length() > MAX_LEN) {
                logger.trace("WS Frame:\n{}", text.substring(0, MAX_LEN));
            } else {
                logger.trace("WS Frame:\n{}", text);
            }
        }
    }

}
