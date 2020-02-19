package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.tools.ARIEncoder;
import ch.loway.oss.ari4java.tools.HttpResponseHandler;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.WsClientAutoReconnect;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * NettyWSClientHandler handles the transactions with the remote
 * WebSocket, forwarding to the client HttpResponseHandler interface.
 *
 * @author mwalton
 *
 */
@ChannelHandler.Sharable
public class NettyWSClientHandler extends NettyHttpClientHandler {
    
    final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;
    final HttpResponseHandler wsCallback;
    private WsClientAutoReconnect wsClient = null;
    private boolean shuttingDown = false;
    private Logger logger = LoggerFactory.getLogger(NettyWSClientHandler.class);

    public NettyWSClientHandler(WebSocketClientHandshaker handshaker, HttpResponseHandler wsCallback, WsClientAutoReconnect wsClient) {
        this(handshaker, wsCallback);
        this.wsClient = wsClient;
    }

    public NettyWSClientHandler(WebSocketClientHandshaker handshaker, HttpResponseHandler wsCallback) {
        this.handshaker = handshaker;
        this.wsCallback = wsCallback;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (!shuttingDown) {
            if (this.wsClient != null) {
                logger.debug("WS channel inactive - {}", ctx.toString());
                wsCallback.onDisconnect();
                wsClient.reconnectWs(new RestException("WS channel inactive"));
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Received Message - {}", msg.getClass().getSimpleName());
        Channel ch = ctx.channel();

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            responseBytes = new byte[response.content().readableBytes()];
            response.content().readBytes(responseBytes);
            HTTPLogger.traceResponse(response, responseBytes);
            if (!handshaker.isHandshakeComplete()) {
                logger.debug("Finish WS Handshake...");
                handshaker.finishHandshake(ch, response);
                handshakeFuture.setSuccess();
                return;
            }
            String error = "Unexpected FullHttpResponse (getStatus=" + response.status().toString() + ", content=" + getResponseText() + ')';
            logger.error(error);
            throw new Exception(error);
        }

        // call this so we can set the last received time
        wsCallback.onResponseReceived();

        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            String text = textFrame.content().toString(ARIEncoder.ENCODING);
            HTTPLogger.traceWebSocketFrame(text);
            responseBytes = text.getBytes(ARIEncoder.ENCODING);
            wsCallback.onSuccess(text);
        } else if (msg instanceof CloseWebSocketFrame) {
            ch.close();
            if (!shuttingDown) {
                if (this.wsClient != null) {
                    wsClient.reconnectWs(null);
                } else {
                    wsCallback.onDisconnect();
                }
            }
        } else if (msg instanceof PongWebSocketFrame) {
            wsClient.pong();
        } else {
            logger.warn("Unhandled WebSocketFrame: {}", msg.getClass().toString());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!shuttingDown)
            return;
        logger.error("exceptionCaught: {}", cause.getMessage(), cause);
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.fireExceptionCaught(cause);
        ctx.close();
        wsCallback.onFailure(cause);
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }

    public void setShuttingDown(boolean shuttingDown) {
        this.shuttingDown = shuttingDown;
    }
}

