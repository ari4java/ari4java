package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.tools.ARIEncoder;
import ch.loway.oss.ari4java.tools.RestException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * HttpClientHandler handles the asynchronous response from the remote
 * HTTP server.
 *
 * @author mwalton
 */

@ChannelHandler.Sharable
public class NettyHttpClientHandler extends SimpleChannelInboundHandler<Object> {
    protected byte[] responseBytes;
    protected HttpResponseStatus responseStatus;
    private Throwable exception;
    private CountDownLatch latch = new CountDownLatch(1);

    private final Logger logger = LoggerFactory.getLogger(NettyHttpClientHandler.class);

    public void reset() {
        latch = new CountDownLatch(1);
        responseBytes = null;
        responseStatus = null;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            responseBytes = new byte[response.content().readableBytes()];
            response.content().readBytes(responseBytes);
            responseStatus = response.status();
            HTTPLogger.traceResponse(response, responseBytes);
            if (response.headers().get(HttpHeaderNames.CONTENT_LENGTH) != null &&
                    responseBytes.length != response.headers().getInt(HttpHeaderNames.CONTENT_LENGTH)) {
                logger.error("HTTP Content-Length: {}, but body read: {}",
                        response.headers().getInt(HttpHeaderNames.CONTENT_LENGTH), responseBytes.length);
            }
        } else if (msg != null) {
            latch.countDown();
            logger.warn("Unexpected: {}", msg);
            throw new RestException("Unknown object: " + msg.getClass().getSimpleName() + ", expecting FullHttpResponse");
        }
        latch.countDown();
    }

    public String getResponseText() {
        if (responseBytes == null || responseBytes.length == 0) {
            return null;
        }
        return new String(responseBytes, ARIEncoder.ENCODING);
    }

    public byte[] getResponseBytes() {
        if (responseBytes == null || responseBytes.length == 0) {
            return null;
        }
        return Arrays.copyOf(responseBytes, responseBytes.length);
    }

    public HttpResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public Throwable getException() {
        return exception;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.exception = cause;
        if (!(cause instanceof ReadTimeoutException)) {
            logger.error("Not a read timeout", cause);
        }
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }

    public void waitForResponse(int timeout) {
        try {
            latch.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

